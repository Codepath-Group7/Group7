package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.net.Uri;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBasePresenter;
import com.codepath.com.sffoodtruck.ui.base.mvp.MvpBaseView;

import java.util.List;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessPhotosContract {

    interface MvpView extends MvpBaseView {
        void renderPhotos(List<String> photoList);
        void addPhotoToAdapter(String photo);
    }

    interface Presenter extends MvpBasePresenter<MvpView> {
        void initialLoad(Business business);
        void fetchPhotosFromFirebase();
        void uploadPhotoToStorage(Uri photoUri);
    }
}
