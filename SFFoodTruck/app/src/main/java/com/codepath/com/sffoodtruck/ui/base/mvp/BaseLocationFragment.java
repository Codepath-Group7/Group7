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
import android.widget.Toast;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedContract;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.codepath.com.sffoodtruck.ui.util.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.Query;

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
    private static final int LOC_INTERVAL = 30000;
    private static final int LOC_FAST_INTERVAL = 30000;
    private LocationRequest mLocationRequest = new LocationRequest();
    private static boolean mRequestingLocationUpdates= true;


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
        getLastKnownLocation();
        /*if(mRequestingLocationUpdates){
            startLocationUpdates();
        }*/
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void startLocationUpdates() {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(mGoogleApiClient.isConnected() && EasyPermissions.hasPermissions(getActivity(),perms)) {
            Log.d(TAG,"Requesting location updates");
            createLocationRequest();
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d(TAG,"Status of requesting location update is: "
                            + status.getStatusMessage());
                }
            });
        }else{
            Log.d(TAG,"Requesting for permissions: ");
            EasyPermissions.requestPermissions(getActivity(),
                    getString(R.string.location_rationale),RC_LOCATION,perms);
        }
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
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
            Log.d(TAG,"Calling location updates in onResume");
            startLocationUpdates();
        }else{
            Log.d(TAG,"Google Api client: " +mGoogleApiClient.isConnected() +
            "mRequestLocationUpdate: " + mRequestingLocationUpdates);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"On Location changed called");
        mLastLocation = location;
        if(mLastLocation != null){
            storeCurrentLocation(mLastLocation);
            Log.d(TAG,"latitude: " + mLastLocation.getLatitude() + ", Longitude: "
                    +mLastLocation.getLongitude());
            mLocationAddress = MapUtils.findLocation(getActivity(),mLastLocation);
        }else{
            Log.d(TAG,"On Location received got null");
        }
    }

    private void storeCurrentLocation(Location currentLocation){
        String storedLastLocation = QueryPreferences.getCurrentLocation(getActivity());
        Log.d(TAG,"Stored last Location: " + storedLastLocation);
        if(storedLastLocation == null){
            QueryPreferences.storeCurrentLocation(getActivity(),JsonUtils.toJson(currentLocation));
        }else{
            Location lastLoc = JsonUtils.fromJson(storedLastLocation,Location.class);
            Log.d(TAG,"Stored location: lat: " + lastLoc.getLatitude() + " , long: "
                    +lastLoc.getLongitude());
            float distanceBetween = lastLoc.distanceTo(currentLocation);
            Log.d(TAG,"Distance between: " + distanceBetween);
            //Toast.makeText(getCAc)
            if(distanceBetween >= 100){
                QueryPreferences.storeCurrentLocation(getActivity(),
                        JsonUtils.toJson(currentLocation));
                Log.d(TAG,"Updating the stored location with: "
                        + JsonUtils.toJson(currentLocation));
            }else{
                Log.d(TAG,"current location and stored location are less than 150 meters apart");
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void getLastKnownLocation() {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(getActivity(),perms)){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                Log.d(TAG,"latitude: " + mLastLocation.getLatitude() + ", Longitude: "
                        +mLastLocation.getLongitude());
                mLocationAddress = MapUtils.findLocation(getActivity(),mLastLocation);
                /*QueryPreferences.storeCurrentLocation(getActivity(),JsonUtils.toJson(mLastLocation));*/
                storeCurrentLocation(mLastLocation);
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
    /*protected void checkLocationSettings(){
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
    }*/

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

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOC_INTERVAL);
        mLocationRequest.setFastestInterval(LOC_FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }




}
