package com.codepath.com.sffoodtruck.ui.homefeed;


import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/28/2017.
 */

public class HomeFeedContract {


    interface View extends MvpBaseView{
        void appendFoodTruckList(List<Business> businessList);
        void loadInitialFoodTruckList(List<Business> businessList);
        void getLastKnownLocation();
        void startLocationUpdates();
        void stopLocationUpdates();
    }

    interface Presenter extends MvpBasePresenter<View> {
        void initialLoad(String query);
        void loadFoodTruckFeed(String location);
        void updateLocation(String location);
    }
}
