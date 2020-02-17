package com.weather.weatherapi.Network;

import com.weather.weatherapi.Model.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConnectorService {

    //1579ec14fe24d755677ee975151f1b19 (Api access key)

    public String BaseURL = "http://api.weatherstack.com/";

    @GET("current")
    Call<MainActivity> getWeather(@Query("access_key") String access_key,
                                             @Query("query") String city);
}
