package com.codepath.com.sffoodtruck;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.codepath.com.sffoodtruck.data.local.DBPayloads;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.infrastructure.service.FirebaseRegistrationIntentService;
import com.codepath.com.sffoodtruck.ui.foodtruckfeed.FoodTruckFeedFragment;
import com.codepath.com.sffoodtruck.ui.homefeed.HomeFeedFragment;
import com.codepath.com.sffoodtruck.ui.nearby.NearByFragment;
import com.codepath.com.sffoodtruck.ui.search.SearchActivity;
import com.codepath.com.sffoodtruck.ui.userprofile.UserProfileActivity;
import com.codepath.com.sffoodtruck.ui.util.ActivityUtils;
import com.codepath.com.sffoodtruck.ui.map.FoodTruckMapFragment;
import com.codepath.com.sffoodtruck.ui.util.ParcelableUtil;
import com.codepath.com.sffoodtruck.ui.util.PlayServicesUtil;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.fragment.app.Fragment;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        NearByFragment.onNearByFragmentListener,
        HomeFeedFragment.onHomeFeedFragmentListener,
        FoodTruckFeedFragment.onFoodTruckFeedListener{

    private final static String TAG = HomeActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Message mActiveMessage;
    private MessageListener mMessageListener;
    private final NearByFragment mNearByFragment = new NearByFragment();
  //  private final FoodTruckFeedFragment mFoodTruckFeedFragment = FoodTruckFeedFragment.newInstance(null);
    private final HomeFeedFragment mHomeFeedFragment = new HomeFeedFragment();
    private final FoodTruckMapFragment mFoodTruckMapFragment = FoodTruckMapFragment.newInstance();
    private boolean isNearByFragment = false;
    private boolean isHomeFeedFragment = false;
    private BottomNavigationView mBottomNavigationView;
    private ImageButton  imageButton;
    private Toolbar toolbar;


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

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        imageButton = (ImageButton) findViewById(R.id.imagebtn);

        setSupportActionBar(toolbar);

        setUpNearBy();

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(R.id.navigation_home);

        imageButton.setOnClickListener(this::presentActivity);
    }

    private void setUpNearBy(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this,this)
                .build();
        Log.d(TAG,"Setting message listener");
        mMessageListener = new MessageListener(){
            @Override
            public void onFound(Message message) {
                byte[] bytes = message.getContent();
                MessagePayload payload = ParcelableUtil.unmarshall(bytes,MessagePayload.CREATOR);


                Log.d(TAG,"Actual Payload: " + payload + ": from -> "
                        + DBPayloads.getInstance().getMessagePayloads());
                if(isNearByFragment && !DBPayloads.getInstance().isDuplicate(payload)){
                    ((NearByFragment)getOnScreenFragment())
                            .addMessagePayload(payload);
                }

                DBPayloads.getInstance().storeMessagePayload(payload);
                Log.d(TAG,"Found message: " + payload);
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
            }
        };
    }

    private void changeFragment(int itemViewId) {
        Fragment newFragment = null;

        isNearByFragment = false;
        isHomeFeedFragment = false;
        setToolbarTitle();
        switch (itemViewId) {
            case R.id.navigation_map:
                newFragment = mFoodTruckMapFragment;
                break;
            case R.id.navigation_home:
                newFragment = mHomeFeedFragment;
                isHomeFeedFragment = true;
                break;
            case R.id.navigation_group:
                newFragment = mNearByFragment;
                isNearByFragment = true;
                break;
        }

        if (newFragment == null) return;

        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (newFragment.getClass().isInstance(currentFragment)) return;

        if(itemViewId == R.id.navigation_home){
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newFragment,R.id.content,R.anim.slide_in_left,R.anim.slide_out_right);
            return;
        }

        if(itemViewId == R.id.navigation_map){
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newFragment,R.id.content);
            return;
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                newFragment,R.id.content,R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_screen,menu);
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
            case R.id.action_account:
                int[] startingLocation = new int[2];
                toolbar.getLocationOnScreen(startingLocation);
                startingLocation[0] += toolbar.getWidth() / 2;
                UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(isHomeFeedFragment){
            Log.d(TAG,"Calling HomeScreenFragment's setgoogleApi");
            if(getOnScreenFragment() instanceof  HomeFeedFragment){
                ((HomeFeedFragment)getOnScreenFragment())
                        .setGoogleApiClient(mGoogleApiClient);
            }

        }else{
            Log.d(TAG,"isHomeFragment is false");
        }
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSendClick(MessagePayload messagePayload) {
        Log.d(TAG,"Got the payload: " + messagePayload);
        publish(messagePayload);
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void subscribe(){
        Log.i(TAG,"Subscribing");
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) return;
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setCallback(new SubscribeCallback(){
                    @Override
                    public void onExpired() {
                        Log.d(TAG,"subscription has expired");
                    }
                }).build();
        Nearby.Messages.subscribe(mGoogleApiClient,mMessageListener,options)
                .setResultCallback(status -> Log.d(TAG,"Status of subscription: "
                        + status.getStatusMessage() + " "
                        + status.getStatus()  + " "
                        + status.getStatusCode() ));
    }

    private void unsubscribe(){
        Log.i(TAG,"Unsubscribing");
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) return;

        Nearby.Messages.unsubscribe(mGoogleApiClient,mMessageListener);
    }


    private void publish(MessagePayload payload){
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) return;

        Log.d(TAG,"Publishing the payload " + payload);
        mActiveMessage = new Message(ParcelableUtil.marshall(payload));
        PublishOptions options = new PublishOptions.Builder().setCallback(new PublishCallback(){
            @Override
            public void onExpired() {
                Log.d(TAG,"Message has expired " + payload.getMessage());
            }
        }).build();
        Nearby.Messages.publish(mGoogleApiClient,mActiveMessage,options)
                .setResultCallback(status ->{

                    boolean isPublishSuccess = false;
                    if(status.getStatusCode() == 0 || status.getStatusCode() == -1){
                       isPublishSuccess = true;
                    }

                    //Checking whether the onscreen fragment is NearByFragment or not
                    if(isNearByFragment){
                       /* DBPayloads.getInstance().storeMessagePayload(payload);*/
                        ((NearByFragment)getOnScreenFragment())
                                .publishSuccessful(isPublishSuccess,payload.getUUID());
                        DBPayloads.getInstance().storeMessagePayload(payload);
                    }

                    Log.d(TAG,"Status of publishing message: "
                            + payload.getMessage() + ", status:  "
                            + status.getStatusMessage() + " "
                            + status.getStatus()  + " "
                            + status.getStatusCode());
                });
    }

    private void unpublish(){
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) return;

        if(mActiveMessage != null){
            Log.i(TAG,"Unpublishing");
            Nearby.Messages.unpublish(mGoogleApiClient,mActiveMessage);
            mActiveMessage = null;
        }
    }

    private Fragment getOnScreenFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    public void onGroupShare(Business business) {
        String businessData = getString(R.string.share_food_truck_content,business.getUrl());
        MessagePayload payload = mNearByFragment
                                    .getMessagePayload(businessData);
        changeFragment(R.id.navigation_group);
        mBottomNavigationView.setSelectedItemId(R.id.navigation_group);
        //crashing when called directly
        new Handler().postDelayed(() -> {
            ((NearByFragment)getOnScreenFragment()).addMessagePayload(payload);
            publish(payload);
        },1000);

    }

    @Override
    public void onSeeAllBtnClick() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                FoodTruckFeedFragment
                        .newInstance(null, QueryPreferences.getCurrentLocation(this))
                ,R.id.content,R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onHomeButtonClick() {
        changeFragment(R.id.navigation_home);
    }

    @Override
    public void onBackPressed() {
        if(getOnScreenFragment() instanceof HomeFeedFragment){
            super.onBackPressed();
        }else{
            changeFragment(R.id.navigation_home);
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }

    }

    private void setToolbarTitle(){
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }
}
