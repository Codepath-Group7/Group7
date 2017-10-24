package com.codepath.com.sffoodtruck.ui.businessdetail;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/14/17.
 * Modified by saip
 */

public class BusinessDetailPresenter extends AbstractPresenter<BusinessDetailContract.MvpView>
        implements BusinessDetailContract.Presenter {

    public static final String TAG = BusinessDetailPresenter.class.getSimpleName();
    private final String token;
    private static String sBusinessId;
    private static Business sBusiness;
    private DatabaseReference mDatabaseReference;
    private static final int DELAYED_CONST = 5000;

    public BusinessDetailPresenter(String token){
        this.token = token;
    }

    @Override
    public void initialLoad(Business business) {
        sBusinessId = business.getId();
        sBusiness = business;
        sBusiness.setTimestamp(String.valueOf(System.currentTimeMillis()));
        getView().renderBusinessDetail();
        loadBusiness();
        loadReviews();
        checkIsFavorite();
        new Handler().postDelayed(() -> {
            if(getView().isAttached()){
                uploadBusinessDetail();
            }
        },DELAYED_CONST);

    }

    @Override
    public void uploadBusinessDetail() {
        mDatabaseReference = FirebaseUtils.getPreviousTripsDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Business business = snapshot.getValue(Business.class);
                            if(business != null && business.getId().equals(sBusinessId)){
                                mDatabaseReference = mDatabaseReference.child(snapshot.getKey());
                                Log.d(TAG,"Got the duplicate: " + business.getTimestamp());
                            }else{
                                mDatabaseReference = mDatabaseReference
                                        .child(sBusiness.getTimestamp());
                            }
                            mDatabaseReference.setValue(sBusiness)
                                    .addOnCompleteListener(task -> {
                                        Log.d(TAG,"uploading data to server into previousTrips");
                                        Log.d(TAG,"Task of uploading business detail: "
                                                + task.isSuccessful());
                                    });
                        }

                    }else{
                        mDatabaseReference.child(sBusiness.getTimestamp())
                                .setValue(sBusiness);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    @Override
    public void loadBusiness() {

        if (TextUtils.isEmpty(sBusinessId)) return;

        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, token);
        Call<Business> callResults = services.getBusiness(sBusinessId);
        callResults.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(@NonNull Call<Business> call,
                                   @NonNull Response<Business> response) {
                Business businessDetails = response.body();
                if (businessDetails == null) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                getView().renderBusiness(businessDetails);
            }

            @Override
            public void onFailure(@NonNull Call<Business> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });
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

    @Override
    public void loadReviews() {
        if (TextUtils.isEmpty(sBusinessId)) return;

        final SearchApi services = RetrofitClient
                .createService(SearchApi.class, token);
        Call<ReviewsResponse> callResults = services.getBusinessReviews(sBusinessId);
        callResults.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call,
                                   @NonNull Response<ReviewsResponse> response) {
                ReviewsResponse reviewsResponse = response.body();
                if (reviewsResponse == null) {
                    Log.w(TAG, "response has failed " + response.code());
                    return;
                }

                getView().renderReviews(reviewsResponse.getReviews());
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed", t);
            }
        });
    }

    @Override
    public void submitReviewToFirebase(Review review) {
        mDatabaseReference = FirebaseUtils
                .getBusinessDatabaseReviewsRef(sBusinessId);
        DatabaseReference userReviewRef = FirebaseUtils
                .getCurrentUserReviewDatabaseRef();
        mDatabaseReference.push().setValue(review);
        if(userReviewRef != null)
            userReviewRef.child(sBusinessId).setValue(sBusiness);

    }

    @Override
    public void fetchReviewsFromFirebase() {
        mDatabaseReference = FirebaseUtils
                .getBusinessDatabaseReviewsRef(sBusinessId);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
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

    @Override
    public void addToFavorites() {
        boolean isFavorite = sBusiness.isFavorite();
        mDatabaseReference = FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(isFavorite){
            sBusiness.setFavorite(false);
            mDatabaseReference.child(sBusinessId).removeValue();
        }else{
            sBusiness.setFavorite(true);
            mDatabaseReference.child(sBusinessId).setValue(sBusiness);
        }
        getView().showAsFavorite(sBusiness.isFavorite());
    }

    @Override
    public void checkIsFavorite() {
        mDatabaseReference = FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.child(sBusinessId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Business business = dataSnapshot.getValue(Business.class);
                    if(business == null){
                        getView().showAsFavorite(false);
                        return;
                    }
                    getView().showAsFavorite(business.isFavorite());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG,"Database error for checking favorite" + databaseError.getDetails());
                }
            });
        }

    }
}
