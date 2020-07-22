package com.codepath.com.sffoodtruck.ui.userprofile.favorites;



import android.os.Bundle;

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
public class FavoriteFragment extends
        UserProfileBaseFragment implements
        FavoriteContract.View{

    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private static final String EXTRA_USERID = "FavoriteFragment.EXTRA_USERID";
    private FavoriteAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FavoritePresenter mFavoritePresenter;
    private String mUserId = null;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String userId){
        Bundle args = new Bundle();
        args.putString(EXTRA_USERID,userId);
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        favoriteFragment.setArguments(args);
        return favoriteFragment;
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        mFavoritePresenter = new FavoritePresenter();
        return mFavoritePresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            mUserId = getArguments().getString(EXTRA_USERID);
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

    @Override
    public String getCurrentUserId() {
        return mUserId;
    }
}
