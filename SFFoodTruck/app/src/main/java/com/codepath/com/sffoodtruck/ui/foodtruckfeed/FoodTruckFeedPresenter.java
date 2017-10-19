package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;

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


    public FoodTruckFeedPresenter(String authToken){
        Log.d(TAG,"This is the generated token"  + authToken);
        this.authToken = authToken;
    }

    @Override
    public void loadFoodTruckFeed(String query) {
        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, authToken);

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put(PARAM_LOCATION,"95112");
        queryParams.put(PARAM_CATEGORIES,FOODTRUCK);
        if(query!=null && !TextUtils.isEmpty(query)) queryParams.put(PARAM_TERM,query);
        Call<SearchResults> callResults = services.getSearchResults(queryParams);
        callResults.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                SearchResults searchResults = response.body();
                if (searchResults == null || searchResults.getBusinesses() == null
                        || searchResults.getBusinesses().isEmpty()) {
                    Log.e(TAG, "response has failed " + response.code());
                    return;
                }

                List<Business> businesses = searchResults.getBusinesses();
                getView().showFoodTruckList(businesses);
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG, "response has failed " ,t );
            }
        });
    }

}
