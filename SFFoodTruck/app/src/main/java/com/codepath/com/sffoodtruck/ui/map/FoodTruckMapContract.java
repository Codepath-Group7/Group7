package com.codepath.com.sffoodtruck.ui.map;

import android.location.Location;

import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by robl2e on 10/13/17.
 */

public class FoodTruckMapContract {
    interface MvpView extends MvpBaseView{
        void renderFoodTrucks(List<FoodTruckMapViewModel> viewModels);
        void renderZoomToLocation(Location location);
    }

    interface Presenter extends MvpBasePresenter<MvpView>{
        void loadFoodTrucks();
    }
}
