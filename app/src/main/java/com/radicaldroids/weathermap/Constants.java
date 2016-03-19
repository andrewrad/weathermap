package com.radicaldroids.weathermap;

/**
 * Created by Andrew on 3/18/2016.
 */
public class Constants {
    public static final String BASE_API_URL="http://api.openweathermap.org/data/2.5/forecast/daily";
    public static final String API_KEY="appid";
    public static final String API_KEY_STRING="4563410a615ddb7bb94f780b91ea0329";
    public static final String LAT="lat";
    public static final String LON="lon";
    public static final String UNITS="units";
    public static final String IMPERIAL="imperial";
    public static final String MODE="mode";
    public static final String JSON="json";
    public static final String COUNT="cnt";
    public static final String DAYS_FORECASTED="14";

    public static final String AUTHORITY="com.radicaldroids.weatherapp";
    public static final String CONTENT_AUTHORITY="content://com.radicaldroids.weatherapp";
    public static final String TABLE_NAME="forecast_data";

    public static final String SQL_ID="_id";
    public static final String DATETIME="date_time";
    public static final String LOW_TEMP="low_temp";
    public static final String HIGH_TEMP="high_temp";
    public static final String DAY_TEMP="day_temp";
    public static final String NIGHT_TEMP="night_temp";
    public static final String EVE_TEMP="eve_temp";
    public static final String MORN_TEMP="morn_temp";
    public static final String PRESSURE="pressure";
    public static final String HUMIDITY="humidity";
    public static final String MAIN="main";
    public static final String DESCRIPTION="description";
    public static final String ICON="icon";
    public static final String SPEED="speed";
    public static final String DEGREES="deg";
    public static final String CLOUDS="clouds";
    public static final String RAIN="rain";

    public static final Integer UPDATE_INTERVAL=1800000;

    public static final Integer PULL_REQUEST=1;
    public static final Integer ALARM_REQUEST=2;

    public static final String ID="id";

    public static final String PREFERENCES= "prefs";
    public static final String LOCATION_TYPE= "loc_type";
    public static final String LOCATION_GPS= "loc_gps";
    public static final String LOCATION_MANUAL= "loc_manual";
    public static final String STORED_LOCATION= "stored_location";
    public static final String STORED_LAT= "stored_lat";
    public static final String STORED_LON= "stored_lon";
}
