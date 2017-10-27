package com.codepath.com.sffoodtruck.ui.userprofile.base;

import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;

/**
 * Created by saip92 on 10/26/2017.
 */

public interface UserProfileBasePresenter<V extends UserProfileBaseView> extends
        com.hannesdorfmann.mosby3.mvp.MvpPresenter<V> {
        void initialLoad();
}
