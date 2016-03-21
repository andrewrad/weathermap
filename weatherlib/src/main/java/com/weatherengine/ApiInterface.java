package com.weatherengine;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Andrew on 3/18/2016.
 */
public interface ApiInterface {
    //limiting forecast data to 1 day to help aleviate work-load on the openweathermap servers
    //openweathermap allows up to 14 days of forecast, I would expand this if I get time to work on UI elements
    @GET("?appid=4563410a615ddb7bb94f780b91ea0329&units=imperial&cnt=1")
    Call<com.weatherengine.model.Example> getCityData(@Query("lat") Double lat, @Query("lon") Double lon);
}
