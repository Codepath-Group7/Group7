package com.codepath.com.sffoodtruck.ui.userprofile.photos;



import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.UserPostedPhoto;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class PhotosAdapter extends UserProfileBaseAdapter<UserPostedPhoto> {

    List<UserPostedPhoto> mUserPostedPhotos;

    public PhotosAdapter(List<UserPostedPhoto> list) {
        super(list, R.layout.userprofile_photo_item);
        mUserPostedPhotos = list;
    }

    void addPhoto(UserPostedPhoto userPostedPhoto){
        mUserPostedPhotos.add(userPostedPhoto);
        notifyItemInserted(mUserPostedPhotos.size());
    }


}
