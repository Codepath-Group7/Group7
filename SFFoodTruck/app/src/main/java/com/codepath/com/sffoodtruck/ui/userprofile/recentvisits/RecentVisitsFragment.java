package com.codepath.com.sffoodtruck.ui.userprofile.recentvisits;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentVisitsFragment
        extends UserProfileBaseFragment implements
        RecentVisitsContract.View {

    private static final String TAG = RecentVisitsFragment.class.getSimpleName();
    private RecentVisitsAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    public RecentVisitsFragment() {
        // Required empty public constructor
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        return new RecentVisitsPresenter();
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        return mLinearLayoutManager;
    }

    @Override
    protected UserProfileBaseAdapter getUserProfileBaseAdapter() {
        mAdapter = new RecentVisitsAdapter(new ArrayList<>());
        return mAdapter;
    }

    @Override
    public void showRecentVisits(List<Business> businessList) {
        mAdapter.addAll(businessList);
    }
}
