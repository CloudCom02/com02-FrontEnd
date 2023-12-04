package com.example.network;

import io.reactivex.Observable;

public interface HttpService {
    Observable<?> fetchData();
}
