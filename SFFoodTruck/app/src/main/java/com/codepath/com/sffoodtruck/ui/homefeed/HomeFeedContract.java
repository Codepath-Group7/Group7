package com.codepath.com.sffoodtruck.ui.homefeed;


import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

/**
 * Created by saip92 on 10/28/2017.
 */

public class HomeFeedContract {


    interface MvpView extends MvpBaseView{
        void initializeUI();
        void addFoodTruckList(List<Business> businessList);
        void addFavoritesFoodTruckList(List<Business> businessList);
        void getLastKnownLocation();
        void startLocationUpdates(LocationRequest locationRequest);
        void stopLocationUpdates();

        void showProgressBar();
        void hideProgressBar();

        void hideFavorites();
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(String query);
        void setUpLocation();
        void stopLocationUpdates();
        void loadFoodTruckFeed(String location);
        void loadFavorites(String location);
        void updateLocation(String location);
    }
}
