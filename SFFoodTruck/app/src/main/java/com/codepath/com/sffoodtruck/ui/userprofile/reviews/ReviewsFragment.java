package com.codepath.com.sffoodtruck.ui.userprofile.reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private static final String EXTRA_USERID = "ReviewsFragment.EXTRA_USERID";
    private ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mUserId = null;


    public static Fragment newInstance(String userId){
        Bundle args = new Bundle();
        args.putString(EXTRA_USERID,userId);
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.setArguments(args);
        return reviewsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            mUserId = getArguments().getString(EXTRA_USERID);
        }
    }

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

    @Override
    public String getCurrentUserId() {
        return mUserId;
    }
}
