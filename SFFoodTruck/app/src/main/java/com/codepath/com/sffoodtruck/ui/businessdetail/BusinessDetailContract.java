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
        void renderBusiness(Business data);
        void addPhotoToAdapter(String photo);
        void renderReviews(List<Review> reviews);
        void addReviewToAdapter(Review review);
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void loadBusiness(Context context, String businessId);
        void fetchPhotosFromFirebase(String businessId);
        void uploadPhotoToStorage(Uri photoUri,String businessId);
        void loadReviews(Context context, String businessId);
        void submitReviewToFirebase(String businessId, Review review);
        void fetchReviewsFromFirebase(String businessId);
    }
}
