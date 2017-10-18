package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.data.model.ReviewsResponse;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/14/17.
 */

public class BusinessDetailPresenter extends AbstractPresenter<BusinessDetailContract.MvpView> implements BusinessDetailContract.Presenter {

    public static final String TAG = BusinessDetailPresenter.class.getSimpleName();
    @Override
    public void loadBusiness(Context context, String businessId) {

        if (TextUtils.isEmpty(businessId)) return;

        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, context);
        Call<Business> callResults = services.getBusiness(businessId);
        callResults.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call,
                                   Response<Business> response) {
                Business businessDetails = response.body();
                if (businessDetails == null) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                getView().renderBusiness(businessDetails);
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });
    }

    @Override
    public void fetchPhotosFromFirebase(String businessId) {
        DatabaseReference databaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(businessId);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String photoName = dataSnapshot.getValue(String.class);
                Log.d(TAG,"Firebase photos path--> " + photoName);
                getView().addPhotoToAdapter(photoName);
                //mAdapter.addPhoto(photoName);
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
    public void uploadPhotoToStorage(Uri photoUri, final String businessId) {
        StorageReference photoRef = FirebaseUtils.getBusinessPhotoReference(photoUri.getLastPathSegment());
        UploadTask mUploadTask = photoRef.putFile(photoUri);
        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG,"File upload failed");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.
                        getTotalByteCount();
                Log.d(TAG,"Upload is " + progress + "% done");
                //progressDialog.setProgress((int) progress);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG,"File upload successfully");
                DatabaseReference databaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(businessId);
                databaseReference.push().setValue(taskSnapshot.getDownloadUrl().toString());
                //progressDialog.dismiss();
            }
        });
    }

    @Override
    public void loadReviews(Context context, String businessId) {
        if (TextUtils.isEmpty(businessId)) return;

        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, context);
        Call<ReviewsResponse> callResults = services.getBusinessReviews(businessId);
        callResults.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call,
                                   Response<ReviewsResponse> response) {
                ReviewsResponse reviewsResponse = response.body();
                if (reviewsResponse == null) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                getView().renderReviews(reviewsResponse.getReviews());
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });
    }

    @Override
    public void submitReviewToFirebase(String businessId, Review review) {
        DatabaseReference  databaseReference = FirebaseUtils.getBusinessDatabaseReviewsRef(businessId);
        databaseReference.push().setValue(review);
    }

    @Override
    public void fetchReviewsFromFirebase(String businessId) {
        DatabaseReference  databaseReference = FirebaseUtils.getBusinessDatabaseReviewsRef(businessId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Review review = dataSnapshot.getValue(Review.class);
                getView().addReviewToAdapter(review);
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
