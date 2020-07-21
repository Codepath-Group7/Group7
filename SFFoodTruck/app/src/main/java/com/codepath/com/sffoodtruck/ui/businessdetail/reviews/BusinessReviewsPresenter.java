package com.codepath.com.sffoodtruck.ui.businessdetail.reviews;

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

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/23/17.
 */

public class BusinessReviewsPresenter extends AbstractPresenter<BusinessReviewsContract.MvpView>
        implements BusinessReviewsContract.Presenter {

    public static final String TAG = BusinessReviewsPresenter.class.getSimpleName();
    private final String token;
    private static String sBusinessId;
    private static Business sBusiness;
    private DatabaseReference mDatabaseReference;

    public BusinessReviewsPresenter(String token){
        this.token = token;
    }
    @Override
    public void initialLoad(Business business) {
        sBusiness = business;
        sBusinessId = business.getId();
        loadReviews();
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
}
