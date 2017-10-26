package com.codepath.com.sffoodtruck.ui.userprofile.favorites;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class FavoriteContract {

    interface View extends MvpBaseView{
        void updateUI();
        void showFavoriteFoodTrucks(List<Business> businessList);
    }

    interface Presenter extends MvpBasePresenter<View>{
        void initialLoad();
        void loadFavoriteFoodTrucks();
    }

}
