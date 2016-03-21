package com.weatherengine;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class WeatherApi {
    Double latitude;
    Double longitude;
    public com.weatherengine.model.Example mExample;
    private Call<com.weatherengine.model.Example> mCall;

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
        mExample=new com.weatherengine.model.Example();
        ApiInterface apiInterface = RestClient.getClient();
        mCall = apiInterface.getCityData(latitude,longitude);
        mCall.enqueue(new Callback<com.weatherengine.model.Example>() {

            @Override
            public void onResponse(Response<com.weatherengine.model.Example> response) {
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
