package com.example.com02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.SearchAdapter;
import com.example.dto.DeviceData;
import com.example.network.HttpTask;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private ImageButton searchButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_device);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //어디에나 있는 prev 버튼
        ImageButton backButton = findViewById(R.id.prevViewButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 페이지로 이동하는 코드
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, OneselfActivity.class);
                startActivity(intent);
            }
        });

        /*

            키워드를 받아 백엔드에 요청, 응답 받아 view 띄우기

        */

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        List<DeviceData> responseList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.deivce_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 열의 수를 지정
        recyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString();

                HttpTask httpTask = new HttpTask();
                httpTask.fetchData(searchQuery)
                        .subscribe(response -> {
                            responseList.clear();
                            responseList.addAll(response);
                        }, throwable -> {
                            Log.e("HttpTask", "Error: " + throwable.getMessage(), throwable);
                            // 오류 발생 시 처리
                        });

                SearchAdapter adapter = new SearchAdapter(responseList);
                recyclerView.setAdapter(adapter);

            }
        });
    }


}
