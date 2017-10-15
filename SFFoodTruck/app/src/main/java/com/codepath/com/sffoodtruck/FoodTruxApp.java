package com.codepath.com.sffoodtruck;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by akshaymathur on 10/12/17.
 */

public class FoodTruxApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
