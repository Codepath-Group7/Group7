package com.codepath.com.sffoodtruck.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.CustomPlace;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.ResponseHandler;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by robl2e on 10/13/17.
 */

public class FoodTruckMapPresenter extends AbstractPresenter<FoodTruckMapContract.MvpView>
        implements FoodTruckMapContract.Presenter {
    private static final String TAG = FoodTruckMapPresenter.class.getSimpleName();
    private static final String FOODTRUCK = "foodtrucks";
    private static final long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private static final long FASTEST_INTERVAL = 2000; /* 2 sec */

    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private SearchApi searchApi;
    private CustomPlace placePreference;

    public FoodTruckMapPresenter(SearchApi searchApi
            , FusedLocationProviderClient client, Geocoder geocoder, CustomPlace placePreference) {
        this.searchApi = searchApi;
        this.client = client;
        this.geocoder = geocoder;
        this.placePreference = placePreference;
    }

    @Override
    public void loadFoodTrucks() {
        loadFoodTrucks(false);
    }

    @Override
    public void loadFoodTrucks(boolean useCurrentLocation) {
        if (!useCurrentLocation && placePreference != null && placePreference.getLatitude() != 0.0) {
          /*  LatLng latLng = placePreference.getLatLng();*/
            Location location = new Location("Place");
            location.setLatitude(placePreference.getLatitude());
            location.setLongitude(placePreference.getLongitude());
            getView().renderZoomToLocation(location);

            String locationString = findLocation(location);
            searchForFoodTrucks(locationString);
        } else {
            getLocation(new ResponseHandler<Location>() {
                @Override
                public void onComplete(Location location) {
                    if (location != null) {
                        getView().renderZoomToLocation(location);
                    }
                    String locationString = findLocation(location);
                    searchForFoodTrucks(locationString);
                }

                @Override
                public void onFailed(String message, Exception exception) {
                    Log.e(TAG, Log.getStackTraceString(exception));
                }
            });
        }
    }

    private void searchForFoodTrucks(String location) {
        if (TextUtils.isEmpty(location)) return;

        Call<SearchResults> callResults = searchApi.getSearchResults(location, FOODTRUCK, 0);
        callResults.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call,
                                   Response<SearchResults> response) {
                SearchResults searchResults = response.body();
                if (searchResults == null || searchResults.getBusinesses() == null
                        || searchResults.getBusinesses().isEmpty()) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                List<Business> businesses = searchResults.getBusinesses();
                List<FoodTruckMapViewModel> viewModels = FoodTruckMapViewModel.convert(businesses);
                getView().renderFoodTrucks(viewModels);
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });
    }

    private void getLocation(final ResponseHandler<Location> responseHandler) {
        getLastLocation(client, new ResponseHandler<Location>() {
            @Override
            public void onComplete(Location location) {
                if (location == null) {
                    //last location null
                    getCurrentLocation(client, responseHandler);
                } else {
                    if (responseHandler != null) responseHandler.onComplete(location);
                }
            }

            @Override
            public void onFailed(String message, Exception exception) {
                if (responseHandler != null) responseHandler.onFailed(message, exception);
            }
        });
    }

    private void getLastLocation(FusedLocationProviderClient client, final ResponseHandler<Location> responseHandler) {
        //noinspection MissingPermission
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnFailureListener(e -> {
            if (responseHandler != null) responseHandler.onFailed(e.getMessage(), e);
        });
        locationTask.addOnCompleteListener(task -> {
            if (responseHandler != null) responseHandler.onComplete(task.getResult());
        });
    }

    private void getCurrentLocation(final FusedLocationProviderClient client, final ResponseHandler<Location> responseHandler) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setNumUpdates(1); // only need one location

        //noinspection MissingPermission
        client.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d(TAG, "getCurrentLocation() - onComplete");
                Location location = locationResult.getLastLocation();
                if (responseHandler != null) responseHandler.onComplete(location);
            }
        }, null).addOnFailureListener(e -> {
            if (responseHandler != null) responseHandler.onFailed(e.getMessage(), e);
        });
    }

    /**
     * Find location given lat and long
     *
     * @param location
     * @return
     */
    @Nullable
    private String findLocation(Location location) {
        if (location == null) return null;

        // lat,lng, your current location
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.isEmpty()) return null;

            return addresses.get(0).getPostalCode();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
