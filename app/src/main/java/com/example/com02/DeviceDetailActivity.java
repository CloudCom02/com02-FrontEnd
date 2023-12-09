package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView capacityTxt;
    TextView usingTimeTxt;
    TextView contentsTxt;
    TextView voltTxt;
    TextView categoryTxt;
    Button deleteBtn;
    Button editBtn;

    String deviceName;
    private StringBuilder url;
    DeviceDetailDTO deviceDetailDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        deviceNameTxt = (TextView) findViewById(R.id.deviceName);
        maxOutputTxt = (TextView) findViewById(R.id.maxOutputTxt);
        capacityTxt = (TextView) findViewById(R.id.capacityTxt);
        usingTimeTxt = (TextView) findViewById(R.id.usingTimeTxt);
        contentsTxt = (TextView) findViewById(R.id.contentsTxt);
        voltTxt = (TextView) findViewById(R.id.voltTxt);
        categoryTxt = (TextView) findViewById(R.id.categoryTxt);
        deleteBtn = (Button) findViewById(R.id.registerBtn);
        editBtn = (Button) findViewById(R.id.editBtn);

//        deviceName = getIntent().getStringExtra("deviceName");
//        deviceNameTxt.setText(deviceName);

        deviceName = "Boseheadset";


        url = new StringBuilder();
        RequestQueue queue = Volley.newRequestQueue(this);
        url.append("http://10.0.2.2:8080/device/list/").append(deviceName);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("success", response.toString());

                try {
                    boolean isSuccess = response.getBoolean("isSuccess");

                    if (isSuccess) {
                        JSONObject jsonObject = response.getJSONObject("result");

                        deviceDetailDTO = new DeviceDetailDTO(jsonObject.getString("deviceName"),
                                jsonObject.getString("category"),
                                jsonObject.getDouble("entireCapacity"),
                                jsonObject.getDouble("maximum_output"),
                                jsonObject.getString("contents"),
                                jsonObject.getString("usingTime"),
                                jsonObject.getString("volt"));

                        deviceNameTxt.setText(deviceDetailDTO.getDeviceName());
                        maxOutputTxt.setText(deviceDetailDTO.getMaximum_output().toString());
                        capacityTxt.setText(deviceDetailDTO.getEntireCapacity().toString());
                        usingTimeTxt.setText(deviceDetailDTO.getUsingTime());
                        contentsTxt.setText(deviceDetailDTO.getContents());
                        voltTxt.setText(deviceDetailDTO.getVolt());
                        categoryTxt.setText(deviceDetailDTO.getCategory());
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
                url.append("http://10.0.2.2:8080/capacity/delete");

                // json request Body 생성
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("deviceName", deviceName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonBody,
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
            }
        });
    }
}