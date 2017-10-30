package com.codepath.com.sffoodtruck.ui.userprofile.photos;

import com.codepath.com.sffoodtruck.data.model.UserPostedPhoto;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBasePresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class PhotosContract {

    interface View extends UserProfileBaseView{
        void showPhoto(UserPostedPhoto userPostedPhoto);
        void showPhotos(List<UserPostedPhoto> photos);
    }


    interface Presenter extends UserProfileBasePresenter<View>{
        void loadPhotos(String userId);
    }
}
