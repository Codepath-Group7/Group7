package com.codepath.com.sffoodtruck.ui.base.mvp;

import android.Manifest;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by saip92 on 10/18/2017.
 */

public abstract class BaseLocationFragment extends AbstractMvpFragment<FoodTruckFeedContract.MvpView,
        FoodTruckFeedContract.Presenter> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, EasyPermissions.PermissionCallbacks,
        LocationListener{


    private static final int REQUEST_CHECK_SETTINGS = 45;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLocationAddress;
    private static final String TAG = BaseLocationFragment.class.getSimpleName();
    private static final int RC_LOCATION = 10;
    private static final int LOC_INTERVAL = 10000;
    private static final int LOC_FAST_INTERVAL = 5000;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates= true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected to Google API Client");
        //getLastKnownLocation();
        if(mRequestingLocationUpdates){
            startLocationUpdates();
        }
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void startLocationUpdates() {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(mGoogleApiClient.isConnected() && EasyPermissions.hasPermissions(getActivity(),perms)) {
            checkLocationSettings();
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            Log.d(TAG,"Requesting for permissions: ");
            EasyPermissions.requestPermissions(getActivity(),
                    getString(R.string.location_rationale),RC_LOCATION,perms);
        }
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if(mLastLocation != null){
            Log.d(TAG,"latitude: " + mLastLocation.getLatitude() + ", Longitude: "
                    +mLastLocation.getLongitude());
            mLocationAddress = findLocation(mLastLocation);
        }
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void getLastKnownLocation() {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(getActivity(),perms)){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            checkLocationSettings();
            if(mLastLocation != null){
                Log.d(TAG,"latitude: " + mLastLocation.getLatitude() + ", Longitude: "
                        +mLastLocation.getLongitude());
                mLocationAddress = findLocation(mLastLocation);
            }else{
                Log.d(TAG,"Last known location is null");
            }
        }else{
            Log.d(TAG,"Requesting for permissions: ");
            EasyPermissions.requestPermissions(getActivity(),
                    getString(R.string.location_rationale),RC_LOCATION,perms);
        }
    }


    //Checks for the current location settings,
    // and prompts the user to ask for missing permissions if any
    protected void checkLocationSettings(){
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    Log.d(TAG,"All Location settings are granted ");
                   // startLocationUpdates();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                                getActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way
                    // to fix the settings so we won't show the dialog.

                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"Connection to google API Client suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Connection to google api client failed: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode == RC_LOCATION){
            Log.d(TAG,"Permission has been granted, hence calling getLastKnownLocation Again");
            getLastKnownLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    private String findLocation( Location location) {
        if (location == null) return null;

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // lat,lng, your current location
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.isEmpty()) return null;

            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOC_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }




}
