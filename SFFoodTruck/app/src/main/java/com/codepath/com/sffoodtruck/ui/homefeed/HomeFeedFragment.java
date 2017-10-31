package com.codepath.com.sffoodtruck.ui.homefeed;


import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentHomeFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.codepath.com.sffoodtruck.ui.util.LinePagerIndicatorDecoration;
import com.codepath.com.sffoodtruck.ui.util.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedFragment extends
        AbstractMvpFragment<HomeFeedContract.MvpView, HomeFeedContract.Presenter>
        implements HomeFeedContract.MvpView,LocationListener {

    private static final int RC_LOCATION = 57;
    private FragmentHomeFeedBinding mHomeFeedBinding;
    private HomeFeedAdapter mFavoriteAdapter;
    private static final String TAG = HomeFeedFragment.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation = null;
    private String mLocationAddress;

    public HomeFeedFragment() {
    }

    @Override
    public void initializeUI() {
        mHomeFeedBinding.rvFavorites.setAdapter(mFavoriteAdapter);
        mHomeFeedBinding.rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mHomeFeedBinding.rvFavorites.addItemDecoration(new LinePagerIndicatorDecoration());
    }

    @Override
    public void appendFoodTruckList(List<Business> businessList) {

    }

    @Override
    public void addInitialFoodTruckList(List<Business> businessList) {

    }

    @Override
    public void addFavoritesFoodTruckList(List<Business> businessList) {
        mFavoriteAdapter.addAll(businessList);
    }

    @AfterPermissionGranted(RC_LOCATION)
    @Override
    public void getLastKnownLocation() {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            Log.d(TAG,"Calling get location service");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                Log.d(TAG,"latitude: " + mLastLocation.getLatitude() + ", Longitude: "
                        +mLastLocation.getLongitude());
                mLocationAddress = MapUtils.findLocation(getActivity(),mLastLocation);
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


    private void storeCurrentLocation(Location currentLocation){
        String storedLastLocation = QueryPreferences.getCurrentLocation(getActivity());
        Log.d(TAG,"Stored last Location: " + storedLastLocation);
        if(storedLastLocation == null){
            QueryPreferences.storeCurrentLocation(getActivity(), JsonUtils.toJson(currentLocation));
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
    @Override
    public void startLocationUpdates(LocationRequest locationRequest) {
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(EasyPermissions.hasPermissions(getActivity(),perms)) {
            Log.d(TAG,"Requesting location updates");
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, locationRequest, this)
                    .setResultCallback(status -> Log.d(TAG,"Status of requesting location update is: "
                            + status.getStatusMessage()));
        }else{
            Log.d(TAG,"Requesting for permissions: ");
            EasyPermissions.requestPermissions(getActivity(),
                    getString(R.string.location_rationale),RC_LOCATION,perms);
        }
    }

    @Override
    public void stopLocationUpdates() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public HomeFeedContract.Presenter createPresenter() {
        return new HomeFeedPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteAdapter = new HomeFeedAdapter(new ArrayList<>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHomeFeedBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_feed,container,false);
        return mHomeFeedBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initialLoad(null);
    }

    @Override
    public void onResume() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) getPresenter().setUpLocation();
        super.onResume();
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient){
        Log.d(TAG,"GoogleApiClient method has been called");
        mGoogleApiClient = googleApiClient;
        if(mGoogleApiClient != null){
            Log.d(TAG,"Calling getLastKnownLocation");
            getPresenter().setUpLocation();
        }
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient != null){
          getPresenter().stopLocationUpdates();
        }
        super.onStop();
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
}
