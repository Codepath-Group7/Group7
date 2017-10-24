package com.codepath.com.sffoodtruck.ui.businessdetail.info;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailFragment extends AbstractMvpFragment<BusinessDetailContract.MvpView
        , BusinessDetailContract.Presenter> implements BusinessDetailContract.MvpView {

    public static final String TAG = BusinessDetailFragment.class.getSimpleName();
    public static final String BUSINESS_KEY = "business_key";
    private Business mBusiness;
    private FragmentBusinessDetailBinding mBinding;

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
        mBinding.tvBusinessAddress.setText(data.getLocation().getCompleteAddress());
        mBinding.tvBusinessDesc.setText(data.getAllCategories());
        mBinding.tvBusinessPhone.setText(data.getDisplayPhone());
        mBinding.rbFoodTruckRating.setRating(data.getRating());
        mBinding.tvPrice.setText(data.getPrice());
        if(data.getHours()!=null && data.getHours().size()>0){
            mBinding.tvBusinessHrs.setText(data.getHours().get(0).getTodaysHours());
        }
    }
}
