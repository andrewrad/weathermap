package com.radicaldroids.weathermap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.radicaldroids.weathermap.model.Example;
import com.radicaldroids.weathermap.network.ApiInterface;
import com.radicaldroids.weathermap.network.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

///**
// * Created by Andrew on 3/18/2016.
// */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG="MapsActivity";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    Example mExample;
    Call<Example> mCall;

    @Bind(R.id.progress_bar)ProgressBar mProgressBar;
    @Bind(R.id.city)TextView mTestBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequestHighAccuracy)
//                .addLocationRequest(mLocationRequestBalancedPowerAccuracy);

        //TODO address permission issue on newest platform
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        //TODO address permission issue on newest platform
        Location myLocation = null;
        try {
            myLocation = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        //For usability I leave the map type to default
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //if location services are turned on, zoom to current location, perform network call, and display results
        if (myLocation != null) {
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            getWeather(latLng.latitude, latLng.longitude);
        } else {
            Toast.makeText(this, "To auto-detect your location, please turn on location services", Toast.LENGTH_LONG).show();
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.e(TAG, "getCameraPosition by longClicking: " + mMap.getCameraPosition().target.latitude + ", " + mMap.getCameraPosition().target.longitude + ", " + latLng.latitude + ", " + latLng.longitude);
                getWeather(latLng.latitude, latLng.longitude);

                //Hoping to build a marker in the near future that will allow the user to mark certain locations that will automatically update every time they open the app (or on an interval)
//                mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("75 rain"));
//                new FetchWeather().execute(mMap.getCameraPosition().target);
            }
        });
    }

    /**
     * Retrofit 2.0 Asynchronous implementation
     * Performs the api call to openweathermap.org and parses all data into a pojo class
     * @param lat
     * @param lon
     */
    public void getWeather(Double lat, Double lon){
        mProgressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestClient.getClient();
        mCall = apiInterface.getCityData(lat,lon);
        mCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Retrofit onResponse");
                if (response.isSuccess()) {
                    Log.e(TAG, "successful onResponse Callback!!! "+response.body());
                    mExample=response.body();

                    //Temporary overlay design of writing text over the map, hoping to build a better UI soon
                    //Also hoping to clean up the com.model class hierarchy if enough time allows
                    Double highTemp=mExample.getList().get(0).getTemp().getMax();
                    Double lowTemp=mExample.getList().get(0).getTemp().getMin();
                    Integer clouds=mExample.getList().get(0).getClouds();
                    Double pressure=mExample.getList().get(0).getPressure();
                    Integer humidity=mExample.getList().get(0).getHumidity();
                    String description=mExample.getList().get(0).getWeather().get(0).getDescription();
                    Double windSpeed=mExample.getList().get(0).getSpeed();
                    Integer windDirection=mExample.getList().get(0).getDeg();

                    StringBuilder sB=new StringBuilder();
                    sB.append("<b>"+mExample.getCity().getName()+", ");
                    sB.append(mExample.getCity().getCountry()+"</b><br><br>");
                    sB.append(description+"<br>");
                    sB.append("High: "+Double.valueOf(highTemp)+"F"+"<br>");
                    sB.append("Low: "+Double.valueOf(lowTemp)+"F"+"<br>");
                    sB.append("Clouds: "+clouds+"%"+"<br>");
                    sB.append("Pressure: "+pressure+"<br>");
                    sB.append("Humidity: "+humidity+"<br>");
                    sB.append("Wind: "+windSpeed+" mph <br>");
                    sB.append("Wind direction: " + windDirection + " deg");

                    setWeatherText(sB);

                }else{
                    Log.e(TAG, "not successful onResponse Callback");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Retrofit onFailure");
            }
        });
    }
    public void setWeatherText(StringBuilder sb){
        mTestBox.setText(Html.fromHtml(sb.toString()));
//        Intent
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "not connected");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG,"Permissions: "+requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//            } else {
        // Permission was denied. Display an error message.
//            }
//        }
    }
}
