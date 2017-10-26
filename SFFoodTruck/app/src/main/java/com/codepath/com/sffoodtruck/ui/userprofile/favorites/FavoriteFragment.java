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
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends
        UserProfileBaseFragment implements
        FavoriteContract.View{

    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private FavoritesAdapter mAdapter;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        return new FavoritePresenter();
    }


    @Override
    protected UserProfileBaseAdapter getUserProfileBaseAdapter() {
        mAdapter = new FavoritesAdapter(new ArrayList<>());
        return mAdapter;
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
