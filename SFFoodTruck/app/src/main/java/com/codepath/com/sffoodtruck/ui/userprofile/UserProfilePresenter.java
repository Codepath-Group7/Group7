package com.codepath.com.sffoodtruck.ui.userprofile;

import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saip92 on 10/21/2017.
 */

public class UserProfilePresenter extends AbstractPresenter<UserProfileContract.MvpView>
implements UserProfileContract.Presenter{

    private static final String TAG = UserProfilePresenter.class.getSimpleName();
    private DatabaseReference mDatabaseReference;

    @Override
    public void initialLoad() {
        loadRecentVisits();
        loadFavoriteFoodTrucks();
        loadReviews();
        getView().updateUI();
    }

    @Override
    public void loadRecentVisits() {
        mDatabaseReference= FirebaseUtils.getPreviousTripsDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Business> businesses = new ArrayList<>();
                    for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                        Business business = businessSnapshot.getValue(Business.class);
                        if(business != null){
                            Log.d(TAG,"Recent visits list :" + business.getName());
                            businesses.add(business);
                        }
                    }
                    getView().showRecentVisits(businesses);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void loadFavoriteFoodTrucks() {
        mDatabaseReference= FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Business> businesses = new ArrayList<>();
                    for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                        Business business = businessSnapshot.getValue(Business.class);
                        if(business != null){
                            Log.d(TAG,"Favorite's list :" + business.getName());
                            businesses.add(business);
                        }
                    }
                    getView().showFavoriteFoodTrucks(businesses);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void loadReviews() {
        mDatabaseReference= FirebaseUtils.getCurrentUserReviewDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Business> businesses = new ArrayList<>();
                    for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                        Business business = businessSnapshot.getValue(Business.class);
                        if(business != null){
                            Log.d(TAG,"Review's list :" + business.getName());
                            businesses.add(business);
                        }
                    }
                    getView().showReviews(businesses);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
