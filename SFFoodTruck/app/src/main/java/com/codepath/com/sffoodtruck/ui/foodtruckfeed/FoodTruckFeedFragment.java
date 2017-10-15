package com.codepath.com.sffoodtruck.ui.foodtruckfeed;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.FragmentFoodTruckFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodTruckFeedFragment extends AbstractMvpFragment<FoodTruckFeedContract.MvpView,
        FoodTruckFeedContract.Presenter> {

    private FragmentFoodTruckFeedBinding mBinding;

    public FoodTruckFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public FoodTruckFeedContract.Presenter createPresenter() {
        return new FoodTruckFeedPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_food_truck_feed,container,false);

        setupRecyclerView();
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    private void setupRecyclerView() {

    }

}
