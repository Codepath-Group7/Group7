package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.content.Context;
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

    @Override
    public void loadFoodTruckFeed(Context context) {
        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, context);

        Call<SearchResults> callResults = services.getSearchResults("95112");
        callResults.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                SearchResults searchResults = response.body();
                if (searchResults == null || searchResults.getBusinesses() == null
                        || searchResults.getBusinesses().isEmpty()) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                List<Business> businesses = searchResults.getBusinesses();
                getView().showFoodTruckList(businesses);
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {

            }
        });
    }

    public void onSaveClick(View view){
        Log.d(TAG,"Clicked on business object: ");
    }

    public boolean onLongClick(String text){
        Log.d(TAG,"Long cliked " + text);
        return true;
    }

    public void onItemClick(Business business){
        Log.d("TESTCLASS","Item has been clicked");
    }
}
