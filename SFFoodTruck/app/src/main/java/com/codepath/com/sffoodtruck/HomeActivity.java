package com.codepath.com.sffoodtruck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.codepath.com.sffoodtruck.infrastructure.service.FirebaseRegistrationIntentService;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedFragment;
import com.codepath.com.sffoodtruck.ui.login.LoginActivity;
import com.codepath.com.sffoodtruck.ui.nearby.NearByActivity;
import com.codepath.com.sffoodtruck.ui.search.SearchActivity;
import com.codepath.com.sffoodtruck.ui.settings.SettingsActivity;
import com.codepath.com.sffoodtruck.ui.userprofile.UserProfileActivity;
import com.codepath.com.sffoodtruck.ui.util.ActivityUtils;
import com.codepath.com.sffoodtruck.ui.map.FoodTruckMapFragment;
import com.codepath.com.sffoodtruck.ui.util.PlayServicesUtil;
import com.crashlytics.android.Crashlytics;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private final static String TAG = HomeActivity.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                changeFragment(item.getItemId());
                return true;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (PlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            FirebaseRegistrationIntentService.start(this);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(R.id.navigation_home);
        ImageButton  imageButton = (ImageButton) findViewById(R.id.imagebtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentActivity(view);
            }
        });
    }

    private void changeFragment(int itemViewId) {
        Fragment newFragment = null;

        switch (itemViewId) {
            case R.id.navigation_map:
                newFragment = FoodTruckMapFragment.newInstance();
                break;
            case R.id.navigation_home:
                newFragment = FoodTruckFeedFragment.newInstance(null);
                break;
        }

        if (newFragment == null) return;

        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (newFragment.getClass().isInstance(currentFragment)) return;

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                newFragment,R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_screen,menu);
        //MenuItem searchItem = menu.findItem(R.id.menu_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                presentActivity(searchView);
                return true;
            }
        });*/
        // Get the SearchView and set the searchable configuration
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default*/
        return true;
    }

    private void presentActivity(View view) {

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(SearchActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity =
                        new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_nearby:
                startActivity(new Intent(this, NearByActivity.class));
                return true;
            case R.id.action_account:
                Intent accountIntent =
                        new Intent(this, UserProfileActivity.class);
                startActivity(accountIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        googleSignOut();
        startLoginActivity();
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
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d(TAG, "User Logged out");
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
