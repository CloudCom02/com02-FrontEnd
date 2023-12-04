package com.example.com02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DetailInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info_ok); // 여기서 your_layout은 현재 화면의 XML 파일 이름입니다.
        //어디에나 있는 prev 버튼
        ImageButton backButton = findViewById(R.id.prevViewButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 페이지로 이동하는 코드
                Intent intent = new Intent(DetailInfoActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }
}