package com.codepath.com.sffoodtruck.ui.userprofile.reviews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class ReviewsFragment extends UserProfileBaseFragment implements ReviewsContract.View {


    private static final String TAG = ReviewsFragment.class.getSimpleName();
    private ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        return mLinearLayoutManager;
    }

    @Override
    protected UserProfileBaseAdapter getUserProfileBaseAdapter() {
        mReviewsAdapter = new ReviewsAdapter(new ArrayList<>());
        return mReviewsAdapter;
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        return new ReviewsPresenter();
    }


    @Override
    public void showUserReviews(List<Review> reviewList) {
        mReviewsAdapter.addAll(reviewList);
    }
}
