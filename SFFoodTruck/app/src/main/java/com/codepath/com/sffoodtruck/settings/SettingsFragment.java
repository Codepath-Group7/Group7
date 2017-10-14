package com.codepath.com.sffoodtruck.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.codepath.com.sffoodtruck.R;

/**
 * Created by saip92 on 10/13/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_profile);

    }
}
