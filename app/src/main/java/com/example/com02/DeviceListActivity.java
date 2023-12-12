package com.example.com02;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.dto.DeviceAdapter;
import com.example.dto.DeviceDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DeviceListActivity extends AppCompatActivity {

    ImageButton bluetoothBtn;
    private StringBuilder url;
    private List<DeviceDTO> deviceDTOList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    public static final String mypreference = "com02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        ListView listView = findViewById(R.id.tableList);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        long userId = sharedPreferences.getLong("userIdx",-1);
        if (userId != -1) {
            Log.d("userIdx", String.valueOf(userId));
        } else {
            Log.d("userIdx", "No userIdx found");
        }


        //어디에나 있는 prev 버튼
        ImageButton backButton = findViewById(R.id.prevViewButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 페이지로 이동하는 코드
                Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        deviceDTOList = new ArrayList<>();

        url = new StringBuilder();
        RequestQueue queue = Volley.newRequestQueue(this);

        // 사용자의 디바이스 리스트
        url.append("http://test.com02cloud.kro.kr/capacity/list/").append(userId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("success", response.toString());

                try {
                    boolean isSuccess = response.getBoolean("isSuccess");

                    if (isSuccess) {
                        JSONArray jsonArray = response.getJSONArray("result");

                        DeviceDTO deviceDTO;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            deviceDTO = new DeviceDTO(jsonObject.getString("deviceName"),
                                    jsonObject.getString("category"));

                            deviceDTOList.add(deviceDTO);
                        }
                        DeviceAdapter adapter = new DeviceAdapter(DeviceListActivity.this, deviceDTOList);
                        listView.setAdapter(adapter);
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

        DeviceAdapter adapter = new DeviceAdapter(this, deviceDTOList);
        listView.setAdapter(adapter);


        // list item 누를 시 상세 정보 창으로 전환
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()   {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceDTO clickDevice = deviceDTOList.get(position);
                Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                Log.d("인텐트 값 정보", String.valueOf(clickDevice.getDeviceName()));
                intent.putExtra("deviceName", clickDevice.getDeviceName());
                startActivity(intent);
            }
        });


        // 블루투스 연결 화면 전환
        bluetoothBtn = findViewById(R.id.bluetoothButton);
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