package com.codepath.com.sffoodtruck.ui.nearby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.util.ActivityUtils;

public class NearByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                new NearByFragment(),R.id.fragment_container);
    }
}
