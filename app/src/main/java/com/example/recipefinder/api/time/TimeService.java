package com.example.recipefinder.api.time;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TimeService {
    @GET("timezone/Etc/UTC")
    Call<TimeResponse> getCurrentTime();
}

