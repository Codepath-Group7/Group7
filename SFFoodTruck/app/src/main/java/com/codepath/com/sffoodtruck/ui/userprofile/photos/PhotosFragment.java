package com.codepath.com.sffoodtruck.ui.userprofile.photos;

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

    private StaggeredGridLayoutManager mLayoutManager;
    private PhotosAdapter mPhotosAdapter;

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
        return null;
    }
}
