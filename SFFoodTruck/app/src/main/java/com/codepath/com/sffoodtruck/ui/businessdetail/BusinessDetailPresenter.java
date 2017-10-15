package com.codepath.com.sffoodtruck.ui.businessdetail;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;

/**
 * Created by akshaymathur on 10/14/17.
 */

public class BusinessDetailPresenter extends AbstractPresenter<BusinessDetailContract.MvpView> implements BusinessDetailContract.Presenter {

    @Override
    public void loadBusiness() {

        getView().renderBusiness(new Business());
    }
}
