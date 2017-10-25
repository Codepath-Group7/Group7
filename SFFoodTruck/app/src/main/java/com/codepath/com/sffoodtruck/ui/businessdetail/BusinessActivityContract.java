package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.net.Uri;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessActivityContract {
    interface MvpView extends MvpBaseView {
        void renderBusiness(Business data);
        void showAsFavorite(boolean isFavorite);
        boolean isAttached();
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(Business business);
        void uploadBusinessDetail();
        void loadBusiness();
        void addToFavorites();
        void checkIsFavorite();
        void uploadPhotoToStorage(Uri photoUri);
        void submitReviewToFirebase(Review review);
    }
}
