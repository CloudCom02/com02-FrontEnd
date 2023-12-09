package com.example.com02.mine;

import static com.example.com02.mine.SampleGattAttributes.UUID_NOTIFICATION;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private final static UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private final static UUID BATTERY_LEVEL_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;



    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    // receive data from the device.
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        connectionState = STATE_CONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                bluetoothGatt.discoverServices());
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        connectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        broadcastUpdate(intentAction);
                    }
                }
                // 블루투스 연결 == GATT 서버와 연결

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status){
                    if (status == BluetoothGatt.GATT_SUCCESS){
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    } else{
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt,
                                 BluetoothGattCharacteristic characteristic, int status){
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic)  {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                }
            };



    public void broadcastUpdate(final String action){
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, BluetoothGattCharacteristic characteristic){
        final Intent intent = new Intent(action);

        // 장치의 배터리 값
        Log.v(TAG, "characteristic.getStringValue(0) ="
                + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0));
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_BATTERY,
                Character.highSurrogate(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)));
        sendBroadcast(intent);
    }


    // 프로필로부터 배터리 정보 가져오기
    @SuppressLint("MissingPermission")
    public void getBattery(){
        BluetoothGattService batteryService = bluetoothGatt.getService(BATTERY_SERVICE_UUID);
        if(batteryService == null){
            Log.d(TAG, "Battery service not found!");
            return;
        }
        BluetoothGattCharacteristic batteryLevel = batteryService.getCharacteristic(BATTERY_LEVEL_UUID);
        if(batteryLevel == null){
            Log.d(TAG, "Battery level not found!");
            return;
        }
        bluetoothGatt.readCharacteristic(batteryLevel);
        Log.v(TAG, "batteryLevel = " + bluetoothGatt.readCharacteristic(batteryLevel));
    }


    public boolean initialize() {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        return true;
    }


    @SuppressLint("MissingPermission")
    public boolean connect(final String address){
        if(bluetoothAdapter == null || address == null){
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device. Try to reconnect.
        if(bluetoothDeviceAddress != null && address.equals(bluetoothDeviceAddress)
                            && bluetoothGatt != null){
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if(bluetoothGatt.connect()){
                connectionState = STATE_CONNECTING;
                return true;
            } else{
                return false;
            }
        }

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        if(device == null){
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }

        bluetoothGatt = device.connectGatt(this, false, gattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        bluetoothDeviceAddress = address;
        connectionState = STATE_CONNECTING;
        return true;
    }


    // 블루투스 디바이스 상태 변화 시 알림
    @SuppressLint("MissingPermission")
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        if (UUID_NOTIFICATION.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }


    // Binder: 서로 다른 프로세스에 위치한 자원 & 코드 연결 (다른 프로세스의 함수 실행)
    public class LocalBinder extends Binder {
        public BluetoothLeService getService(){return BluetoothLeService.this;}
    }

    // Binder 호출 & 프로세스 간 재귀작업 등
    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public boolean onUnbind(Intent intent){
        close();
        return super.onUnbind(intent);
    }

    public BluetoothLeService() {
    }


    @SuppressLint("MissingPermission")
    public void readCharacteristic(BluetoothGattCharacteristic characteristic){
        if(bluetoothAdapter == null || bluetoothGatt == null){
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    public List<BluetoothGattService> getSupportedGattServices(){
        if(bluetoothGatt == null) return null;

        return bluetoothGatt.getServices();
    }

    @SuppressLint("MissingPermission")
    public void close(){
        if(bluetoothGatt == null){
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }


    @SuppressLint("MissingPermission")
    public void disconnect(){
        if(bluetoothAdapter == null || bluetoothGatt == null){
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.disconnect();
    }
}