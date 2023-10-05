package com.example.com02;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionCheck {

    private Context context;
    private Activity activity;

    // 요청할 권한
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    // 권한 요청 시 발생하는 창 결과값
    private List<Object> permissionList;
    private final int MULTIPEL_PERMISSIONS = 1023;

    public PermissionCheck(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    // 허용할 권한 요청 남았는지 체크
    public boolean checkPermission(){
        int result;
        permissionList = new ArrayList<>();

        for (String pm : permissions){
            result = ContextCompat.checkSelfPermission(context, pm);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(pm);
            }
        }
        return  permissionList.isEmpty();
    }

    // 권한 허용 요청
    public void requestPermission(){
        ActivityCompat.requestPermissions(activity,
                permissionList.toArray(new String[permissionList.size()]), MULTIPEL_PERMISSIONS);
    }

    //권한 요청에 대한 결과 처리
    public boolean permissionResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grandResults){
        if (requestCode == MULTIPEL_PERMISSIONS && (grandResults.length > 0)){
            for(int i = 0; i < grandResults.length; i++){
                if(grandResults[i] == -1){
                    // 0: 허용, -1: 거부
                    return false;
                }
            }
        }
        return true;
    }
}
