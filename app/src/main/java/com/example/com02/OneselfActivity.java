package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class OneselfActivity extends AppCompatActivity {

    EditText deviceName;
    EditText category;
    EditText usingTime;
    EditText averageDays;
    Button addDeviceBtn;
    private StringBuilder url;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "com02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneself);

        deviceName = (EditText) findViewById(R.id.productName);
        category = (EditText) findViewById(R.id.categoryTextView);
        usingTime = (EditText) findViewById(R.id.usingTime);
        averageDays = (EditText) findViewById(R.id.averageDays);
        addDeviceBtn = (Button) findViewById(R.id.registerBtn);

        // userId 얻기
        sharedPreferences = getSharedPreferences(mypreference,MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userIdx", -1);
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
                Intent intent = new Intent(OneselfActivity.this, DetailInfoActivity.class);
                startActivity(intent);
            }
        });


        url = new StringBuilder();
        RequestQueue queue = Volley.newRequestQueue(OneselfActivity.this);

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("진입접", "OnClick 요청 직전" + String.valueOf(userId));

                url.append("http://test.com02cloud.kro.kr/capacity/add");

                JSONObject jsonBody = new JSONObject();
                try {
                    Log.d("요청 후 userIdx", String.valueOf(userId));

                    jsonBody.put("userId", Long.valueOf(userId));
                    jsonBody.put("deviceName", deviceName.getText().toString());
                    jsonBody.put("category", category.getText().toString());
                    jsonBody.put("averageDays", Long.valueOf(averageDays.getText().toString()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
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

                Toast.makeText(getApplicationContext(), "디바이스 추가 완료", Toast.LENGTH_LONG).show();

                //device 리스트 activity로 전환
                Intent intent = new Intent(OneselfActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });
    }
}