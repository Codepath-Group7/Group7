package com.codepath.com.sffoodtruck.ui.base.mvp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.common.ActivityRequestCodeGenerator;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by saip92 on 10/18/2017.
 */

public abstract class BaseLocationFragment extends AbstractMvpFragment<FoodTruckFeedContract.MvpView,
        FoodTruckFeedContract.Presenter> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, EasyPermissions.PermissionCallbacks {


    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private static final String TAG = BaseLocationFragment.class.getSimpleName();
    private static final int RC_LOCATION = 10;


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
            }else{
                Log.d(TAG,"Last known location is null");
            }
        }else{
            Log.d(TAG,"Requesting for permissions: ");
            EasyPermissions.requestPermissions(getActivity(),
                    getString(R.string.location_rationale),RC_LOCATION,perms);
        }
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
}
