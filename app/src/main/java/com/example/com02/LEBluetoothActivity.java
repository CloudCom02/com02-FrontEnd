package com.example.com02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
//import java.util.logging.Handler; //없어도 될듯

public class LEBluetoothActivity extends Activity {
    // extends ListActivity

    private PermissionCheck permission;

    private static final int REQUEST_ENABLE_BT = 99;
    private static final long SCAN_PERIOD = 100000; //scanning for 10 seconds
    public static final String TAG = "DeviceListBTActivity";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    //private BTAdapter deviceAdapter;
    private LeDeviceListAdapter leDeviceListAdapter;

    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    ArrayList<Integer> rssiList = new ArrayList<>();
    private BluetoothLeScanner scanner;
    private Handler handler;
    private boolean scanning;

    ListView listView;
    private TextView emptyList;
    private Button cancelButton;


    /* 전체 과정  PM -> SCAN -> CONNECT -> R/W */

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        permissionCheck();

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // 블루투스 Enable 확인
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }

    // [Permission] 권한 체크
    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            permission = new PermissionCheck(this, this);

            // false일 경우 권한 요청
            if (!permission.checkPermission()) {
                permission.requestPermission();
            }
        }
    }

    // [Permission] Request permission 대한 결과값
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 리턴이 false일 경우 다시 권한 요청
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            permission.requestPermission();
        }
    }


    // [Scan] 디바이스 스캔
    /*
    특정 유형의 주변 장치만 스캔하고 싶다면
    startLeScan(UUID[], BluetoothAdapter.LeScanCallback)를 호출하여
    앱이 지원하는 GATT 서비스를 지정하는 UUID 객체의 배열을 제공해야 합니다.
     */

    @SuppressLint("MissingPermission")
    private void scanLeDevice(final boolean enable){
        if (enable){
            // 이전 스캔 디바이스 존재 시 스캔 중단
            handler.postDelayed(new Runnable(){
                @SuppressLint("MissingPermission")
                @Override
                public void run(){
                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        } else{
            scanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            leDeviceListAdapter.addDevice(device);
                            leDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };


    // [Scan] Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = LEBluetoothActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }



        /***************/


        // [Scan] get Bluetooth Devices List
        @Override
        @SuppressLint("MissingPermission")
        public View getView(int i, View view, ViewGroup viewGroup) {
            DeviceScanActivity.ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.bluetooth_device_list, null);
                viewHolder = new DeviceScanActivity.ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (DeviceScanActivity.ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // 등록 시 MAC adress DB 저장 & 저장 플래스 설정
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}


