package com.codepath.com.sffoodtruck.ui.businessdetail.reviews;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessReviewsContract {
    interface MvpView extends MvpBaseView {
        void renderReviews(List<Review> reviews);
        void addReviewToAdapter(Review review);
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(Business business);
        void loadReviews();
        void fetchReviewsFromFirebase();
    }
}
