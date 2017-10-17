package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;


/**
 * Created by akshaymathur on 10/14/17.
 */

public class BusinessDetailContract {
    interface MvpView extends MvpBaseView {
        void renderBusiness(Business data);
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void loadBusiness(Context context, String businessId);
    }
}
