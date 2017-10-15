package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;

public class BusinessDetailActivity extends AppCompatActivity{


    private static final String EXTRA_BUSINESS =
            "com.codepath.com.sffoodtruck.ui.businessdetail.EXTRA_BUSINESS";


    public static Intent newIntent(Context context, Business business){
        Intent intent = new Intent(context, BusinessDetailActivity.class);
        intent.putExtra(EXTRA_BUSINESS, business);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        initialize();
    }

    private void initialize() {
        Business business = getIntent().getParcelableExtra(EXTRA_BUSINESS);
        if(business == null) return;

        Fragment fragment = BusinessDetailFragment.newInstance(business);
        FragmentManager fm = getSupportFragmentManager();
        if (fragment.isAdded()) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .add(R.id.business_detail_container, fragment)
                    .commit();
        }
    }

}
