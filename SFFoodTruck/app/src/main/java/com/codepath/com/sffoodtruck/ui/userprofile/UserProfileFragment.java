package com.codepath.com.sffoodtruck.ui.userprofile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentUserProfileBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.util.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends AbstractMvpFragment<UserProfileContract.MvpView,
        UserProfileContract.Presenter> implements
        UserProfileContract.MvpView,  AppBarLayout.OnOffsetChangedListener{

    private static final String TAG = UserProfileFragment.class.getSimpleName();
    private FragmentUserProfileBinding mBinding;
    private FirebaseUser mUser;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheTitleVisible          = false;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public UserProfileContract.Presenter createPresenter() {
        return new UserProfilePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_user_profile, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null)
            getPresenter().initialLoad();
        else
            Log.d(TAG,"presenter is null");
    }

    @Override
    public void updateUI() {
        mBinding.setUser(mUser);
        mBinding.executePendingBindings();
        mBinding.appBarLayout.addOnOffsetChangedListener(this);
        /*Picasso.with(getActivity())
                .load(mUser.getPhotoUrl())
                .transform(new CircleTransform())
                .error(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_camera_launcher))
                .into(mBinding.ivProfile);*/

        startAlphaAnimation(mBinding.toolbar,0,View.INVISIBLE);
        mBinding.collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(),
                android.R.color.black));
        mBinding.collapsingToolbarLayout
                .setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(),
                android.R.color.white));

    }

    @Override
    public void showRecentVisits(List<Business> businessList) {
        mBinding.recentVisits.btnShowMore
                .setVisibility(businessList.size() <= 5 ? View.GONE: View.VISIBLE);
        mBinding.recentVisits.tvListTitle.setText(getString(R.string.recent_visits_title));
        mBinding.recentVisits.rvUserProfile.setAdapter(new UserProfileAdapter(businessList));
        mBinding.recentVisits.rvUserProfile
                .setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showFavoriteFoodTrucks(List<Business> businessList) {
        mBinding.favorite.btnShowMore
                .setVisibility(businessList.size() <= 5 ? View.GONE: View.VISIBLE);
        mBinding.favorite.tvListTitle.setText(getString(R.string.favorites_title));
        mBinding.favorite.rvUserProfile.setAdapter(new UserProfileAdapter(businessList));
        mBinding.favorite.rvUserProfile
                .setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showReviews(List<Business> businessList) {
        mBinding.reviews.btnShowMore
                .setVisibility(businessList.size() <= 5 ? View.GONE: View.VISIBLE);
        mBinding.reviews.tvListTitle.setText(getString(R.string.reviews_title));
        mBinding.reviews.rvUserProfile.setAdapter(new UserProfileAdapter(businessList));
        mBinding.reviews.rvUserProfile
                .setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        Log.d(TAG,"Percentage: " + percentage);
        handleToolbarVisibility(percentage);
    }

    private void handleToolbarVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mBinding.toolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mBinding.toolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }


    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
