package com.codepath.com.sffoodtruck.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by saip92 on 10/13/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final int PLACE_PICKER_REQUEST_CODE = 12;
    private static final String TAG = SettingsFragment.class.getSimpleName();
    Preference locationPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_profile);
        setupLocationPicker();
    }

    private void setupLocationPicker() {
        locationPref = findPreference(getString(R.string.pref_location_picker_key));
        if(locationPref != null){
            locationPref.setOnPreferenceClickListener(preference -> {
                String key = preference.getKey();
                if(key.equals(getString(R.string.pref_location_picker_key))){
                    openLocationPicker();
                }
                return true;
            });
        }
    }

    private void openLocationPicker(){
        int connectionResult = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(getActivity());
        if( connectionResult == ConnectionResult.SUCCESS){
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"Google play services is: " + connectionResult);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Place place = PlacePicker.getPlace(getActivity(),data);
                locationPref.setSummary(place.getAddress());
            }
        }
    }
}