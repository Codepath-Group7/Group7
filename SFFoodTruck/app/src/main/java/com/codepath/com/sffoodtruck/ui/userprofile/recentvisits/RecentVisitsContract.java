package com.codepath.com.sffoodtruck.ui.userprofile.recentvisits;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class RecentVisitsContract {

    interface View extends UserProfileBaseView{
        void showRecentVisits(List<Business> businessList);
    }

    interface Presenter extends UserProfileBasePresenter<View>{
        void loadRecentVisits();
    }
}
