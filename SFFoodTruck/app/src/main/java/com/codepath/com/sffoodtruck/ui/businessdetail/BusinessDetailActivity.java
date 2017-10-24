package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.ActivityBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpActivity;
import com.squareup.picasso.Picasso;

public class BusinessDetailActivity extends AbstractMvpActivity<BusinessActivityContract.MvpView
        , BusinessActivityContract.Presenter> implements BusinessActivityContract.MvpView {


    private static final String EXTRA_BUSINESS =
            "com.codepath.com.sffoodtruck.ui.businessdetail.EXTRA_BUSINESS";
    private ActivityBusinessDetailBinding mBinding;
    private Business mBusiness;
    private MenuItem mFavoriteItem;

    public static Intent newIntent(Context context, Business business){
        Intent intent = new Intent(context, BusinessDetailActivity.class);
        intent.putExtra(EXTRA_BUSINESS, business);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_business_detail);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_business_detail);
        mBusiness = getIntent().getParcelableExtra(EXTRA_BUSINESS);
        //initialize();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mBinding.toolbar);
        ActionBar  actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBusiness.getName());
        }

        Picasso.with(this).load(mBusiness.getImageUrl()).fit().into(mBinding.headerImage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().initialLoad(mBusiness);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_business_detail,menu);
        mFavoriteItem = menu.findItem(R.id.action_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_favorite:
                getPresenter().addToFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public BusinessActivityContract.Presenter createPresenter() {
        return new BusinessActivityPresenter(QueryPreferences.getAccessToken(this));
    }

    @Override
    public void renderBusiness(Business data) {
        mBinding.tabViewpager.setAdapter(new BusinessDetailPagerAdapter(getSupportFragmentManager(),
                this,data));
        mBinding.tabLayout.setupWithViewPager(mBinding.tabViewpager);
    }

    @Override
    public void showAsFavorite(boolean isFavorite) {
        if(mFavoriteItem == null) return;
        if(!isFavorite){
            mFavoriteItem.setIcon(ContextCompat.getDrawable(this,
                    R.drawable.ic_favorite_border_white_24px));
        }else{
            mFavoriteItem.setIcon(ContextCompat.getDrawable(this,
                    R.drawable.ic_favorite_white_24px));
        }
    }

    /*private void initialize() {
        Business business = getIntent().getParcelableExtra(EXTRA_BUSINESS);
        if(business == null) return;

        Fragment fragment = BusinessDetailFragment.newInstance(business);
        FragmentManager fm = getSupportFragmentManager();
        if (fragment.isAdded()) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .add(R.id.business_detail_container, fragment)
                    .commit();
        }
    }*/

    @Override
    public boolean isAttached() {
        return true;
    }
}
