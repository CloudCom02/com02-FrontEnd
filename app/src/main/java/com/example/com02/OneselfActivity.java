package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneself);

        deviceName = (EditText) findViewById(R.id.productName);
        category = (EditText) findViewById(R.id.categoryTextView);
        usingTime = (EditText) findViewById(R.id.usingTime);
        averageDays = (EditText) findViewById(R.id.averageDays);
        addDeviceBtn = (Button) findViewById(R.id.registerBtn);

        String userId = "2";

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
        RequestQueue queue = Volley.newRequestQueue(this);
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                url.append("http://10.0.2.2:8080/capacity/add");

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("userId", Long.valueOf(userId.toString()));
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
            }
        });
    }
}