package com.codepath.com.sffoodtruck.ui.businessdetail.info;

import android.content.Context;
import android.net.Uri;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;


/**
 * Created by akshaymathur on 10/14/17.
 */

public class BusinessDetailContract {
    interface MvpView extends MvpBaseView {
        void renderBusiness(Business data);
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(Business business);
        void loadBusiness();

    }
}
