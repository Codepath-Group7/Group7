package com.codepath.com.sffoodtruck.ui.userprofile;

import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

/**
 * Created by saip92 on 10/21/2017.
 */

public class UserProfileContract {

    interface MvpView extends MvpBaseView{
        void showRecentVisits();
        void showFavoriteFoodTrucks();
        void showReviews();
    }


    interface Presenter extends MvpBasePresenter<MvpView>{
        void initialLoad();
        void loadRecentVisits();
        void loadFavoriteFoodTrucks();
        void loadReviews();
    }
}
