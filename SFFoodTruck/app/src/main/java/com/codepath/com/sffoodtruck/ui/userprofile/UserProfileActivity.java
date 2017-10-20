package com.codepath.com.sffoodtruck.ui.userprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.util.ActivityUtils;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                new UserProfileFragment(),R.id.fragment_container);
    }
}
