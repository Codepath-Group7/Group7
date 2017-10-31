package com.codepath.com.sffoodtruck.ui.homefeed;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.databinding.FragmentHomeFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.util.BackgroundLocationService;
import com.codepath.com.sffoodtruck.ui.util.ItemClickSupport;
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
    private static final int RC_SHARE_DATA = 123;
    private static final String OPEN_BOTTOM_SHEET = "REQUEST_TO_OPEN_BOTTOM_SHEET";
    private FragmentHomeFeedBinding mHomeFeedBinding;
    private HomeFeedAdapter mFavoriteAdapter;
    private HomeFeedAdapter mTopStoriesAdapter;
    private static final String TAG = HomeFeedFragment.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation = null;
    private String mLocationAddress;

    private onGroupShareListener mOnGroupShareListener;

    public HomeFeedFragment() {
    }

    public interface onGroupShareListener{
        void onGroupShare(Business business);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mOnGroupShareListener = (onGroupShareListener) activity;
        }catch (ClassCastException e){
            Log.e(TAG," Activity must Implement onGroupShareListener");
        }

    }

    @Override
    public void initializeUI() {
        mFavoriteAdapter = new HomeFeedAdapter(new ArrayList<>());
        mTopStoriesAdapter = new HomeFeedAdapter(new ArrayList<>());

        //initializing favorites recycler view
        mHomeFeedBinding.rvFavorites.setAdapter(mFavoriteAdapter);
        mHomeFeedBinding.rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mHomeFeedBinding.rvFavorites.addItemDecoration(new LinePagerIndicatorDecoration());
        ItemClickSupport.addTo(mHomeFeedBinding.rvFavorites)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    openBusinessDetail(mFavoriteAdapter,position,v);
                });

        //initializing top stories recycler view
        mHomeFeedBinding.rvHomeFeed.setAdapter(mTopStoriesAdapter);
        mHomeFeedBinding.rvHomeFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemClickSupport.addTo(mHomeFeedBinding.rvHomeFeed)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    openBusinessDetail(mTopStoriesAdapter,position,v);
                });
    }

    private void openBusinessDetail(HomeFeedAdapter homeFeedAdapter, int position, View v){
        Intent intent = BusinessDetailActivity
                .newIntent(getActivity(),homeFeedAdapter.getBusinessForPos(position));

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            BottomSheetDialogFragment shareBottomSheet =
                    ShareBottomSheet.newInstance(homeFeedAdapter.getBusinessForPos(position));
            shareBottomSheet.setTargetFragment(HomeFeedFragment.this,RC_SHARE_DATA);
            shareBottomSheet.show(getChildFragmentManager(),OPEN_BOTTOM_SHEET);
        });

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            ImageView ivBanner = (ImageView) v.findViewById(R.id.ivBanner);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), ivBanner, "businessImage");

            startActivity(intent,options.toBundle());
        } else {
            // Implement this feature without material design
            startActivity(intent);
        }
    }

    @Override
    public void addFoodTruckList(List<Business> businessList) {
        mTopStoriesAdapter.addAll(businessList);
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
                if(mTopStoriesAdapter.getItemCount() == 0){
                   getPresenter().updateLocation(QueryPreferences.getLocationPref(getActivity()));
                }
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
                getPresenter().updateLocation(JsonUtils.toJson(currentLocation));
                Log.d(TAG,"Updating the stored location with: "
                        + JsonUtils.toJson(currentLocation));
            }else{
                if(mTopStoriesAdapter.getItemCount() <= 0){
                    getPresenter().updateLocation(JsonUtils.toJson(currentLocation));
                }
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
           // getActivity().startService(new Intent(getActivity(), BackgroundLocationService.class));
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
        SearchApi services = RetrofitClient
                .createService(SearchApi.class,getActivity());
        return new HomeFeedPresenter(services);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == RC_SHARE_DATA && data != null){
            Business business = data.getParcelableExtra(ShareBottomSheet.EXTRA_BUSINESS);
            mOnGroupShareListener.onGroupShare(business);
        }

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
