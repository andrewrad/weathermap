package com.radicaldroids.weathermap;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Andrew on 3/18/2016.
 */
public interface ApiInterface {
    @GET("?appid=4563410a615ddb7bb94f780b91ea0329")
    Call<Example> getCityData(@Query("lat") Double lat, @Query("lon") Double lon);
}
