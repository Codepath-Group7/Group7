package com.codepath.com.sffoodtruck.ui.businessdetail;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailFragment extends AbstractMvpFragment<BusinessDetailContract.MvpView
        , BusinessDetailContract.Presenter> implements BusinessDetailContract.MvpView {

    public static final String TAG = BusinessDetailFragment.class.getSimpleName();
    public static final String BUSINESS_KEY = "business_key";
    public static final int REQUEST_PHOTO = 99;
    public static final int REQUEST_REVIEW = 44;
    private Business mBusiness;
    private FragmentBusinessDetailBinding mBinding;
    private List<String> mPhotoList;
    private List<Review> mReviewList;
    private BusinessPhotosRecyclerViewAdapter mPhotosAdapter;
    private BusinessReviewsRecyclerViewAdapter mReviewsAdapter;
    private LinearLayoutManager mPhotosLayoutManager, mReviewsLayoutManager;

    public BusinessDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public BusinessDetailContract.Presenter createPresenter() {
        return new BusinessDetailPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    public static Fragment newInstance(Business business) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUSINESS_KEY, business);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusiness = getArguments().getParcelable(BUSINESS_KEY);
        mPhotoList = new ArrayList<>();
        mPhotosAdapter = new BusinessPhotosRecyclerViewAdapter(mPhotoList);
        mReviewList = new ArrayList<>();
        mReviewsAdapter = new BusinessReviewsRecyclerViewAdapter(mReviewList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_detail, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initialLoad(mBusiness.getId());
    }

    private void openTakePhotoDialog() {
        DialogFragment fragment = new TakePhotoDialogFragment();
        fragment.setTargetFragment(BusinessDetailFragment.this, REQUEST_PHOTO);
        fragment.show(getFragmentManager(), TakePhotoDialogFragment.TAG);
    }

    private void openSubmitReviewDialog() {
        DialogFragment fragment = new SubmitReviewDialogFragment();
        fragment.setTargetFragment(BusinessDetailFragment.this, REQUEST_REVIEW);
        fragment.show(getFragmentManager(), SubmitReviewDialogFragment.TAG);
    }

    private void setupRecyclerViews() {
        mPhotosLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvPhotosList.setLayoutManager(mPhotosLayoutManager);
        mBinding.rvPhotosList.setAdapter(mPhotosAdapter);

        mReviewsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvReviewList.setLayoutManager(mReviewsLayoutManager);
        mBinding.rvReviewList.setAdapter(mReviewsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mBinding.rvReviewList.addItemDecoration(itemDecoration);
    }

    @Override
    public void renderBusinessDetail() {
        setupRecyclerViews();
        mBinding.btnAddPhotos.setOnClickListener(v -> openTakePhotoDialog());
        mBinding.btnAddReview.setOnClickListener((view1 -> openSubmitReviewDialog()));
    }

    @Override
    public void renderBusiness(Business data) {
        mBinding.tvBusinessName.setText(data.getName());
        mBinding.tvBusinessAddress.setText(data.getLocation().getCompleteAddress());
        mBinding.tvBusinessDesc.setText(data.getAllCategories());
        mBinding.tvBusinessPhone.setText(data.getDisplayPhone());
        mBinding.rbFoodTruckRating.setRating(data.getRating());
        mBinding.tvPrice.setText(data.getPrice());
        if(data.getHours()!=null && data.getHours().size()>0){
            mBinding.tvBusinessHrs.setText(data.getHours().get(0).getTodaysHours());
        }
        mPhotosAdapter.addPhotos(data.getPhotos());
        getPresenter().fetchPhotosFromFirebase();
        Picasso.with(getActivity())
                .load(data.getImageUrl())
                .fit()
                .into(mBinding.ivBusinessImage);
    }

    @Override
    public void addPhotoToAdapter(String photo) {
        mPhotosAdapter.addPhoto(photo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri photoUri = data.getParcelableExtra(TakePhotoDialogFragment.EXTRA_PHOTO_URI);
            Log.d(TAG, "From photo dialog fragment " + photoUri);
            if(photoUri!=null && !TextUtils.isEmpty(photoUri.toString())) getPresenter().uploadPhotoToStorage(photoUri);
        }
        else if(requestCode == REQUEST_REVIEW && resultCode == Activity.RESULT_OK){
            Review review = data.getParcelableExtra(SubmitReviewDialogFragment.EXTRA_REVIEW);
            Log.d(TAG, "From review dialog fragment " + review.getText());
            getPresenter().submitReviewToFirebase(review);
        }
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
}
