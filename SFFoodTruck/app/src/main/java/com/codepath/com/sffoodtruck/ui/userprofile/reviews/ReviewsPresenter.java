package com.codepath.com.sffoodtruck.ui.userprofile.reviews;

import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileAbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by saip92 on 10/26/2017.
 */

class ReviewsPresenter extends UserProfileAbstractPresenter<ReviewsContract.View>
    implements ReviewsContract.Presenter{

    private static final String TAG = ReviewsPresenter.class.getSimpleName();

    @Override
    public void initialLoad() {
        loadUserReviews();
        getView().updateUI();
    }

    @Override
    public void loadUserReviews() {

        DatabaseReference reviewsRef = FirebaseUtils.getCurrentUserReviewDatabaseRef();
        if(reviewsRef != null){

            reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedList<Review> reviews = new LinkedList<>();
                            for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                                Review review = businessSnapshot.getValue(Review.class);
                                if(review != null){
                                    //Log.d(TAG,"Review's list :" + review.getUser().getName());
                                    reviews.addFirst(review);
                                }
                            }
                            getView().showUserReviews(reviews);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }


    }
}
