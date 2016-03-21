package com.weatherengine.model;

/**
 * Created by Andrew on 3/21/2016.
 */
public class WeatherData {
    private Long date;
    private Double highTemp;
    private Double lowTemp;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(Double highTemp) {
        this.highTemp = highTemp;
    }

    public Double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(Double lowTemp) {
        this.lowTemp = lowTemp;
    }
}
