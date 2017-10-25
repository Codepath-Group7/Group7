package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessPhotosBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;

import java.util.ArrayList;
import java.util.List;

public class BusinessPhotosFragment extends AbstractMvpFragment<BusinessPhotosContract.MvpView
        , BusinessPhotosContract.Presenter> implements BusinessPhotosContract.MvpView {

    public static final String BUSINESS_KEY = "business_key";
    public static final String TAG = BusinessPhotosFragment.class.getSimpleName();
    public static final int REQUEST_PHOTO = 99;
    private Business mBusiness;
    private List<String> mPhotoList;
    private BusinessPhotosRecyclerViewAdapter mPhotosAdapter;
    private FragmentBusinessPhotosBinding mBinding;
    private StaggeredGridLayoutManager mPhotosLayoutManager;
    public BusinessPhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public BusinessPhotosContract.Presenter createPresenter() {
        return new BusinessPhotosPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    public static BusinessPhotosFragment newInstance(Business business) {
        BusinessPhotosFragment fragment = new BusinessPhotosFragment();
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
        mPhotoList = new ArrayList<>();
        mPhotosAdapter = new BusinessPhotosRecyclerViewAdapter(mPhotoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_business_photos, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        getPresenter().initialLoad(mBusiness);
    }

    private void setupView() {
        mPhotosLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvPhotosList.setLayoutManager(mPhotosLayoutManager);
        mBinding.rvPhotosList.setAdapter(mPhotosAdapter);
    }

    @Override
    public void renderPhotos(List<String> photoList) {
        mPhotosAdapter.addPhotos(photoList);
        getPresenter().fetchPhotosFromFirebase();
    }

    @Override
    public void addPhotoToAdapter(String photo) {
        mPhotosAdapter.addPhoto(photo);
    }

}
