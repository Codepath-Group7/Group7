package com.codepath.com.sffoodtruck.ui.userprofile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentUserProfileBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.util.CircleTransform;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends AbstractMvpFragment<UserProfileContract.MvpView,
        UserProfileContract.Presenter> implements
        UserProfileContract.MvpView{

    private static final String TAG = UserProfileFragment.class.getSimpleName();
    private FragmentUserProfileBinding mBinding;
    private FirebaseUser mUser;


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
        Picasso.with(getActivity())
                .load(mUser.getPhotoUrl())
                .transform(new CircleTransform())
                .error(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_camera_launcher))
                .into(mBinding.ivProfile);
        mBinding.tvUserName.setText(mUser.getDisplayName());
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
}
