package com.codepath.com.sffoodtruck.data.local;

import android.content.Context;
import android.preference.PreferenceManager;

import com.codepath.com.sffoodtruck.R;

/**
 * Created by saip92 on 10/11/2017.
 */

public class QueryPreferences {

    private static final String PREF_KEY_AUTHORIZATION = "PREF_KEY_AUTHORIZATION";


    public static void storeAccessToken(Context context, String accessToken, String tokenType){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_KEY_AUTHORIZATION,tokenType + " " + accessToken)
                .apply();
    }


    public static String getAccessToken(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_KEY_AUTHORIZATION, "");
    }


    public static void storeLocationPref(Context context , String locationAddr){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_location_picker_key),locationAddr)
                .apply();
    }

    public static String getLocationPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_location_picker_key),"");
    }
}
