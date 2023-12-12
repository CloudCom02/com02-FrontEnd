package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText deviceNameEdit;
    TextView maxOutputTxt;
    EditText nowCapacityEdit;
    TextView usingTimeTxt;
    TextView contentsTxt;
    EditText averageDayEdit;
    TextView categoryTxt;
    EditText mAhEdit;
    Button deleteBtn;
    Button editBtn;
    TextView capacityOfUserId;
    String deviceName;
    private StringBuilder url;
    DeviceDetailDTO deviceDetailDTO;
    private JsonObjectRequest request;

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

        deviceNameEdit = (EditText) findViewById(R.id.deviceName);
        categoryTxt = (TextView) findViewById(R.id.categoryTxt);
        maxOutputTxt = (TextView) findViewById(R.id.maxOutputTxt);
        nowCapacityEdit = (EditText) findViewById(R.id.nowCapacityTxt);
        usingTimeTxt = (TextView) findViewById(R.id.usingTimeTxt);
        contentsTxt = (TextView) findViewById(R.id.contentsTxt);
        averageDayEdit = (EditText) findViewById(R.id.averageDaysTxt);
        mAhEdit = (EditText) findViewById(R.id.mAhTxt);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        editBtn = (Button) findViewById(R.id.editBtn);
        capacityOfUserId = (TextView) findViewById(R.id.capacityId);

        deviceName = getIntent().getStringExtra("deviceName");
        deviceNameEdit.setText(deviceName);
        Log.d("인텐트로 받아온 값", String.valueOf(deviceName));

        url = new StringBuilder();
        RequestQueue queue = Volley.newRequestQueue(this);
        url.append("http://test.com02cloud.kro.kr/capacity/deviceOfList/").append(deviceName);

        request = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success", response.toString());

                        try {
                            boolean isSuccess = response.getBoolean("isSuccess");
                            if (isSuccess) {
                                JSONObject jsonObject = response.getJSONObject("result");

                                deviceDetailDTO = new DeviceDetailDTO(jsonObject.getString("deviceName"),
                                        jsonObject.getString("category"),
                                        jsonObject.getInt(  "mAh"),
                                        jsonObject.getDouble("maximum_output"),
                                        jsonObject.getString("contents"),
                                        jsonObject.getString("usingTime"),
                                        jsonObject.getString("nowCapacity"),
                                        jsonObject.getString("averageDays"),
                                        jsonObject.getLong("capacityOfUserId"));

                                deviceNameEdit.setText(deviceDetailDTO.getDeviceName());
                                categoryTxt.setText(deviceDetailDTO.getCategory());
                                maxOutputTxt.setText(deviceDetailDTO.getMaximum_output().toString());
                                nowCapacityEdit.setText(deviceDetailDTO.getNowCapacity());
                                contentsTxt.setText(deviceDetailDTO.getContents());
                                usingTimeTxt.setText(deviceDetailDTO.getUsingTime());
                                averageDayEdit.setText(deviceDetailDTO.getAverageDays());
                                mAhEdit.setText(deviceDetailDTO.getmAh().toString());
                                capacityOfUserId.setText(deviceDetailDTO.getCapacityOfUserId().toString());

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


        // 수정 목록 반영
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                url.append("http://test.com02cloud.kro.kr/capacity/update");

                Long capacityId = Long.valueOf(capacityOfUserId.getText().toString());

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("userCapacityId", capacityId);
                    jsonBody.put("deviceName", deviceNameEdit.getText());
                    jsonBody.put("averageDays", Double.valueOf(averageDayEdit.getText().toString()));
                    jsonBody.put("nowCapacity", Double.valueOf(nowCapacityEdit.getText().toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new JsonObjectRequest(Request.Method.PUT, url.toString(), jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("success", response.toString());
                                try {
                                    boolean isSuccess = response.getBoolean("isSuccess");
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

                Toast.makeText(getApplicationContext(), "디바이스 수정 완료", Toast.LENGTH_LONG).show();

                //device 리스트 activity로 전환
                Intent intent = new Intent(DeviceDetailActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });

        // 내 기기에서 삭제
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                url.append("http://test.com02cloud.kro.kr/capacity/delete");

                Long capacityId = Long.valueOf(capacityOfUserId.getText().toString());

                // json request Body 생성
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("userCapacityId", capacityId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new JsonObjectRequest(Request.Method.PUT, url.toString(), jsonBody,
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

                Toast.makeText(getApplicationContext(), "디바이스 삭제 완료", Toast.LENGTH_LONG).show();

                //device 리스트 activity로 전환
                Intent intent = new Intent(DeviceDetailActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });
    }
}