package com.codepath.com.sffoodtruck.ui.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.ActivityUserProfileBinding;
import com.codepath.com.sffoodtruck.ui.common.CustomFragmentPagerAdapter;
import com.codepath.com.sffoodtruck.ui.login.LoginActivity;
import com.codepath.com.sffoodtruck.ui.settings.SettingsActivity;
import com.codepath.com.sffoodtruck.ui.userprofile.favorites.FavoriteFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.photos.PhotosFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.recentvisits.RecentVisitsFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.reviews.ReviewsFragment;
import com.codepath.com.sffoodtruck.ui.util.CircleTransform;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private static final String FAVORITES_TITLE = "Favorites";
    private static final String REVIEWS_TITLE = "Reviews";
    private static final String RECENT_VISITS_TITLE = "Visits";
    private static final String PHOTOS_TITLE = "Photos";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private static final String EXTRA_USER = "UserProfileActivity.EXTRA_USER";
    private static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private boolean mIsTheTitleVisible = false;


    private ActivityUserProfileBinding mUserProfileBinding;

    private GoogleApiClient mGoogleApiClient;

    private static boolean isCurrentUser = false;

    private static String mCurrentUserId;

    private static MessagePayload sMessagePayload;



    public static Intent newIntent(Context context, MessagePayload payload){
        Intent intent = new Intent(context,UserProfileActivity.class);
        intent.putExtra(EXTRA_USER,payload);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        setSupportActionBar(mUserProfileBinding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        checkForCurrentUser();
        renderUserProfile();
        renderTabs();

    }

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, UserProfileActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        checkForCurrentUser();
    }

    private void checkForCurrentUser() {
        if(getIntent() != null){
            sMessagePayload = getIntent().getParcelableExtra(EXTRA_USER);
            if(sMessagePayload == null || sMessagePayload.getUserId()
                    .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                isCurrentUser = true;
            }
        }

        if(isCurrentUser){
            mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }else{
            mCurrentUserId = sMessagePayload.getUserId();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCurrentUser();
    }

    private void renderUserProfile() {
        mUserProfileBinding.setIsCurrentUser(isCurrentUser);

        if(isCurrentUser){
            Log.d(TAG,"Loading current user profile");
            mUserProfileBinding.setUser(FirebaseAuth.getInstance().getCurrentUser());
        }else{
            Log.d(TAG,"Loading secondary person user profile: " + sMessagePayload.getUserEmail());
            mUserProfileBinding.setPayload(sMessagePayload);
        }
        mUserProfileBinding.executePendingBindings();
        mUserProfileBinding.collapsingToolbarLayout
                .setExpandedTitleColor(ContextCompat.getColor(this,android.R.color.black));
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

        pagerAdapter.addFragment(FavoriteFragment.newInstance(mCurrentUserId),FAVORITES_TITLE);
        if(isCurrentUser) pagerAdapter.addFragment(new RecentVisitsFragment(),RECENT_VISITS_TITLE);
        pagerAdapter.addFragment(PhotosFragment.newInstance(mCurrentUserId),PHOTOS_TITLE);
        pagerAdapter.addFragment(ReviewsFragment.newInstance(mCurrentUserId),REVIEWS_TITLE);

        mUserProfileBinding.viewpager.setAdapter(pagerAdapter);
        mUserProfileBinding.tabLayout.setupWithViewPager(mUserProfileBinding.viewpager);
    }

    private void handleToolbarVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                Picasso.with(this)
                        .load(isCurrentUser ?
                                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl():
                                Uri.parse(sMessagePayload.getImageUrl()))
                        .transform(new CircleTransform())
                        .into(mUserProfileBinding.ivToolbarProfile);
                startAlphaAnimation(mUserProfileBinding.ivToolbarProfile,
                        ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mUserProfileBinding.ivToolbarProfile,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(0,R.anim.slide_down);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity =
                        new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.slide_down);
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        googleSignOut();
        startLoginActivity();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void googleSignOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d(TAG, "User Logged out");
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentUser = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




}
