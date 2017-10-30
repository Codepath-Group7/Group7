package com.codepath.com.sffoodtruck.ui.userprofile.base;


/**
 * Created by saip92 on 10/26/2017.
 */

public interface UserProfileBaseView extends com.hannesdorfmann.mosby3.mvp.MvpView {
    void updateUI();
    void showProgressBar(boolean showProgress);
    String getCurrentUserId();
}
