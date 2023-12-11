package com.example.com02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Join3Activity extends AppCompatActivity {

    private EditText editText_password1;
    private EditText editText_password2;
    private TextView text_message;
    private Button btn_next;
    private ImageView img_privousArraw;
    private StringBuilder url;
    private SharedPreferences preferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join3);

        editText_password1 = findViewById(R.id.editText_password1);
        editText_password2 = findViewById(R.id.editText_password2);
        text_message = findViewById(R.id.text_message);
        text_message.setVisibility(View.VISIBLE);
        text_message.setTextColor(getResources().getColor(R.color.red));
        text_message.setText("비밀번호가 일치하지 않습니다");
        btn_next = findViewById(R.id.btn_next);
        btn_next.setClickable(false);
        img_privousArraw = findViewById(R.id.img_privousArraw);

        queue = Volley.newRequestQueue(this);

        editText_password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editText_password1.getText().toString().equals("") && editText_password1.getText().toString().equals(editText_password2.getText().toString())) {
                    text_message.setTextColor(getResources().getColor(R.color.green));
                    text_message.setText("비밀번호가 일치합니다");
                    btn_next.setClickable(true);
                } else {
                    text_message.setTextColor(getResources().getColor(R.color.red));
                    text_message.setText("비밀번호가 일치하지 않습니다");
                    btn_next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editText_password1.getText().toString().equals("") && editText_password1.getText().toString().equals(editText_password2.getText().toString())) {
                    text_message.setTextColor(getResources().getColor(R.color.green));
                    text_message.setText("비밀번호가 일치합니다");
                    btn_next.setClickable(true);
                } else {
                    text_message.setTextColor(getResources().getColor(R.color.red));
                    text_message.setText("비밀번호가 일치하지 않습니다");
                    btn_next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                JSONObject jsonRequest = new JSONObject();

                preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String email = preferences.getString("email", null);
                try {
                    url.append("http://test.com02cloud.kro.kr/user/join");
                    jsonRequest.put("email", email);
                    jsonRequest.put("password", editText_password2.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("asdf", "Join3Activity : 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                Intent intent = new Intent(getApplicationContext(), Join4Activity.class);
                                startActivity(intent);
                            } else {
                                btn_next.setClickable(false);
                                Log.d("asdf", "회원가입 실패");
                                Toast.makeText(Join3Activity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Join3Activity.this, "JSON 파싱 오류입니다.", Toast.LENGTH_SHORT).show();
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

        img_privousArraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}