package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.text.TextUtils;
import android.location.Location;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saip92 on 10/15/2017.
 */

public class FoodTruckFeedPresenter extends AbstractPresenter<FoodTruckFeedContract.MvpView>
implements FoodTruckFeedContract.Presenter{


    private static final String TAG = FoodTruckFeedPresenter.class.getSimpleName();
    private static final String FOODTRUCK = "foodtrucks";
    private final String authToken;
    private static final String PARAM_LOCATION = "location";
    private static final String PARAM_CATEGORIES = "categories";
    private static final String PARAM_TERM = "term";
    private static final String PARAM_OFFSET = "offset";
    private static boolean sInitialLoad = false;

    public FoodTruckFeedPresenter(String authToken){
        Log.d(TAG,"This is the generated token"  + authToken);
        this.authToken = authToken;
    }
    
    public void initialLoad(String query) { //add query string
        sInitialLoad = true;
        loadFoodTruckFeed(null,query,0);
    }


    @Override
    public void loadFoodTruckFeed(String location, String query, int page) {
        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, authToken);
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
        queryParams.put(PARAM_OFFSET,String.valueOf(page*20));
        if(query!=null && !TextUtils.isEmpty(query)) queryParams.put(PARAM_TERM,query);
        Call<SearchResults> callResults = services.getSearchResults(queryParams);
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
                if(sInitialLoad){
                    getView().loadInitialFoodTruckList(businesses);
                }else{
                    getView().appendFoodTruckList(businesses);
                }
                sInitialLoad = false;

            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG, "response has failed " ,t );
            }
        });
    }

    @Override
    public void updateLocation(String location, String query) {
        sInitialLoad = true;
        loadFoodTruckFeed(location,query,0);
    }


}
