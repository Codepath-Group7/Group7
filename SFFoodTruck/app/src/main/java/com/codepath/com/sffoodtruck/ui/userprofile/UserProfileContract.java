package com.codepath.com.sffoodtruck.ui.userprofile;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/21/2017.
 */

public class UserProfileContract {

    interface MvpView extends MvpBaseView{
        void updateUI();
        void showRecentVisits(List<Business> businessList);
        void showFavoriteFoodTrucks(List<Business> businessList);
        void showReviews(List<Business> businessList);
    }


    interface Presenter extends MvpBasePresenter<MvpView>{
        void initialLoad();
        void loadRecentVisits();
        void loadFavoriteFoodTrucks();
        void loadReviews();
    }
}
