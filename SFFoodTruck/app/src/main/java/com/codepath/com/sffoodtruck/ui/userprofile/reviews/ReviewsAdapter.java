package com.codepath.com.sffoodtruck.ui.userprofile.reviews;


import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class ReviewsAdapter extends UserProfileBaseAdapter<Review> {

    private List<Review> mReviews;

    public ReviewsAdapter(List<Review> list) {
        super(list, R.layout.userprofile_reviews);
        mReviews = list;
    }


}
