package com.codepath.com.sffoodtruck.ui.foodtruckfeed;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.codepath.com.sffoodtruck.ui.base.mvp.BaseLocationFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.util.EndlessRecyclerViewScrollListener;
import com.codepath.com.sffoodtruck.ui.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodTruckFeedFragment extends BaseLocationFragment implements FoodTruckFeedContract.MvpView {

    private FragmentFoodTruckFeedBinding mBinding;
    private FoodTruckFeedAdapter mAdapter;
    private static final String TAG = FoodTruckFeedFragment.class.getSimpleName();
    private static final String ARG_QUERY = "query";
    private String mQuery = null;
    public FoodTruckFeedFragment() {
        // Required empty public constructor
    }


    public static FoodTruckFeedFragment newInstance(String queryString){
        FoodTruckFeedFragment foodTruckFeedFragment = new FoodTruckFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_QUERY,queryString);
        foodTruckFeedFragment.setArguments(bundle);
        return foodTruckFeedFragment;
    }


    @Override
    public FoodTruckFeedContract.Presenter createPresenter() {
        return new FoodTruckFeedPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mQuery = getArguments().getString(ARG_QUERY);
        }
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvFoodTruckFeed.setLayoutManager(layoutManager);
        mBinding.rvFoodTruckFeed.setAdapter(mAdapter);
        ItemClickSupport.addTo(mBinding.rvFoodTruckFeed)
                .setOnItemClickListener((recyclerView, position, v) ->
                {
                    Log.d(TAG,"Opening detail view for "
                            + mAdapter.getBusinessForPos(position).getName());
                    startActivity(BusinessDetailActivity
                            .newIntent(getActivity(),mAdapter.getBusinessForPos(position)));
                });
        mBinding.rvFoodTruckFeed.addOnScrollListener
                (new EndlessRecyclerViewScrollListener(layoutManager){
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        getPresenter().loadFoodTruckFeed(mQuery, page);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null && isAdded()){
            getPresenter().initialLoad(mQuery); //add query String
        }else{
            Log.d("PRESENTER","it is null");
        }
    }

    @Override
    public void showFoodTruckList(List<Business> businessList) {
        mAdapter.addData(businessList);
    }
}
