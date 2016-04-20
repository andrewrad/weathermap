package com.weatherapi;

import android.content.Intent;

import com.model.Example;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class WeatherApi{
    Double latitude;
    Double longitude;
    public Example mExample;
    private Call<Example> mCall;

    public WeatherApi(){
    }

    public WeatherApi(Double latitude, Double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * Retrofit 2.0 Asynchronous implementation
     * Performs the api call to openweathermap.org and parses all data into a pojo class called Example
     * @param latitude
     * @param longitude
     */
    public void getWeather(Double latitude, Double longitude){
        mExample=new Example();
        ApiInterface apiInterface = RestClient.getClient();
        mCall = apiInterface.getCityData(latitude,longitude);
//        mCall.enqueue(this);
        mCall.enqueue(new Callback<Example>() {

            @Override
            public void onResponse(Response<Example> response) {
//                Log.e(TAG, "Retrofit onResponse");
                if (response.isSuccess()) {
//                    Log.e(TAG, "successful onResponse Callback!!! "+response.body());
                    mExample=response.body();


                }else{
//                    Log.e(TAG, "not successful onResponse Callback");
                }
            }

            @Override
            public void onFailure(Throwable t) {
//                Log.e(TAG, "Retrofit onFailure");
            }
        });
    }
}
