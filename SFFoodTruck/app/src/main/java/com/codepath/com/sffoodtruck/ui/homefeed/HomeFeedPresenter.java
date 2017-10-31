package com.codepath.com.sffoodtruck.ui.homefeed;

import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saip92 on 10/30/2017.
 */

class HomeFeedPresenter extends AbstractPresenter<HomeFeedContract.MvpView> implements
 HomeFeedContract.Presenter{


    private static final long LOC_INTERVAL = 3000;
    private static final long LOC_FAST_INTERVAL = 3000;
    private static final String FOODTRUCK = "foodtrucks";
    private static final String PARAM_LOCATION = "location";
    private static final String PARAM_CATEGORIES = "categories";
    private static final String PARAM_TERM = "term";
    private static final String PARAM_OFFSET = "offset";
    private SearchApi mSearchApi;


    public HomeFeedPresenter(){}

    public HomeFeedPresenter(SearchApi searchApi){
        mSearchApi = searchApi;
    }

    private static final String TAG = HomeFeedPresenter.class.getSimpleName();

    @Override
    public void initialLoad(String query) {
        loadFavorites();
        getView().initializeUI();
    }

    @Override
    public void setUpLocation() {
        Log.d(TAG,"Starting location updates and getting last known location");
        getView().getLastKnownLocation();
        getView().startLocationUpdates(getLocationRequest());
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOC_INTERVAL);
        locationRequest.setFastestInterval(LOC_FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    @Override
    public void stopLocationUpdates() {
        getView().stopLocationUpdates();
    }

    @Override
    public void loadFoodTruckFeed(String location) {
        Map<String,String> queryParams = new HashMap<>();
        if(location != null){
            Location loc = JsonUtils.fromJson(location,Location.class);
            queryParams.put("latitude",String.valueOf(loc.getLatitude()));
            queryParams.put("longitude",String.valueOf(loc.getLongitude()));
        }else{
            queryParams.put(PARAM_LOCATION,"San Jose, California");
        }
        //queryParams.put(PARAM_LOCATION,"95112");
        queryParams.put(PARAM_CATEGORIES,FOODTRUCK);
        //put page number
        Call<SearchResults> callResults = mSearchApi.getSearchResults(queryParams);
        callResults.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                SearchResults searchResults = response.body();
                if (searchResults == null || searchResults.getBusinesses() == null
                        || searchResults.getBusinesses().isEmpty() || getView() == null) {
                    Log.e(TAG, "response has failed " + response.code());
                    return;
                }

                List<Business> businesses = searchResults.getBusinesses();

                getView().addFoodTruckList(businesses);

            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG, "response has failed " ,t );
            }
        });
    }

    @Override
    public void loadFavorites() {
        DatabaseReference databaseReference =  FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(databaseReference != null){
            databaseReference
                    .orderByChild("timestamp")
                    .limitToLast(5)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedList<Business> businesses = new LinkedList<>();
                            for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                                Business business = businessSnapshot.getValue(Business.class);
                                if(business != null){
                                    Log.d(TAG,"Favorite's list :" + business.getName());
                                    businesses.addLast(business);
                                }
                            }
                            getView().addFavoritesFoodTruckList(businesses);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void updateLocation(String location) {

    }

}
