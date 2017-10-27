package com.codepath.com.sffoodtruck.ui.userprofile.reviews;

import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class ReviewsContract {

    interface View extends UserProfileBaseView{
        void showUserReviews(List<Review> reviewList);
    }

    interface Presenter extends UserProfileBasePresenter<View>{
        void loadUserReviews();
    }
}
