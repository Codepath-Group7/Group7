package com.codepath.com.sffoodtruck.ui.foodtruckfeed;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.databinding.FragmentFoodTruckFeedBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.BaseLocationFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.homefeed.FeedAdapter;
import com.codepath.com.sffoodtruck.ui.homefeed.HomeFeedFragment;
import com.codepath.com.sffoodtruck.ui.homefeed.ShareBottomSheet;
import com.codepath.com.sffoodtruck.ui.util.EndlessRecyclerViewScrollListener;
import com.codepath.com.sffoodtruck.ui.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodTruckFeedFragment extends BaseLocationFragment implements
        FoodTruckFeedContract.MvpView, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String ARG_LOCATION = "ARG_LOCATION";
    private FragmentFoodTruckFeedBinding mBinding;
    private FeedAdapter mAdapter;
    private static final String TAG = FoodTruckFeedFragment.class.getSimpleName();
    private static final String ARG_QUERY = "query";
    private String mQuery = null;
    private static String sLocation = null;
    private static final int RC_SHARE_DATA = 124;
    private static final String OPEN_BOTTOM_SHEET = "FoodTruckFeedFragment.REQUEST_TO_OPEN_BOTTOM_SHEET";

    private FeedAdapter.onBusinessItemClickListener mOnBusinessItemClickListener
            = new FeedAdapter.onBusinessItemClickListener() {
        @Override
        public void onClickBusinessItem(Business business, View view) {
            Intent intent = BusinessDetailActivity
                    .newIntent(getActivity(),business);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ImageView ivBanner = (ImageView) view.findViewById(R.id.ivBanner);
                // Call some material design APIs here
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), ivBanner, "businessImage");

                startActivity(intent,options.toBundle());
            } else {
                // Implement this feature without material design
                startActivity(intent);
            }
        }

        @Override
        public void onClickFab(Business business) {
            BottomSheetDialogFragment shareBottomSheet =
                    ShareBottomSheet.newInstance(business);
            shareBottomSheet.setTargetFragment(FoodTruckFeedFragment.this,RC_SHARE_DATA);
            shareBottomSheet.show(getChildFragmentManager(),OPEN_BOTTOM_SHEET);
        }
    };

    private SharedPreferences mSharedPreferences;
  
    public FoodTruckFeedFragment() {
        // Required empty public constructor
    }


    public static FoodTruckFeedFragment newInstance(String queryString, String location){
        FoodTruckFeedFragment foodTruckFeedFragment = new FoodTruckFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_QUERY,queryString);
        bundle.putString(ARG_LOCATION,location);
        foodTruckFeedFragment.setArguments(bundle);
        return foodTruckFeedFragment;
    }



    @Override
    public FoodTruckFeedContract.Presenter createPresenter() {
         SearchApi services = RetrofitClient
                .createService(SearchApi.class,getActivity());
        return new FoodTruckFeedPresenter(services);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mQuery = getArguments().getString(ARG_QUERY);
            sLocation = getArguments().getString(ARG_LOCATION);
        }
        mAdapter = new FeedAdapter(new ArrayList<>(),mOnBusinessItemClickListener);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initialLoad(mQuery,sLocation);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvFoodTruckFeed.setLayoutManager(layoutManager);
        mBinding.rvFoodTruckFeed.setAdapter(mAdapter);
        /*ItemClickSupport.addTo(mBinding.rvFoodTruckFeed)
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

                });*/
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
            Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                    getString(R.string.new_location),Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction(getString(R.string.load_new_results), view -> {
                if(getPresenter() != null){
                    getPresenter().updateLocation(QueryPreferences.getCurrentLocation(getActivity()),mQuery);
                }
            }).show();
        }
    }
}
