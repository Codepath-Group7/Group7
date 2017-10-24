package com.codepath.com.sffoodtruck.ui.businessdetail.reviews;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessReviewsBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessReviewsFragment extends AbstractMvpFragment<BusinessReviewsContract.MvpView
        , BusinessReviewsContract.Presenter> implements BusinessReviewsContract.MvpView {

    public static final String BUSINESS_KEY = "business_key";
    public static final int REQUEST_REVIEW = 44;
    public static final String TAG = BusinessReviewsFragment.class.getSimpleName();
    private Business mBusiness;
    private FragmentBusinessReviewsBinding mBinding;
    private List<Review> mReviewList;
    private BusinessReviewsRecyclerViewAdapter mReviewsAdapter;
    private LinearLayoutManager mReviewsLayoutManager;

    public BusinessReviewsFragment() {
        // Required empty public constructor
    }

    public static BusinessReviewsFragment newInstance(Business business) {
        BusinessReviewsFragment fragment = new BusinessReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUSINESS_KEY, business);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBusiness = getArguments().getParcelable(BUSINESS_KEY);
        }
        mReviewList = new ArrayList<>();
        mReviewsAdapter = new BusinessReviewsRecyclerViewAdapter(mReviewList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_reviews,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        getPresenter().initialLoad(mBusiness);
    }
    private void setupViews(){
        mReviewsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvReviewList.setLayoutManager(mReviewsLayoutManager);
        mBinding.rvReviewList.setAdapter(mReviewsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mBinding.rvReviewList.addItemDecoration(itemDecoration);
        mBinding.fabAddReview.setOnClickListener((view1 -> openSubmitReviewDialog()));
    }

    private void openSubmitReviewDialog() {
        DialogFragment fragment = new SubmitReviewDialogFragment();
        fragment.setTargetFragment(this, REQUEST_REVIEW);
        fragment.show(getFragmentManager(), SubmitReviewDialogFragment.TAG);
    }

    @Override
    public void renderReviews(List<Review> reviews) {
        mReviewsAdapter.addReviews(reviews);
        getPresenter().fetchReviewsFromFirebase();
    }

    @Override
    public void addReviewToAdapter(Review review) {
        mReviewsAdapter.addReview(review);
    }

    @Override
    public BusinessReviewsContract.Presenter createPresenter() {
        return new BusinessReviewsPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_REVIEW && resultCode == Activity.RESULT_OK){
            Review review = data.getParcelableExtra(SubmitReviewDialogFragment.EXTRA_REVIEW);
            Log.d(TAG, "From review dialog fragment " + review.getText());
            getPresenter().submitReviewToFirebase(review);
        }
    }
}
