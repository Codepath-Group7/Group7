package com.codepath.com.sffoodtruck.ui.foodtruckfeed;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentFoodTruckFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodTruckFeedFragment extends AbstractMvpFragment<FoodTruckFeedContract.MvpView,
        FoodTruckFeedContract.Presenter> implements FoodTruckFeedContract.MvpView {

    private FragmentFoodTruckFeedBinding mBinding;
    private FoodTruckFeedAdapter mAdapter;
    private static final String TAG = FoodTruckFeedFragment.class.getSimpleName();

    public FoodTruckFeedFragment() {
        // Required empty public constructor
    }


    public static FoodTruckFeedFragment newInstance(){
        return new FoodTruckFeedFragment();
    }


    @Override
    public FoodTruckFeedContract.Presenter createPresenter() {
        return new FoodTruckFeedPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FoodTruckFeedAdapter(new ArrayList<>());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_food_truck_feed,container,false);
        setupRecyclerView();
        return mBinding.getRoot();
    }

    private void setupRecyclerView() {
        mBinding.rvFoodTruckFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.rvFoodTruckFeed.setAdapter(mAdapter);
        ItemClickSupport.addTo(mBinding.rvFoodTruckFeed)
                .setOnItemClickListener((recyclerView, position, v) ->
                {
                    Log.d(TAG,"Opening detail view for "
                            + mAdapter.getBusinessForPos(position).getName());
                    startActivity(BusinessDetailActivity
                            .newIntent(getActivity(),mAdapter.getBusinessForPos(position)));
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null && isAdded()){
            getPresenter().loadFoodTruckFeed();
        }else{
            Log.d("PRESENTER","it is null");
        }
    }

    @Override
    public void showFoodTruckList(List<Business> businessList) {
        mAdapter.addAll(businessList);
    }
}
