package com.codepath.com.sffoodtruck.ui.foodtruckfeed;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentFoodTruckFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.BaseLocationFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.util.EndlessRecyclerViewScrollListener;
import com.codepath.com.sffoodtruck.ui.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodTruckFeedFragment extends BaseLocationFragment implements
        FoodTruckFeedContract.MvpView, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentFoodTruckFeedBinding mBinding;
    private FoodTruckFeedAdapter mAdapter;
    private static final String TAG = FoodTruckFeedFragment.class.getSimpleName();
    private static final String ARG_QUERY = "query";
    private String mQuery = null;
    private static String sLocation = null;

    private SharedPreferences mSharedPreferences;
  
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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                    Intent intent = BusinessDetailActivity
                            .newIntent(getActivity(),mAdapter.getBusinessForPos(position));
                    // Check if we're running on Android 5.0 or higher
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Call some material design APIs here
                        ImageView ivBanner = (ImageView) v.findViewById(R.id.ivBanner);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(getActivity(), (View)ivBanner, "businessImage");

                        startActivity(intent,options.toBundle());
                    } else {
                        // Implement this feature without material design
                        startActivity(intent);
                    }

                });
        mBinding.rvFoodTruckFeed.addOnScrollListener
                (new EndlessRecyclerViewScrollListener(layoutManager){
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        Log.d(TAG,"Calling load more");
                        getPresenter().loadFoodTruckFeed(sLocation,mQuery, page);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if(getPresenter() != null && isAdded()){
            getPresenter().initialLoad(mQuery); //add query String
        }else{
            Log.d("PRESENTER","it is null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void appendFoodTruckList(List<Business> businessList) {
        mAdapter.addData(businessList);
    }

    @Override
    public void loadInitialFoodTruckList(List<Business> businessList) {
        mAdapter.clearData();
        mAdapter.addData(businessList);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(QueryPreferences.PREF_CURRENT_LOCATION)){
            //Handle sLocation updates accordingly
            Log.d(TAG,"Location: " + QueryPreferences.getCurrentLocation(getActivity()));
            sLocation = QueryPreferences.getCurrentLocation(getActivity());
            Toast.makeText(getActivity(),"Current Location is: " +
                    QueryPreferences.getCurrentLocation(getActivity()),Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                    getString(R.string.new_location),Snackbar.LENGTH_LONG);

            snackbar.setAction(getString(R.string.load_new_results), view -> {
                if(getPresenter() != null){
                    getPresenter().updateLocation(sLocation,mQuery);
                }
            }).show();
        }
    }
}
