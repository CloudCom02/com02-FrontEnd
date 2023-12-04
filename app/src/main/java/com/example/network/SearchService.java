package com.example.network;

import com.example.dto.DeviceData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService extends HttpService {
    @GET("device/search")
    Observable<List<DeviceData>> fetchData(@Query("searchKeyword") String searchKeyword);
}