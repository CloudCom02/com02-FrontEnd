package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login,join;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "com02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.editText_email);
        password=findViewById(R.id.editText_password);
        login=findViewById(R.id.btn_login); //로그인
        join=findViewById(R.id.btn_join);
        queue= Volley.newRequestQueue(this);
        sharedPreferences=getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String url="http://34.22.110.168:8001/user/login";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email.getText().toString());
                    jsonRequest.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success", response.toString());
                        JSONObject result = null;
                        long userIdx=0;
                        try {
                            result = response.getJSONObject("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(result!=null) {
                                userIdx = result.getLong("userIdx");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong("userIdx",userIdx);
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Log.e("test", "Login error: " + error.toString());
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    try {
                                        String errorResponse = new String(error.networkResponse.data, "utf-8");
                                        JSONObject jsonObject = new JSONObject(errorResponse);
                                        String errorMessage = jsonObject.getString("errorMessage");
                                        // Handle BaseException
                                        Log.e("test", "BaseException: " + errorMessage);
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                //이 부분에 추가
                request.setRetryPolicy(new DefaultRetryPolicy(
                        100000000,  // 기본 타임아웃 (기본값: 2500ms)
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                request.setShouldCache(false);
                queue.add(request);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Join1Activity.class);
                startActivity(intent);
            }
        });
    }
}