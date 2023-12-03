//package com.example.com02;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import android.app.Activity;
//import android.app.ListActivity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//public class BluetoothMainActivity extends AppCompatActivity {
//    TextView receiveData;
//    Button btnBluetoothOnOff;
//    Button btnConnect;
//    Button btnSearch;
//    ListView listPaired;
//    ListView listSearch;
//
//    BluetoothAdapter bluetoothAdapter;
//    Set<BluetoothDevice> pairedDevices;
//    List<String> listPairedDevices;
//
//    Handler bluetoothHandler;
//    ConnectedBluetoothThread threadConnectedBluetooth;
//    BluetoothDevice bluetoothDevice;
//    BluetoothSocket bluetoothSocket;
//
//    private LeDeviceListAdapter mLeDeviceListAdapter;
//    private BluetoothAdapter mBluetoothAdapter;
//    private boolean mScanning;
//    //private Handler mHandler; // bluetoothHandler와 겹치는 부분
//    private static final int REQUEST_ENABLE_BT = 1;
//    private static final long SCAN_PERIOD = 10000;
//
//
//    final static int BT_REQUEST_ENABLE = 1;
//    final static int BT_MESSAGE_READ = 2;
//    final static int BT_CONNECTING_STATUS = 3;
//    @SuppressLint({"HandlerLeak", "MissingInflatedId"})    // handler class의 외부 클래스 참조 유지를 방지
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        receiveData = (TextView)findViewById(R.id.tvReceiveData);
//        btnBluetoothOnOff = (Button)findViewById(R.id.btnBluetoothOn);
//        btnConnect = (Button)findViewById(R.id.btnConnect);
//        btnSearch = (Button)findViewById(R.id.btnSearch);
//        listPaired = (ListView)findViewById(R.id.listPaired);
//        listSearch = (ListView)findViewById(android.R.id.list);
//
//
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        btnBluetoothOnOff.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bluetoothOnOff();
//            }
//        });
//        btnConnect.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listPairedDevices();
//            }
//        });
//        btnSearch.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                scanLeDevice(true); //true - adapter 사용 가능한지 enable 체크값이어야 함
//            }
//        });
//
//
//        // 핸들러 생성
//        bluetoothHandler = new Handler(){
//            public void handleMessage(android.os.Message msg){
//                if(msg.what == BT_MESSAGE_READ){
//                    String readMessage = null;
//                    try {
//                        readMessage = new String((byte[]) msg.obj, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    receiveData.setText(readMessage);
//                }
//            }
//        };
//    }
//
//    // optionsMenu 부분 생
//
//
//    // Bluetooth On & Off
//    @SuppressLint("MissingPermission")
//    void bluetoothOnOff() {
//        if (bluetoothAdapter == null) {
//            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
//        } else {
//            if (bluetoothAdapter.isEnabled()) {
//                bluetoothAdapter.disable();
//                Toast.makeText(getApplicationContext(), "블루투스 비활성화", Toast.LENGTH_LONG).show();
//                btnBluetoothOnOff.setText("블루투스 ON");
//
//            } else {
//                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
//                Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
//                btnBluetoothOnOff.setText("블루투스 OFF");
//
//                // Device Scan Start
//
//
//
//
//            }
//        }
//    }
//
//    // 디바이스 스캔
//    @SuppressLint("MissingPermission")
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            bluetoothHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    invalidateOptionsMenu();
//                }
//            }, SCAN_PERIOD);
//            mScanning = true;
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//        invalidateOptionsMenu();
//    }
//
//    // 연결된 디바이스 목록
//    @SuppressLint("MissingPermission")
//    void listPairedDevices() {
//        if (bluetoothAdapter.isEnabled()) {
//            pairedDevices = bluetoothAdapter.getBondedDevices();
//
//            if (pairedDevices.size() > 0) {
//                listPairedDevices = new ArrayList<String>();
//                for (BluetoothDevice device : pairedDevices) {
//                    listPairedDevices.add(device.getName());
//                    listPairedDevices.add(device.getName() + "\n" + device.getAddress());
//                }
//                final CharSequence[] items = listPairedDevices.toArray(new CharSequence[listPairedDevices.size()]);
//                listPairedDevices.toArray(new CharSequence[listPairedDevices.size()]);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, listPairedDevices);
//                listPaired.setAdapter(adapter);
//
//                // 목록 클릭 시
//                listPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int item, long l) {
//                        connectSelectedDevice(items[item].toString());
//                    }
//                });
//            } else {
//                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    void connectSelectedDevice(String selectedDeviceName) {
//        for(BluetoothDevice tempDevice : pairedDevices) {
//            if (selectedDeviceName.equals(tempDevice.getName())) {
//>>>>>>> e0b8b21 ([CHORE] activity 옮김)
//                bluetoothDevice = tempDevice;
//                break;
//            }
//        }
//<<<<<<< HEAD
//        Toast.makeText(getApplicationContext(), bluetoothDevice.getName() + "연결 완료", Toast.LENGTH_SHORT);
//    }
//
//
//
//    // Device Search
//    @SuppressLint("MissingPermission")
//    public void onClickButtonSearch(View view){
//
//        Intent discoverableIntent =
//                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
//        startActivity(discoverableIntent);
//
//        /*
//        // Check if the device is already discovering
//        if(btAdapter.isDiscovering()){
//            btAdapter.cancelDiscovery();
//        } else {
//            if (btAdapter.isEnabled()) {
//                btAdapter.startDiscovery();
//                btArrayAdapter.clear();
//                if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
//                    deviceAddressArray.clear();
//                }
//                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                registerReceiver(receiver, filter);
//            } else {
//                Toast.makeText(getApplicationContext(), "bluetooth not on", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//         */
//
//    }
//
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        @SuppressLint("MissingPermission")
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if(BluetoothDevice.ACTION_FOUND.equals(action)){
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHwAddress = device.getAddress();
//                btArrayAdapter.add(deviceName);
//                deviceAddressArray.add(deviceHwAddress);
//                btArrayAdapter.notifyDataSetChanged();
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//
//        // Don't forget to unregister the receiver
//        unregisterReceiver(receiver);
//    }
//
//}

//        try {
//            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(
//                    UUID.fromString(SampleGattAttributes.BT_UUID));
//            bluetoothSocket.connect();
//            threadConnectedBluetooth = new ConnectedBluetoothThread(bluetoothSocket);
//            threadConnectedBluetooth.start();
//            bluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
//        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // User chose not to enable Bluetooth.
//        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
//            finish();
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    // Adapter for holding devices found through scanning.
//    // BT Device add & get 하기 위함
//    private class LeDeviceListAdapter extends BaseAdapter {
//        private ArrayList<BluetoothDevice> mLeDevices;
//        private LayoutInflater mInflator;
//        public LeDeviceListAdapter() {
//            super();
//            mLeDevices = new ArrayList<BluetoothDevice>();
//            mInflator = BluetoothMainActivity.this.getLayoutInflater();
//        }
//        public void addDevice(BluetoothDevice device) {
//            if(!mLeDevices.contains(device)) {
//                mLeDevices.add(device);
//            }
//        }
//        public BluetoothDevice getDevice(int position) {
//            return mLeDevices.get(position);
//        }
//        public void clear() {
//            mLeDevices.clear();
//        }
//        @Override
//        public int getCount() {
//            return mLeDevices.size();
//        }
//        @Override
//        public Object getItem(int i) {
//            return mLeDevices.get(i);
//        }
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//
//
////    // when pick the device in the list
////    @Override
////    @SuppressLint("MissingPermission")
////    protected void onListItemClick(ListView l, View v, int position, long id) {
////        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
////        if (device == null) return;
////        final Intent intent = new Intent(this, DeviceControlActivity.class);
////        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
////        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
////        if (mScanning) {
////            mBluetoothAdapter.stopLeScan(mLeScanCallback);
////            mScanning = false;
////        }
////        startActivity(intent);
////    }
//
//
//        @Override
//        @SuppressLint("MissingPermission")
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            BluetoothMainActivity.ViewHolder viewHolder;
//            // General ListView optimization code.
//            if (view == null) {
//                view = mInflator.inflate(R.layout.bluetooth_device_list, null);
//                viewHolder = new BluetoothMainActivity.ViewHolder();
//                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
//                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
//                view.setTag(viewHolder);
//            } else {
//                viewHolder = (BluetoothMainActivity.ViewHolder) view.getTag();
//            }
//            BluetoothDevice device = mLeDevices.get(i);
//            final String deviceName = device.getName();
//            if (deviceName != null && deviceName.length() > 0)
//                viewHolder.deviceName.setText(deviceName);
//            else
//                viewHolder.deviceName.setText(R.string.unknown_device);
//            viewHolder.deviceAddress.setText(device.getAddress());
//            return view;
//        }
//    }
//
//
//    // 블루투스 양방향 실시간 통신을 위한 스레드 클래스(소켓)
//    private class ConnectedBluetoothThread extends Thread {
//        private final BluetoothSocket mSocket;
//        private final InputStream mInStream;
//        private final OutputStream mOutStream;
//
//        public ConnectedBluetoothThread(BluetoothSocket socket) {
//            mSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//
//            mInStream = tmpIn;
//            mOutStream = tmpOut;
//        }
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (true) {
//                try {
//                    bytes = mInStream.available();
//                    if (bytes != 0) {
//                        SystemClock.sleep(100);
//                        bytes = mInStream.available();
//                        bytes = mInStream.read(buffer, 0, bytes);
//                        bluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                    }
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//        public void write(String str) {
//            byte[] bytes = str.getBytes();
//            try {
//                mOutStream.write(bytes);
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//        public void cancel() {
//            try {
//                mSocket.close();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
////
////    @Override
////    @SuppressLint("MissingPermission")
////    protected void onResume() {
////        super.onResume();
////        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
////        // fire an intent to display a dialog asking the user to grant permission to enable it.
////        if (!mBluetoothAdapter.isEnabled()) {
////            if (!mBluetoothAdapter.isEnabled()) {
////                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
////            }
////        }
////
////        // Initializes list view adapter.
////        mLeDeviceListAdapter = new MainActivity.LeDeviceListAdapter();
////        listSearch.setAdapter(mLeDeviceListAdapter);
////        //ListActivity 클래스인데 조취를 어케 취하지
////        scanLeDevice(true);
////    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        scanLeDevice(false);
//        mLeDeviceListAdapter.clear();
//    }
//
//
//
//    // Device scan callback
//    private BluetoothAdapter.LeScanCallback mLeScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//                @Override
//                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mLeDeviceListAdapter.addDevice(device);
//                            mLeDeviceListAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            };
//    static class ViewHolder {
//        TextView deviceName;
//        TextView deviceAddress;
//    }
//}
