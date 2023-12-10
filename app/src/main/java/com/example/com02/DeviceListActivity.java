package com.example.com02;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.com02.mine.BluetoothMainActivity;
import com.example.dto.DeviceAdapter;
import com.example.dto.DeviceDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {

    ImageButton bluetoothBtn;
    private StringBuilder url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        bluetoothBtn = findViewById(R.id.bluetoothButton);

        List<DeviceDTO> deviceDTOList = null;

        url = new StringBuilder();
        int user = 2;       //수정 필요

        // http를 통한 DB 연동 (capacity of user 값 읽어오기)
        RequestQueue queue = Volley.newRequestQueue(this);
        url.append("http://10.0.2.2:8080/capacity/list").append(user);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("success", response.toString());

                try {
                    boolean isSuccess = response.getBoolean("isSuccess");

                    if (isSuccess) {
                        JSONArray jsonArray = response.getJSONArray("result");

                        DeviceDTO deviceDTO = null;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            deviceDTO = new DeviceDTO(jsonObject.getString("devicename"),
                                    jsonObject.getString("category"),
                                    jsonObject.getLong("batteryLevel"),
                                    jsonObject.getBoolean("isNeedCharge"));

                            deviceDTOList.set(i, deviceDTO);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceListActivity.this, "Json 파싱 오류", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    Log.d("volleyError", "에러 발생" + error.toString());
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(errorResponse);
                            String errorMessage = jsonObject.getString("errorMessage");
                            // Handle BaseException
                            Log.d("VolleyError", "BaseException: " + errorMessage);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        });

        queue.add(request);

        ListView listView = findViewById(R.id.listView);
        DeviceAdapter adapter = new DeviceAdapter(this, deviceDTOList);
        listView.setAdapter(adapter);


        // list item 누를 시 상세 정보 창으로 전환
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()   {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceDTO clickDevice = deviceDTOList.get(position);
                Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                intent.putExtra("deviceName", clickDevice.getDeviceName());
                startActivity(intent);
            }
        });

        // 블루투스 연결 화면 전환
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
    }



    //bluetooth main activity로 연결하는 창
    private void showPopup() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_connect_check, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 연결")
                .setMessage("블루투스 연결 창으로 넘어가시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DeviceListActivity.this, BluetoothMainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니요", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}