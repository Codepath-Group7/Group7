package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.content.Context;
import android.location.Location;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/15/2017.
 */

public interface FoodTruckFeedContract {

    interface MvpView extends MvpBaseView{
        void appendFoodTruckList(List<Business> businessList);
        void loadInitialFoodTruckList(List<Business> businessList);
    }


    interface Presenter extends MvpBasePresenter<MvpView>{
        void initialLoad(String query);
        void loadFoodTruckFeed(String location, String query, int page);
        void updateLocation(String location, String query);
    }

}
