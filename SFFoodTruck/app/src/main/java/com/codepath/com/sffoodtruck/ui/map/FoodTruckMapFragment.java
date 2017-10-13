package com.codepath.com.sffoodtruck.ui.map;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.common.ActivityRequestCodeGenerator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by robl2e on 10/11/17.
 */

public class FoodTruckMapFragment extends Fragment implements OnMapReadyCallback
        , EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_LOCATION = ActivityRequestCodeGenerator.getFreshInt();

    private MapView mapView;
    private GoogleMap map;
    private Bundle bundle;

    public static FoodTruckMapFragment newInstance() {
        FoodTruckMapFragment fragment = new FoodTruckMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_truck_map, container ,false);


        MapsInitializer.initialize(getActivity());
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(bundle);
        initializeMap();
        return view;
    }

    private void initializeMap() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            enableMapMyLocation(map);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        enableMapMyLocation(map);
    }

    private void enableMapMyLocation(GoogleMap map) {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            //noinspection MissingPermission
            map.setMyLocationEnabled(true);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}
