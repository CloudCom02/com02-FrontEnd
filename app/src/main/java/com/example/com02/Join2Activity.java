package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Join2Activity extends AppCompatActivity {
    private EditText editText_authcode;
    private TextView text_message;
    private Button btn_check;
    private Button btn_next;
    private ImageView img_privousArraw;
    private StringBuilder url;
    private SharedPreferences preferences;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        editText_authcode = findViewById(R.id.editText_authcode);
        btn_check = findViewById(R.id.btn_check);
        text_message = findViewById(R.id.text_message);
        text_message.setVisibility(View.VISIBLE);
        text_message.setTextColor(getResources().getColor(R.color.red));
        text_message.setText("일치하지 않는 인증코드입니다");
        btn_next = findViewById(R.id.btn_next);
        btn_next.setClickable(false);
        img_privousArraw = findViewById(R.id.img_privousArraw);

        queue = Volley.newRequestQueue(this);

        // 인증코드 확인 버튼
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                JSONObject jsonRequest = new JSONObject();

                preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String email = preferences.getString("email", null);
                try {
                    url.append("http://10.0.2.2:8080/user/emails/verifications");
                    jsonRequest.put("email", email);
                    jsonRequest.put("code", editText_authcode.getText().toString());
                    Log.d("asdf", "Join2Activity : 입력한 인증코드 - " + editText_authcode.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("asdf", "Join2Activity : 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                boolean isCorrected = result.getBoolean("isCorrected");

                                // 이메일 & 인증코드 둘 다 맞는 경우
                                if (isCorrected) {
                                    text_message.setTextColor(getResources().getColor(R.color.green));
                                    text_message.setText("인증이 완료되었습니다");
                                    btn_next.setClickable(true);
                                }
                            } else {
                                btn_next.setClickable(false);
                                Log.d("asdf", "이메일 & 인증코드 맞지 않음");
                                Toast.makeText(Join2Activity.this, "인증코드가 맞지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Join2Activity.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asdf", "에러뜸!!" + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorResponse = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(errorResponse);
                                String errorMessage = jsonObject.getString("errorMessage");
                                // Handle BaseException
                                Log.d("asdf", "BaseException: " + errorMessage);
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                request.setShouldCache(false); //이전 결과가 있어도 새로 요청하여 응답을 보여준다.
                request.setRetryPolicy(new DefaultRetryPolicy(100000000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        img_privousArraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Join1Activity.class);
                startActivity(intent);
            }
        });
    }
}