package com.codepath.com.sffoodtruck.ui.businessdetail;

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
        void renderBusinessDetail();
        void renderBusiness(Business data);
        void addPhotoToAdapter(String photo);
        void renderReviews(List<Review> reviews);
        void addReviewToAdapter(Review review);
        void showAsFavorite(boolean isFavorite);
        boolean isAttached();
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(Business business);
        void uploadBusinessDetail();
        void loadBusiness();
        void fetchPhotosFromFirebase();
        void uploadPhotoToStorage(Uri photoUri);
        void loadReviews();
        void submitReviewToFirebase(Review review);
        void fetchReviewsFromFirebase();
        void addToFavorites();
        void checkIsFavorite();
    }
}
