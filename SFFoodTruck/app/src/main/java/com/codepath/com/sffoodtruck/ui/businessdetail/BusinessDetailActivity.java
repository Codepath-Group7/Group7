package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.databinding.ActivityBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpActivity;
import com.codepath.com.sffoodtruck.ui.businessdetail.photos.TakePhotoDialogFragment;
import com.codepath.com.sffoodtruck.ui.businessdetail.reviews.SubmitReviewDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri mCurrentPhotoPath = null;
    private MaterialDialog mProgressDialog;

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
        getPresenter().initialLoad(mBusiness);
    }

    private void setToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar  actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mBusiness.getName());
        }
        Picasso.with(this).load(mBusiness.getImageUrl()).fit().into(mBinding.headerImage);
        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(
                BusinessDetailActivity.this,R.drawable.ic_favorite_white_24px));
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                //getPresenter().addToFavorites();
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
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
                        getPresenter().checkIsFavorite();
                        break;
                    case 1:
                        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(
                                BusinessDetailActivity.this,R.drawable.ic_add_a_photo_black_24dp));
                        break;
                    case 2:
                        mBinding.fab.setImageDrawable(ContextCompat.getDrawable(
                                BusinessDetailActivity.this,R.drawable.ic_rate_review_black_24dp));
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
                dispatchTakePictureIntent();
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
        DialogFragment fragment = TakePhotoDialogFragment.newInstance(mCurrentPhotoPath);
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
        if(mBinding.tabViewpager.getCurrentItem() == 0){
            if(!isFavorite){
                mBinding.fab.setImageDrawable(ContextCompat.getDrawable(
                        BusinessDetailActivity.this,R.drawable.ic_favorite_border_white_24px));
            }else{
                mBinding.fab.setImageDrawable(ContextCompat.getDrawable(
                        BusinessDetailActivity.this,R.drawable.ic_favorite_white_24px));
            }
        }
    }

    @Override
    public boolean isAttached() {
        return true;
    }

    @Override
    public void hideToolbarProgress(){
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.label_uploading)
                .content(R.string.please_wait)
                .progress(false, 100,true).build();
        mProgressDialog.show();
    }

    @Override
    public void updateProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }

    @Override
    public void hideProgressDialog() {
        if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    @Override
    public void OnPhotoSubmit(Uri photoPath) {
        if(photoPath!=null && !TextUtils.isEmpty(photoPath.toString()))
            getPresenter().uploadPhotoToStorage(photoPath);
    }

    @Override
    public void onReviewSubmit(Review review) {
        Log.d(TAG, "From review dialog fragment " + review.getText());
        getPresenter().submitReviewToFirebase(review);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.codepath.com.sffoodtruck.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Uri.fromFile(image);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "From Camera " + mCurrentPhotoPath);
            openTakePhotoDialog();
        }
    }
}
