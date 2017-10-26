package com.codepath.com.sffoodtruck.ui.userprofile;

import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.ActivityUserProfileBinding;
import com.codepath.com.sffoodtruck.ui.common.CustomFragmentPagerAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.favorites.FavoriteFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.photos.PhotosFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.recentvisits.RecentVisitsFragment;
import com.codepath.com.sffoodtruck.ui.util.ActivityUtils;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private static final String FAVORITES_TITLE = "Favorites";
    private static final String REVIEWS_TITLE = "Reviews";
    private static final String RECENT_VISITS_TITLE = "Visits";
    private static final String PHOTOS_TITLE = "Photos";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheTitleVisible = false;


    private ActivityUserProfileBinding mUserProfileBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mUserProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile);

        renderUserProfile();
        renderTabs();
    }

    private void renderUserProfile() {
        mUserProfileBinding.setUser(FirebaseAuth.getInstance().getCurrentUser());
        mUserProfileBinding.executePendingBindings();
        mUserProfileBinding.collapsingToolbarLayout
                .setExpandedTitleColor(ContextCompat.getColor(this,android.R.color.black));
        startAlphaAnimation(mUserProfileBinding.toolbar,0,View.INVISIBLE);
        mUserProfileBinding.appBarLayout
                .addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                    int maxScroll = appBarLayout.getTotalScrollRange();
                    float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                    Log.d(TAG,"Percentage: " + percentage);
                    handleToolbarVisibility(percentage);
                });
    }

    private void renderTabs() {
        CustomFragmentPagerAdapter pagerAdapter = new
                CustomFragmentPagerAdapter(getSupportFragmentManager(),true);

        pagerAdapter.addFragment(new FavoriteFragment(),FAVORITES_TITLE);
        pagerAdapter.addFragment(new RecentVisitsFragment(),RECENT_VISITS_TITLE);
        pagerAdapter.addFragment(new PhotosFragment(),PHOTOS_TITLE);
        pagerAdapter.addFragment(PageFragment.newInstance(4),"Page 4");

        mUserProfileBinding.viewpager.setAdapter(pagerAdapter);
        mUserProfileBinding.tabLayout.setupWithViewPager(mUserProfileBinding.viewpager);
    }

    private void handleToolbarVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mUserProfileBinding.toolbar,
                        ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mUserProfileBinding.toolbar,
                        ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
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
