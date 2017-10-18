package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.content.Context;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/15/2017.
 */

public interface FoodTruckFeedContract {

    interface MvpView extends MvpBaseView{
        void showFoodTruckList(List<Business> businessList);
    }


    interface Presenter extends MvpBasePresenter<MvpView>{
        void initialLoad();
        void loadFoodTruckFeed(int page);
    }

}
