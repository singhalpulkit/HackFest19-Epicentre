/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pulkit.epicentre;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MyLocationDemoActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    //public Marker marker = new Marker();
    public HashMap<Marker,String> hashmap = new HashMap<>();

    String queryCreation(VisibleRegion boundary){
        return "minLat="+boundary.latLngBounds.southwest.latitude+"&maxLat="+boundary.latLngBounds.northeast.latitude
                +"&minLag="+boundary.latLngBounds.southwest.longitude+"&maxLag="+boundary.latLngBounds.northeast.longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Drawing circle on the map
       // drawCircle(new LatLng(23.78, 86.41));
    }

//    private void drawCircle(LatLng point){
//
//        // Instantiating CircleOptions to draw a circle around the marker
//        CircleOptions circleOptions = new CircleOptions();
//
//        // Specifying the center of the circle
//        circleOptions.center(point);
//
//        // Radius of the circle
//        circleOptions.radius(50);
//
//        // Border color of the circle
//        circleOptions.strokeColor(Color.BLUE);
//
//        // Fill color of the circle
//        circleOptions.fillColor(0x30ff0000);
//
//        // Border width of the circle
//        circleOptions.strokeWidth(2);
//
//        // Adding the circle to the GoogleMap
//        mMap.addCircle(circleOptions);
//
//    }



    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        enableMyLocation();


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                         @Override
                                         public void onCameraIdle() {
//                double CameraLat = mMap.getCameraPosition().target.latitude;
//                double CameraLong = mMap.getCameraPosition().target.longitude;
//                double zoomlevel = mMap.getCameraPosition().zoom;

                 final VisibleRegion bounds = mMap.getProjection().getVisibleRegion();

                 AsyncTask asyncTask = new AsyncTask() {
                     @Override
                     protected Object doInBackground(Object[] objects) {
                         OkHttpClient client = new OkHttpClient();

                         Request request = new Request.Builder()
                                 .url("http://192.168.137.1:8080/fetchdata/areadata?" + queryCreation(bounds))
                                 .build();

                         Response response = null;

                         try {

                             response = client.newCall(request).execute();

                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         return null;
                     }
                 }.execute();
             }
         }
        );

        //iit dhanbad
        plot(23.825210,86.440390, 2000*0.11729288002019206,0x55ff0000,"Swine Flu", "https://www.cdc.gov/h1n1flu/general_info.htm");
        plot(23.815210,86.430390, 2500*0.12674208338559803,0x55ffff00,"Malaria", "https://www.cdc.gov/malaria/prevent_control.html");

        //IGI
        plot(28.546324, 77.096304, 2100*0.11729288002019206,0x55808000,"Dengue", "https://www.cdc.gov/features/avoid-dengue/index.html");
        plot(28.566324, 77.079304, 1900*0.11729288002019206,0x55B22222,"Ebola", "https://www.cdc.gov/vhf/ebola/prevention/index.html");
        plot(28.568324, 77.089304, 2300*0.11729288002019206,0x55ff0000,"Swine Flu", "https://www.cdc.gov/h1n1flu/general_info.htm");
        plot(28.588324, 77.090304, 2800*0.11729288002019206,0x55ffff00,"Malaria", "https://www.cdc.gov/malaria/prevent_control.html");





//        plot(33,84, 50,0x55ff0000,"Test");
//        plot(27,84, 50,0x55ff0000,"Test");
//        plot(24,84, 50,0x55ff0000,"Test");
//        plot(42,84, 50,0x55ff0000,"Test");
//        plot(63,84, 50,0x55ff0000,"Test");
    }


    void plot(double lat,double lng, double rad, int clr_hex, String title,String url){

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(rad)
                .strokeColor(Color.BLACK)
                .strokeWidth(1)
                .fillColor(clr_hex));
       // mMap.addMarker(new MarkerOptions().title(title).position(new LatLng(lat,lng))).showInfoWindow();
        Marker marker = mMap.addMarker(new MarkerOptions().title(title).position(new LatLng(lat,lng)));
        hashmap.put(marker,url);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {

        if (marker.equals(marker))
        {
            Uri uriUrl = Uri.parse(hashmap.get(marker));
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }




}