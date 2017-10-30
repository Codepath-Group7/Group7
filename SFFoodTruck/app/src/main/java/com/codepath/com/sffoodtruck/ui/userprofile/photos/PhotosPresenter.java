package com.codepath.com.sffoodtruck.ui.userprofile.photos;

import com.codepath.com.sffoodtruck.data.model.UserPostedPhoto;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileAbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

public class PhotosPresenter extends UserProfileAbstractPresenter<PhotosContract.View> implements
        PhotosContract.Presenter {

    private static final String TAG = PhotosPresenter.class.getSimpleName();

    @Override
    public void initialLoad() {
        loadPhotos(getView().getCurrentUserId());
        getView().updateUI();
    }

    @Override
    public void loadPhotos(String userId) {
        DatabaseReference photoRef = userId != null ?
                FirebaseUtils.getPhotoDatabaseRef(userId) :
                FirebaseUtils.getCurrentUserPhotoDatabaseRef();
        if(photoRef != null){
            photoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getView().showProgressBar(false);
                    if(dataSnapshot.getChildrenCount() > 0){
                        List<UserPostedPhoto> photos = new LinkedList<>();
                        for(DataSnapshot photoSnapshot : dataSnapshot.getChildren()){
                            UserPostedPhoto postedPhoto = photoSnapshot
                                    .getValue(UserPostedPhoto.class);
                            if(postedPhoto != null){
                                photos.add(postedPhoto);
                            }
                        }

                        getView().showPhotos(photos);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
