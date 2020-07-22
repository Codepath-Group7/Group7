package com.codepath.com.sffoodtruck.ui.userprofile.recentvisits;


import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public String getCurrentUserId() {
        return null;
    }
}
