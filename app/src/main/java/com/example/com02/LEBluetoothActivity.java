package com.example.com02;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Gravity;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class LEBluetoothActivity extends Activity {
    // extends ListActivity

    private PermissionCheck permission;

    private static final int REQUEST_ENABLE_BT = 99;
    private static final long SCAN_PERIOD = 100000; //scanning for 10 seconds
    public static final String TAG = "DeviceListBTActivity";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    //private BTAdapter deviceAdapter;

    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    ArrayList<Integer> rssiList = new ArrayList<>();
    private BluetoothLeScanner scanner;
    private Handler handler;
    private boolean scanning;

    ListView listView;
    private TextView emptyList;
    private Button cancelButton;


    /* 전체 과정  PM -> SCAN -> CONNECT -> R/W     */

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bluetooth);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        permissionCheck();

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

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


    /*    **********************     */



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

