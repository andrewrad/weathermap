package com.radicaldroids.weathermap;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;

///**
// * Created by Andrew on 3/18/2016.
// */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String TAG="MapsActivity";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    @Bind(R.id.click_button)Button click;
    @Bind(R.id.progress_bar)ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "getCameraPosition: " + mMap.getCameraPosition().target.latitude + ", " + mMap.getCameraPosition().target.longitude);
            }
        });
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

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if(myLocation!=null) {
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }else{
            Toast.makeText(this,"To auto-detect your location, please turn on location services",Toast.LENGTH_LONG).show();
        }

//        mGoogleApiClient=new GoogleApiClient.Builder()

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.e(TAG, "getCameraPosition by longClicking: " + mMap.getCameraPosition().target.latitude + ", " + mMap.getCameraPosition().target.longitude + ", " + latLng.latitude + ", " + latLng.longitude);
//                mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("75 rain"));
                new FetchWeather().execute(mMap.getCameraPosition().target);
            }
        });
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

    private class FetchWeather extends AsyncTask {
        private String TAG ="FetchWeather";
        @Override
        protected Object doInBackground(Object[] params) {
            LatLng mLatLng=(LatLng) params[0];
            Log.e(TAG, "lat long: "+mLatLng.latitude+", "+mLatLng.longitude);
            publishProgress(null);
            try {
                //for development purposes only
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Log.e(TAG,"thread sleep exception");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG,"sleepage");
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBar.setVisibility(View.INVISIBLE);
            Log.e(TAG,"task done!");
        }
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
