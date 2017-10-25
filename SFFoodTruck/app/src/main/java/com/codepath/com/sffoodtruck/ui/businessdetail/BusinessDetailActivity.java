package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.databinding.ActivityBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpActivity;
import com.codepath.com.sffoodtruck.ui.businessdetail.photos.BusinessPhotosFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.photos.TakePhotoDialogFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.reviews.BusinessReviewsFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.reviews.SubmitReviewDialogFragment;
import com.squareup.picasso.Picasso;

public class BusinessDetailActivity extends AbstractMvpActivity<BusinessActivityContract.MvpView
        , BusinessActivityContract.Presenter> implements BusinessActivityContract.MvpView,
        TakePhotoDialogFragment.TakePhotoListner, SubmitReviewDialogFragment.SubmitReviewListener {


    private static final String EXTRA_BUSINESS =
            "com.codepath.com.sffoodtruck.ui.businessdetail.EXTRA_BUSINESS";
    private static final String TAG = BusinessDetailActivity.class.getSimpleName();
    private ActivityBusinessDetailBinding mBinding;
    private Business mBusiness;
    private MenuItem mFavoriteItem;
    private BusinessDetailPagerAdapter mViewPagerAdapter;

    public static Intent newIntent(Context context, Business business){
        Intent intent = new Intent(context, BusinessDetailActivity.class);
        intent.putExtra(EXTRA_BUSINESS, business);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_business_detail);
        mBusiness = getIntent().getParcelableExtra(EXTRA_BUSINESS);
        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar  actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBusiness.getName());
        }
        Picasso.with(this).load(mBusiness.getImageUrl()).fit().into(mBinding.headerImage);
        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(BusinessDetailActivity.this,R.drawable.ic_favorite_white_24px));
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
            case android.R.id.home:
                finish();
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
        setupViewPager(data);
        setupFabButton();
    }
    private void setupViewPager(Business business){
        mViewPagerAdapter = new BusinessDetailPagerAdapter(getSupportFragmentManager(),
                this,business);
        mBinding.tabViewpager.setAdapter(mViewPagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.tabViewpager);
        mBinding.tabViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(BusinessDetailActivity.this,R.drawable.ic_favorite_white_24px));
                        break;
                    case 1:
                        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(BusinessDetailActivity.this,R.drawable.ic_add_a_photo_black_24dp));
                        break;
                    case 2:
                        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(BusinessDetailActivity.this,R.drawable.ic_rate_review_black_24dp));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        mBinding.fab.show();
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mBinding.fab.hide();
                        break;
                }

            }
        });
    }

    private void setupFabButton() {
        mBinding.fab.setOnClickListener(view -> handleFabButtonClick());
    }

    private void handleFabButtonClick() {
        int currentTab = mBinding.tabViewpager.getCurrentItem();
        switch (currentTab){
            case 0:
                getPresenter().addToFavorites();
                break;
            case 1:
                openTakePhotoDialog();
                break;
            case 2:
                openSubmitReviewDialog();
                break;
        }
    }

    public void openSubmitReviewDialog() {
        DialogFragment fragment = new SubmitReviewDialogFragment();
        fragment.show(getSupportFragmentManager(), SubmitReviewDialogFragment.TAG);
    }

    public void openTakePhotoDialog() {
        DialogFragment fragment = new TakePhotoDialogFragment();
        fragment.show(getSupportFragmentManager(), TakePhotoDialogFragment.TAG);
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

    @Override
    public boolean isAttached() {
        return true;
    }

    @Override
    public void OnPhotoSubmit(Uri photoPath) {
        if(photoPath!=null && !TextUtils.isEmpty(photoPath.toString())) getPresenter().uploadPhotoToStorage(photoPath);
    }

    @Override
    public void onReviewSubmit(Review review) {
        Log.d(TAG, "From review dialog fragment " + review.getText());
        getPresenter().submitReviewToFirebase(review);
    }
}
