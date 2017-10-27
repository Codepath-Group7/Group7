package com.codepath.com.sffoodtruck.ui.userprofile.favorites;



import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends
        UserProfileBaseFragment implements
        FavoriteContract.View{

    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private FavoriteAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        return new FavoritePresenter();
    }


    @Override
    protected UserProfileBaseAdapter getUserProfileBaseAdapter() {
        mAdapter = new FavoriteAdapter(new ArrayList<>());
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        return mLinearLayoutManager;
    }

    @Override
    public void showFavoriteFoodTrucks(List<Business> businessList) {
        mAdapter.addAll(businessList);
    }
}
