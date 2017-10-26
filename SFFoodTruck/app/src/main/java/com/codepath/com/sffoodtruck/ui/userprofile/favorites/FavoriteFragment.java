package com.codepath.com.sffoodtruck.ui.userprofile.favorites;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentFavoriteBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.UserProfileAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends
        AbstractMvpFragment<FavoriteContract.View,FavoriteContract.Presenter> implements
        FavoriteContract.View{

    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private FragmentFavoriteBinding mFavoriteBinding;
    private UserProfileAdapter mAdapter;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public FavoriteContract.Presenter createPresenter() {
        return new FavoritePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new UserProfileAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFavoriteBinding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_favorite, container, false);

        return mFavoriteBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null)
            getPresenter().initialLoad();
        else
            Log.d(TAG,"presenter is null");
    }

    @Override
    public void updateUI() {
        mFavoriteBinding.rvUserProfile.setAdapter(mAdapter);
        mFavoriteBinding.rvUserProfile.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showFavoriteFoodTrucks(List<Business> businessList) {
       mAdapter.addFavorites(businessList);
    }
}
