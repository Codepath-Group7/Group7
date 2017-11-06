package com.codepath.com.sffoodtruck.ui.businessdetail.info;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailFragment extends AbstractMvpFragment<BusinessDetailContract.MvpView
        , BusinessDetailContract.Presenter> implements BusinessDetailContract.MvpView, OnMapReadyCallback{

    public static final String TAG = BusinessDetailFragment.class.getSimpleName();
    public static final String BUSINESS_KEY = "business_key";
    private Business mBusiness;
    private FragmentBusinessDetailBinding mBinding;
    private GoogleMap mMap;
    //private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private LatLng mBusinessCoordinates;
    public BusinessDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public BusinessDetailContract.Presenter createPresenter() {
        return new BusinessDetailPresenter(QueryPreferences.getAccessToken(getActivity()));
    }

    public static Fragment newInstance(Business business) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUSINESS_KEY, business);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusiness = getArguments().getParcelable(BUSINESS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_detail, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().initialLoad(mBusiness);
    }

    @Override
    public void renderBusiness(Business data) {
        mBinding.tvAddress.setText(data.getLocation().getCompleteAddress());
        mBinding.tvBusinessDesc.setText(data.getAllCategories());
        mBinding.tvBusinessPhone.setText(data.getDisplayPhone());
        mBinding.rbFoodTruckRating.setRating(data.getRating());
        mBinding.ivYelpLogo.setOnClickListener(view -> openYelpWebsite(data.getUrl()));
        mBinding.tvPrice.setText(data.getPrice());
        if(data.getHours()!=null && data.getHours().size()>0){
            mBinding.tvBusinessHrs.setText(data.getHours().get(0).getTodaysHours());
        }
        mBusinessCoordinates = new LatLng(data.getCoordinates().getLatitude(),data.getCoordinates().getLongitude());
        initializeMapView();
        mBinding.llCallBusiness.setOnClickListener(view -> callBusiness(data.getPhone()));
    }

    private void openYelpWebsite(String businessUrl) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(businessUrl));
    }

    private void callBusiness(String phoneNumber) {
        String uri = "tel:" + phoneNumber.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        mMap = googleMap;
        if(mBusinessCoordinates == null) return;
        mMap.addMarker(new MarkerOptions()
                .position(mBusinessCoordinates));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mBusinessCoordinates,16f));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void initializeMapView() {
        if (mBinding.liteMap != null) {
            // Initialise the MapView
            mBinding.liteMap.onCreate(null);
            // Set the map ready callback to receive the GoogleMap object
            mBinding.liteMap.getMapAsync(this);
        }
    }
}
