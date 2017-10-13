package com.codepath.com.sffoodtruck.ui.map;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.com.sffoodtruck.R;

public class FoodTruckMapActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, FoodTruckMapActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_map);
        initialize();
    }

    private void initialize() {
        FoodTruckMapFragment fragment = FoodTruckMapFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        if (fragment.isAdded()) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .add(R.id.layout_content, fragment)
                    .commit();
        }
    }
}
