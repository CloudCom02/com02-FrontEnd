package com.example.com02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class OneselfActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneself);
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

        // 배열로부터 아이템을 가져와서 초기화
//        String[] items1 = getResources().getStringArray(R.array.category_array);
//
//        Spinner spinner1 = findViewById(R.id.categorySpinner);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(adapter1);
//
//        // 배열로부터 아이템을 가져와서 초기화
//        String[] items2 = getResources().getStringArray(R.array.time_array);
//
//        Spinner spinner2 = findViewById(R.id.UseTimeSpinner);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items2);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(adapter2);
//
//        // 배열로부터 아이템을 가져와서 초기화
//        String[] items3 = getResources().getStringArray(R.array.time_array);
//
//
//        Spinner spinner3 = findViewById(R.id.AverageUseTimeSpinner);
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items3);
//        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner3.setAdapter(adapter3);


    }
}
