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

public class Join1Activity extends AppCompatActivity {
    private EditText editText_email;
    private TextView text_message;
    private Button btn_dupCheck;
    private Button btn_next;
    private ImageView img_privousArraw;
    private StringBuilder url;
    private SharedPreferences preferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);

        editText_email = findViewById(R.id.editText_email);
        btn_dupCheck = findViewById(R.id.btn_dupCheck);
        text_message = findViewById(R.id.text_message);
        text_message.setVisibility(View.INVISIBLE);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setClickable(false);
        img_privousArraw = findViewById(R.id.img_privousArraw);

        queue = Volley.newRequestQueue(this);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        // 중복 검증 버튼
        btn_dupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();

                try {
                    url.append("http://test.com02cloud.kro.kr/user/check-email").append("?email=").append(editText_email.getText().toString());
                    Log.d("asdf", "Join1Activity : 입력한 이메일 - " + editText_email.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success", response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                boolean emailExists = result.getBoolean("emailExists");

                                // 이메일 중복 여부에 따라 처리
                                if (emailExists) {
                                    // 이메일이 중복되면
                                    text_message.setTextColor(getResources().getColor(R.color.red));
                                    text_message.setText("중복된 이메일입니다");
                                    btn_next.setClickable(false);
                                } else {
                                    // 이메일이 중복되지 않으면
                                    text_message.setTextColor(getResources().getColor(R.color.green));
                                    text_message.setText("사용 가능한 이메일입니다");
                                    btn_next.setClickable(true);
                                }
                                text_message.setVisibility(View.VISIBLE);
                            } else {
                                btn_next.setClickable(false);
                                // 요청 실패
                                Toast.makeText(Join1Activity.this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Join1Activity.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
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

        // 다음 버튼 (이메일 전송도 같이 진행)
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Join2Activity.class);
                startActivity(intent);

                url = new StringBuilder();
                url.append("http://test.com02cloud.kro.kr/user/emails/verification-requests");

                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", editText_email.getText().toString());
                    jsonRequest.put("isForJoin", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Editor를 preferences에 쓰겠다고 연결
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", editText_email.getText().toString());
                editor.commit();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("asdf", "이메일 전송 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                String correctCode = result.getString("code");
                                editor.putString("correctCode", correctCode);
                                editor.commit();
                            } else {
                                Toast.makeText(Join1Activity.this, "메일 전송 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Join1Activity.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
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

                //항상 commit & apply 를 해주어야 저장이 된다.
                editor.commit();

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