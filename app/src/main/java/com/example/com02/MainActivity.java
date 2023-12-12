package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 여기서 your_layout은 현재 화면의 XML 파일 이름입니다.

        Button findDeviceButton = findViewById(R.id.btn_findDevice);
        Button addDeviceButton = findViewById(R.id.btn_addDevice);
        Button mypage = findViewById(R.id.btn_mypage);
        Button myDeviceList = findViewById(R.id.deviceListBtn);

        findDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 클릭했을 때 실행되는 부분
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        myDeviceList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
}

//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//
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
//public class MainActivity extends AppCompatActivity {
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
//                Intent BLEintent = new Intent(getApplicationContext(), DeviceScanActivity.class);
//                startActivity(BLEintent);
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
//            } else {
//                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
//                Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
//                btnBluetoothOnOff.setText("블루투스 OFF");
//            }
//        }
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
//                bluetoothDevice = tempDevice;
//                break;
//            }
//        }
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
//}
