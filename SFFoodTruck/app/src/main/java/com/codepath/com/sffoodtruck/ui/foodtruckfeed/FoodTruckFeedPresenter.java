package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;

import java.util.List;

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

    public FoodTruckFeedPresenter(String authToken){
        Log.d(TAG,"This is the generated token"  + authToken);
        this.authToken = authToken;
    }

    @Override
    public void initialLoad() {
        loadFoodTruckFeed(0);
    }

    @Override
    public void loadFoodTruckFeed(int page) {
        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, authToken);

        Call<SearchResults> callResults = services.getSearchResults("95112" ,FOODTRUCK, page);
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
