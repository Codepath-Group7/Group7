package com.codepath.com.sffoodtruck.ui.userprofile.base;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.FragmentFavoriteBinding;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class UserProfileBaseFragment extends
        MvpFragment<UserProfileBaseView,UserProfileBasePresenter<UserProfileBaseView>> implements
        UserProfileBaseView{


    private static final String TAG = UserProfileBaseFragment.class.getSimpleName();
    protected FragmentFavoriteBinding mFavoriteBinding;
    protected UserProfileBaseAdapter mAdapter;
    private UserProfileBaseAdapter mUserProfileBaseAdapter;

    public UserProfileBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = getUserProfileBaseAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFavoriteBinding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_favorite, container, false);

        return mFavoriteBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null)
            getPresenter().initialLoad();
        else
            Log.d(TAG,"presenter is null");
    }

    protected abstract UserProfileBaseAdapter getUserProfileBaseAdapter();
}
