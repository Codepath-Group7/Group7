package com.codepath.com.sffoodtruck.ui.homefeed;

import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by saip92 on 10/30/2017.
 */

class HomeFeedPresenter extends AbstractPresenter<HomeFeedContract.MvpView> implements
 HomeFeedContract.Presenter{


    public HomeFeedPresenter(){}

    private static final String TAG = HomeFeedPresenter.class.getSimpleName();

    @Override
    public void initialLoad(String query) {
        loadFavorites();


    }

    @Override
    public void loadFoodTruckFeed(String location) {

    }

    @Override
    public void loadFavorites() {
        DatabaseReference databaseReference =  FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(databaseReference != null){
            databaseReference
                    .orderByChild("timestamp")
                    .limitToLast(5)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedList<Business> businesses = new LinkedList<>();
                            for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                                Business business = businessSnapshot.getValue(Business.class);
                                if(business != null){
                                    Log.d(TAG,"Favorite's list :" + business.getName());
                                    businesses.addLast(business);
                                }
                            }
                            getView().addFavoritesFoodTruckList(businesses);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void updateLocation(String location) {

    }

}
