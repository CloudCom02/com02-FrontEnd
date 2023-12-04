package com.example.network;

import com.example.dto.DeviceData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpTask {

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private List<DeviceData> responseList = new ArrayList<>();
    private final SearchService searchService;

    public HttpTask() {
        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // ApiService 구현
        searchService = retrofit.create(SearchService.class);
    }

    public Observable<List<DeviceData>> fetchData(String searchKeyword) {
        // RxJava를 사용하여 비동기 작업 수행
        return searchService.fetchData(searchKeyword)
                .subscribeOn(Schedulers.io())          // IO 스케줄러에서 작업 수행
                .observeOn(AndroidSchedulers.mainThread()) // 메인 스레드에서 결과 처리
                .doOnNext(list -> responseList.addAll(list));  // responseList에 값을 저장
    }
    public List<DeviceData> getResponseList() {
        return responseList;
    }
}
