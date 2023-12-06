package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MyPageActivity extends AppCompatActivity {

    Button logout;
    RequestQueue queue;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "com02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        logout=findViewById(R.id.logout);
        queue= Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long userIdx=sharedPreferences.getLong("userIdx",-1);

                if (userIdx != -1) { // 저장된 userIdx가 있다면
                    Log.d("userIdx", String.valueOf(userIdx));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("userIdx");
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else { // 저장된 userIdx가 없을 경우
                    Log.d("userIdx", "No userIdx found");
                }
            }
        });
    }
}