package com.codepath.com.sffoodtruck.ui.homefeed;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentHomeFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.util.LinePagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedFragment extends AbstractMvpFragment<HomeFeedContract.MvpView,HomeFeedContract.Presenter>
 implements HomeFeedContract.MvpView{

    private FragmentHomeFeedBinding mHomeFeedBinding;
    private HomeFeedAdapter mFavoriteAdapter;
    public HomeFeedFragment(){}

    @Override
    public void initializeUI() {

    }

    @Override
    public void appendFoodTruckList(List<Business> businessList) {

    }

    @Override
    public void addInitialFoodTruckList(List<Business> businessList) {

    }

    @Override
    public void addFavoritesFoodTruckList(List<Business> businessList) {
        mFavoriteAdapter.addAll(businessList);
    }

    @Override
    public void getLastKnownLocation() {

    }

    @Override
    public void startLocationUpdates() {

    }

    @Override
    public void stopLocationUpdates() {

    }

    @Override
    public HomeFeedContract.Presenter createPresenter() {
        return new HomeFeedPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteAdapter = new HomeFeedAdapter(new ArrayList<>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHomeFeedBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_feed,container,false);
        setUpRecyclerView();
        return mHomeFeedBinding.getRoot();
    }

    private void setUpRecyclerView() {
        mHomeFeedBinding.rvFavorites.setAdapter(mFavoriteAdapter);
        mHomeFeedBinding.rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mHomeFeedBinding.rvFavorites.addItemDecoration(new LinePagerIndicatorDecoration());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initialLoad(null);
    }
}
