package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.net.Uri;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessPhotosPresenter extends AbstractPresenter<BusinessPhotosContract.MvpView>
        implements BusinessPhotosContract.Presenter  {
    public static final String TAG = BusinessPhotosPresenter.class.getSimpleName();
    private final String token;
    private static String sBusinessId;
    private static Business sBusiness;
    private DatabaseReference mDatabaseReference;

    public BusinessPhotosPresenter(String token){
        this.token = token;
    }
    @Override
    public void initialLoad(Business business) {
        sBusiness = business;
        sBusinessId = business.getId();
        loadYelpPhotos();
    }
    private void loadYelpPhotos(){
        getView().renderPhotos(sBusiness.getPhotos());
    }
    @Override
    public void fetchPhotosFromFirebase() {
        mDatabaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(sBusinessId);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String photoName = dataSnapshot.getValue(String.class);
                Log.d(TAG,"Firebase photos path--> " + photoName);
                getView().addPhotoToAdapter(photoName);
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

    @Override
    public void uploadPhotoToStorage(Uri photoUri) {
        StorageReference photoRef = FirebaseUtils
                .getBusinessPhotoReference(photoUri.getLastPathSegment());
        UploadTask mUploadTask = photoRef.putFile(photoUri);
        mUploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Log.e(TAG,"File upload failed");
        }).addOnProgressListener(taskSnapshot -> {
            @SuppressWarnings("VisibleForTests")
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.
                    getTotalByteCount();
            Log.d(TAG,"Upload is " + progress + "% done");
            //progressDialog.setProgress((int) progress);

        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Log.d(TAG,"File upload successfully");
            DatabaseReference databaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(sBusinessId);
            if(taskSnapshot.getDownloadUrl()!= null)
                databaseReference.push().setValue(taskSnapshot.getDownloadUrl().toString());
            //progressDialog.dismiss();
        });
    }
}
