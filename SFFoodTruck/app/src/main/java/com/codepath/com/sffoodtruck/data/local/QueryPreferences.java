package com.codepath.com.sffoodtruck.data.local;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.CustomPlace;
import com.codepath.com.sffoodtruck.ui.util.JsonUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;

import androidx.annotation.Nullable;

/**
 * Created by saip92 on 10/11/2017.
 */

public class QueryPreferences {
    private static final String TAG = QueryPreferences.class.getSimpleName();

    public static final String PREF_FCM_TOKEN = "PREF_FCM_TOKEN";
    public static final String PREF_CURRENT_LOCATION = "PREF_CURRENT_LOCATION";
    private static final String PREF_PLACE = "PREF_PLACE";

    public static void storeString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getStoredString(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, "");
    }


    public static void storeLocationPref(Context context , String locationAddr){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_location_picker_key),locationAddr)
                .apply();

    }

    public static String getLocationPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_location_picker_key),null);
    }

    public static void storeCurrentLocation(Context context, String location){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CURRENT_LOCATION,location)
                .apply();
    }

    public static String getCurrentLocation(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CURRENT_LOCATION,null);
    }

    public static void storePlacePref(Context context , Place place){
        String jsonStr = JsonUtils.toJson(place);
        Log.d(TAG,"place string: " + jsonStr);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_PLACE, jsonStr)
                .apply();
    }


    public static void storeCustomPlacePref(Context context , CustomPlace place){
        String jsonStr = JsonUtils.toJson(place);
        Log.d(TAG,"place string: " + jsonStr);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_PLACE, jsonStr)
                .apply();
    }

    @Nullable
    public static Place getPlacePref(Context context){
        String jsonStr = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_PLACE, "");
        if (TextUtils.isEmpty(jsonStr)) return null;

        try {
            return JsonUtils.fromJson(jsonStr, PlaceEntity.class);
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }
        return null;
    }

    @Nullable
    public static CustomPlace getCustomPlacePref(Context context){
        String jsonStr = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_PLACE, "");
        if (TextUtils.isEmpty(jsonStr)) return null;

        try {
            return JsonUtils.fromJson(jsonStr, CustomPlace.class);
        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }
        return null;
    }
}
