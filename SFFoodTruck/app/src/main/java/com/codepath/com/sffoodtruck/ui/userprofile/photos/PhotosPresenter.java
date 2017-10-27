package com.codepath.com.sffoodtruck.ui.userprofile.photos;

import com.codepath.com.sffoodtruck.data.model.UserPostedPhoto;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileAbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by saip92 on 10/26/2017.
 */

public class PhotosPresenter extends UserProfileAbstractPresenter<PhotosContract.View> implements
        PhotosContract.Presenter {

    private static final String TAG = PhotosPresenter.class.getSimpleName();

    @Override
    public void initialLoad() {
        loadPhotos();
        getView().updateUI();
    }

    @Override
    public void loadPhotos() {
        DatabaseReference photoRef = FirebaseUtils.getCurrentUserPhotoDatabaseRef();
        if(photoRef != null)
            photoRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserPostedPhoto postedPhoto = dataSnapshot.getValue(UserPostedPhoto.class);
                    if(postedPhoto != null){
                        getView().showPhoto(postedPhoto);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
