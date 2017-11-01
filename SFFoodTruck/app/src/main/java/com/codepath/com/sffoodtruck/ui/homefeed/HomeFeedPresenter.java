package com.codepath.com.sffoodtruck.ui.homefeed;

import android.location.Location;
import android.util.Log;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Coordinates;
import com.codepath.com.sffoodtruck.data.model.CustomPlace;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saip92 on 10/30/2017.
 */

class HomeFeedPresenter extends AbstractPresenter<HomeFeedContract.MvpView> implements
 HomeFeedContract.Presenter{


    private static final long LOC_INTERVAL = 30000;
    private static final long LOC_FAST_INTERVAL = 30000;
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
            CustomPlace place = JsonUtils.fromJson(location,CustomPlace.class);
            if(loc != null && (loc.getLongitude() != 0.0 || loc.getLatitude() != 0.0)){
                queryParams.put("latitude",String.valueOf(loc.getLatitude()));
                queryParams.put("longitude",String.valueOf(loc.getLongitude()));
            }else if(place != null && (place.getLatitude() != 0.0 || place.getLongitude() != 0.0)){
                queryParams.put("latitude",String.valueOf(place.getLatitude()));
                queryParams.put("longitude",String.valueOf(place.getLongitude()));
            }else{
                queryParams.put(PARAM_LOCATION,"Menlo Park, California");
            }


        }else{
            queryParams.put(PARAM_LOCATION,"Menlo Park, California");
        }
        queryParams.put("limit","10");
        queryParams.put(PARAM_CATEGORIES,FOODTRUCK);
        //put page number
        Log.d(TAG,"Requesting server with: " + queryParams);
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
                getView().hideProgressBar();

            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG, "response has failed " ,t );
                getView().hideProgressBar();
            }
        });
    }

    @Override
    public void loadFavorites(String location) {
        DatabaseReference databaseReference =  FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(databaseReference != null){


            databaseReference
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() == 0) {
                                getView().hideFavorites();
                                return;
                            }
                            LinkedList<Business> businesses = new LinkedList<>();

                            Location loc = JsonUtils.fromJson(location,Location.class);
                            CustomPlace customPlace = JsonUtils.fromJson(location,CustomPlace.class);
                            Integer integer = Integer.valueOf(String.valueOf(dataSnapshot.getChildrenCount()));
                            Queue<Business> businessQueue = new PriorityQueue<>(integer, (one, two) -> {

                                if(loc != null ){

                                    boolean result = false;
                                   if(loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0){
                                        result = getDistance(loc,one.getCoordinates()) >
                                               getDistance(loc,two.getCoordinates());
                                   }else if(customPlace != null && (customPlace.getLatitude() != 0.0 && customPlace.getLongitude() != 0.0)){
                                       result = getDistance(customPlace,one.getCoordinates()) >
                                               getDistance(customPlace,two.getCoordinates());
                                   }else{
                                       return 0;
                                   }

                                    return result? 1 : -1;
                                }
                                return 0;
                            });

                            for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                                Business business = businessSnapshot.getValue(Business.class);
                                if(business != null){
                                    Log.d(TAG,"Favorite's list :" + business.getName());
                                    if(loc != null){
                                        Log.d(TAG,"Updating distance");
                                        if(loc.getLatitude() != 0.0 ){
                                            business.setDistance(getDistance(loc,
                                                    business.getCoordinates()));
                                        }else if(customPlace.getLatitude() != 0.0){
                                            business.setDistance(getDistance(customPlace,
                                                    business.getCoordinates()));
                                        }


                                    }

                                    businessQueue.add(business);
                                }
                            }

                            while(businesses.size() <= 5 && businessQueue.size() > 0 ){
                                businesses.addFirst(businessQueue.poll());
                            }

                            getView().addFavoritesFoodTruckList(businesses);
                            getView().hideProgressBar();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{
            getView().hideFavorites();
        }
    }

    private float getDistance(Location location, Coordinates coordinates){
        float[] results = new float[3];


        Location.distanceBetween(location.getLatitude(),
                location.getLongitude(),
                Double.valueOf(coordinates.getLatitude()),
                Double.valueOf(coordinates.getLongitude()),
                results);

        return results[0];
    }

    private float getDistance(CustomPlace location, Coordinates coordinates){
        float[] results = new float[3];


        Location.distanceBetween(location.getLatitude(),
                location.getLongitude(),
                Double.valueOf(coordinates.getLatitude()),
                Double.valueOf(coordinates.getLongitude()),
                results);

        return results[0];
    }

    @Override
    public void updateLocation(String location) {
        loadFoodTruckFeed(location);
        loadFavorites(location);
    }

}
