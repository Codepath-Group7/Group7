package com.codepath.com.sffoodtruck.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.com.sffoodtruck.HomeActivity;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.ActivitySearchBinding;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailFragment;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedFragment;
import com.codepath.com.sffoodtruck.ui.login.LoginActivity;
import com.codepath.com.sffoodtruck.ui.settings.SettingsActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private final static String TAG = SearchActivity.class.getSimpleName();
    private ActivitySearchBinding mBinding;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        setSupportActionBar(mBinding.toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleIntent(getIntent());


    }

    private void initialize() {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fl_food_truck_feed);
        if (fragment!=null) {
            fragment = FoodTruckFeedFragment.newInstance(mQuery);
            fm.beginTransaction()
                    .replace(R.id.fl_food_truck_feed, fragment)
                    .commit();
        } else {
            fragment = FoodTruckFeedFragment.newInstance(mQuery);
            fm.beginTransaction()
                    .add(R.id.fl_food_truck_feed, fragment)
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        Log.d(TAG,"The search query is--> "+query);
        mQuery = query;
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_screen,menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // If query String is not empty set the value to the search view.
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.expandActionView();
        searchView.requestFocus();
        if (!TextUtils.isEmpty(mQuery)) {
            Log.d(TAG, "Setting query");

            searchView.setQuery(mQuery, false);
        }

        //Close the Search results fragment when home button on app bar is pressed.
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d(TAG,"Search view back pressed");
                finish();
                return true;
            }
        });

        return true;
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
            case R.id.menu_search:
                return true;
            case android.R.id.home:
                finish();
                return true;

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
