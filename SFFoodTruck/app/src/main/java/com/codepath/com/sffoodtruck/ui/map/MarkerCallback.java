package com.codepath.com.sffoodtruck.ui.map;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

public class MarkerCallback implements Callback {
    Marker marker = null;

    MarkerCallback(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void onError() {
        Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
    }

    @Override
    public void onSuccess() {
        if (marker != null && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }
}