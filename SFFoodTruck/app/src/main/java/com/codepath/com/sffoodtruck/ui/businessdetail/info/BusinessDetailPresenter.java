package com.codepath.com.sffoodtruck.ui.businessdetail.info;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by akshaymathur on 10/14/17.
 * Modified by saip
 */

public class BusinessDetailPresenter extends AbstractPresenter<BusinessDetailContract.MvpView>
        implements BusinessDetailContract.Presenter {

    public static final String TAG = BusinessDetailPresenter.class.getSimpleName();
    private final String token;
    private static String sBusinessId;
    private static Business sBusiness;
    private DatabaseReference mDatabaseReference;
    private static final int DELAYED_CONST = 5000;

    public BusinessDetailPresenter(String token){
        this.token = token;
    }

    @Override
    public void initialLoad(Business business) {
        sBusinessId = business.getId();
        sBusiness = business;
        loadBusiness();
    }

    @Override
    public void loadBusiness() {
        getView().renderBusiness(sBusiness);
    }


}
