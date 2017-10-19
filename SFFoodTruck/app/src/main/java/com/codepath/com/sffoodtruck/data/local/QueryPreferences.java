package com.codepath.com.sffoodtruck.data.local;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;

/**
 * Created by saip92 on 10/11/2017.
 */

public class QueryPreferences {
    private static final String TAG = QueryPreferences.class.getSimpleName();

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

    public static void storePlacePref(Context context , Place place){
        String jsonStr = JsonUtils.toJson(place);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_location_picker_place_key), jsonStr)
                .apply();
    }

    @Nullable
    public static Place getPlacePref(Context context){
        String jsonStr = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_location_picker_place_key), "");
        if (TextUtils.isEmpty(jsonStr)) return null;

        Place place = null;
        try {
            place = JsonUtils.fromJson(jsonStr, PlaceEntity.class);
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }
        return place;
    }
}
