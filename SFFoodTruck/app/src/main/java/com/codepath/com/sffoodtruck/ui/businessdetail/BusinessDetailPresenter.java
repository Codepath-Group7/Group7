package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.map.FoodTruckMapViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/14/17.
 */

public class BusinessDetailPresenter extends AbstractPresenter<BusinessDetailContract.MvpView> implements BusinessDetailContract.Presenter {

    public static final String TAG = BusinessDetailPresenter.class.getSimpleName();
    @Override
    public void loadBusiness(Context context, String businessId) {

        getFoodTruckDetails(context,businessId);
    }


    private void getFoodTruckDetails(Context context, String businessId){

        if (TextUtils.isEmpty(businessId)) return;

        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, context);
        Call<Business> callResults = services.getBusiness(businessId);
        callResults.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call,
                                   Response<Business> response) {
                Business businessDetails = response.body();
                if (businessDetails == null) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                getView().renderBusiness(businessDetails);
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });

    }
}
