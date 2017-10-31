package com.codepath.com.sffoodtruck.ui.userprofile.photos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.codepath.com.sffoodtruck.data.model.UserPostedPhoto;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseFragment;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class PhotosFragment extends UserProfileBaseFragment implements
    PhotosContract.View{

    private static final String TAG = PhotosFragment.class.getSimpleName();
    private static final String EXTRA_USERID = "PhotosFragment.EXTRA_USERID";

    private StaggeredGridLayoutManager mLayoutManager;
    private PhotosAdapter mPhotosAdapter;
    private String mUserId = null;


    public static Fragment newInstance(String userId){
        Bundle args = new Bundle();
        args.putString(EXTRA_USERID,userId);
        PhotosFragment photosFragment = new PhotosFragment();
        photosFragment.setArguments(args);
        return photosFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mUserId = getArguments().getString(EXTRA_USERID);
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        return mLayoutManager;
    }

    @Override
    protected UserProfileBaseAdapter getUserProfileBaseAdapter() {
        mPhotosAdapter = new PhotosAdapter(new ArrayList<>());
        return mPhotosAdapter;
    }

    @Override
    public UserProfileBasePresenter createPresenter() {
        return new PhotosPresenter();
    }

    @Override
    public void showPhoto(UserPostedPhoto userPostedPhoto) {
        mPhotosAdapter.addPhoto(userPostedPhoto);
    }

    @Override
    public void showPhotos(List<UserPostedPhoto> photos) {
        mPhotosAdapter.addAll(photos);
    }

    @Override
    public String getCurrentUserId() {
        return mUserId;
    }
}
