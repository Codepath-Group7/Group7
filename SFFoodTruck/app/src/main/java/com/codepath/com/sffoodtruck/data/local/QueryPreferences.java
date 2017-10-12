package com.codepath.com.sffoodtruck.data.local;

import android.content.Context;
import android.preference.PreferenceManager;

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
}
