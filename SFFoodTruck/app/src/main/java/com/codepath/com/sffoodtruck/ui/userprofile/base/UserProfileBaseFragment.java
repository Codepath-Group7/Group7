package com.codepath.com.sffoodtruck.ui.userprofile.base;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.FragmentUserProfileBinding;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class UserProfileBaseFragment extends
        MvpFragment<UserProfileBaseView,UserProfileBasePresenter<UserProfileBaseView>> implements
        UserProfileBaseView{


    private static final String TAG = UserProfileBaseFragment.class.getSimpleName();
    protected FragmentUserProfileBinding mFavoriteBinding;
    protected UserProfileBaseAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

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
                .inflate(inflater,R.layout.fragment_user_profile, container, false);

        return mFavoriteBinding.getRoot();
    }


    @Override
    public void updateUI() {
        showProgressBar(true);
        mLayoutManager = getLayoutManager();
        mFavoriteBinding.rvUserProfile.setAdapter(mAdapter);
        mFavoriteBinding.rvUserProfile.setLayoutManager(mLayoutManager);
    }

    @Override
    public void showProgressBar(boolean showProgress){
        if(showProgress){
            mFavoriteBinding.rvUserProfile.setVisibility(View.INVISIBLE);
            mFavoriteBinding.progressBar.setVisibility(View.VISIBLE);
        }else{
            mFavoriteBinding.rvUserProfile.setVisibility(View.VISIBLE);
            mFavoriteBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getPresenter() != null)
            getPresenter().initialLoad();
        else
            Log.d(TAG,"presenter is null");
    }

    protected abstract UserProfileBaseAdapter getUserProfileBaseAdapter();
}
