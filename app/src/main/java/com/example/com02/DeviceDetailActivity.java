package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dto.DeviceDetailDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DeviceDetailActivity extends AppCompatActivity {

    TextView deviceNameTxt;
    TextView maxOutputTxt;
    TextView nowCapacityTxt;
    TextView usingTimeTxt;
    TextView contentsTxt;
    TextView averageDayTxt;
    TextView categoryTxt;
    TextView mAh;
    Button deleteBtn;
    Button editBtn;

    String deviceName;
    int userId;
    private StringBuilder url;
    DeviceDetailDTO deviceDetailDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        ImageButton backButton = findViewById(R.id.prevViewButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 페이지로 이동하는 코드
                Intent intent = new Intent(DeviceDetailActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        deviceNameTxt = (TextView) findViewById(R.id.deviceName);
        categoryTxt = (TextView) findViewById(R.id.categoryTxt);
        maxOutputTxt = (TextView) findViewById(R.id.maxOutputTxt);
        nowCapacityTxt = (TextView) findViewById(R.id.nowCapacityTxt);
        usingTimeTxt = (TextView) findViewById(R.id.usingTimeTxt);
        contentsTxt = (TextView) findViewById(R.id.contentsTxt);
        averageDayTxt = (TextView) findViewById(R.id.averageDaysTxt);
        mAh = (TextView) findViewById(R.id.mAhTxt);
        deleteBtn = (Button) findViewById(R.id.registerBtn);
        editBtn = (Button) findViewById(R.id.editBtn);

//        deviceName = getIntent().getStringExtra("deviceName");
//        deviceNameTxt.setText(deviceName);

        deviceName = "BoseHeadphone";

        url = new StringBuilder();
        RequestQueue queue = Volley.newRequestQueue(this);
        url.append("http://10.0.2.2:8080/capacity/deviceOfList/").append(deviceName);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                Log.d("success", response.toString());

                try {
                    boolean isSuccess = response.getBoolean("isSuccess");
                    if (isSuccess) {
                        JSONObject jsonObject = response.getJSONObject("result");

                        deviceDetailDTO = new DeviceDetailDTO(jsonObject.getString("deviceName"),
                                jsonObject.getString("category"),
                                jsonObject.getDouble("mah"),
                                jsonObject.getDouble("maximum_output"),
                                jsonObject.getString("contents"),
                                jsonObject.getString("usingTime"),
                                jsonObject.getString("nowCapacity"),
                                jsonObject.getString("averageDays"));

                        deviceNameTxt.setText(deviceDetailDTO.getDeviceName());
                        categoryTxt.setText(deviceDetailDTO.getCategory());
                        maxOutputTxt.setText(deviceDetailDTO.getMaximum_output().toString());
                        nowCapacityTxt.setText(deviceDetailDTO.getNowCapacity());
                        contentsTxt.setText(deviceDetailDTO.getContents());
                        usingTimeTxt.setText(deviceDetailDTO.getUsingTime());
                        averageDayTxt.setText(deviceDetailDTO.getAverageDays());
                        mAh.setText(deviceDetailDTO.getmAh().toString());


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceDetailActivity.this, "Json 파싱 오류", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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



        // 내 기기에서 삭제
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                url.append("http://10.0.2.2:8080/device/delete");

                // json request Body 생성
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("deviceName", deviceName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url.toString(), jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("success", response.getString("isSuccess"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

                //device 리스트 activity로 전환
//                Intent intent = new Intent(DeviceDetailActivity.this, DeviceListActivity.class);
//                startActivity(intent);
            }
        });
    }
}