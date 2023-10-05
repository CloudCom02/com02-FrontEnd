package com.example.com02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;

    Button btnSearch;
    ListView listPaired, listSearch;
    BluetoothDevice bluetoothDevice;
    Set<BluetoothDevice> devices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayAdapter<String> deviceAddressArray;
    List<String> pairedDevList;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        listPaired = (ListView) findViewById(R.id.listPaired);
        listSearch = (ListView) findViewById(R.id.listSearch);

        Set<BluetoothDevice> pairedDevices;
        ArrayAdapter<String> btArrayAdapter; //배열과 어댑터 뷰 연결

        //Get permission of location info
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_PRIVILEGED
        };

        // Enable bluetooth
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null && btAdapter.isEnabled()){
            Toast.makeText(getApplicationContext(), "블루투스 미지원 기기", Toast.LENGTH_SHORT);
            System.out.println("블루투스 미지원 기기");

        }else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                            }
                        }
                    });
            startActivityResult.launch(enableBtIntent);
        }

        // Show paired devices
        devices = btAdapter.getBondedDevices();
        if(devices.size() > 0) {
            pairedDevList = new ArrayList<String>();
            for (BluetoothDevice device : devices) {
                pairedDevList.add(device.getName());
                System.out.println("디바이스 담는 중");
                device.getBluetoothClass();
            }
            final CharSequence[] charSequences = pairedDevList.toArray(new CharSequence[pairedDevList.size()]);
            pairedDevList.toArray(new CharSequence[pairedDevList.size()]);

            // 디바이스 클릭 시 Bluetooth Connect
            listPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    connectDevice(charSequences[i].toString());
                    System.out.println("Bluetooth connecting ...");
                }
            });

        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);

                onClickButtonSearch(view);
                System.out.println("Finding Bluetooth Device");
            }
        });
    }


    // Connect with Paired Device
    @SuppressLint("MissingPermission")
    public void connectDevice(String deviceName){
        for (BluetoothDevice tempDevice : devices){
            if (deviceName.equals(tempDevice.getName())){
                bluetoothDevice = tempDevice;
                break;
            }
        }
        Toast.makeText(getApplicationContext(), bluetoothDevice.getName() + "연결 완료", Toast.LENGTH_SHORT);
    }



    // Device Search
    @SuppressLint("MissingPermission")
    public void onClickButtonSearch(View view){

        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
        startActivity(discoverableIntent);
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHwAddress = device.getAddress();
                btArrayAdapter.add(deviceName);
                deviceAddressArray.add(deviceHwAddress);
                btArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Don't forget to unregister the receiver
        unregisterReceiver(receiver);
    }

}
