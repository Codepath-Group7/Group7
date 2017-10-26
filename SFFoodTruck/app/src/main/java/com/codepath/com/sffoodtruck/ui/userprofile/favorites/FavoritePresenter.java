package com.codepath.com.sffoodtruck.ui.userprofile.favorites;

import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractPresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileAbstractPresenter;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseView;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by saip92 on 10/26/2017.
 */

public class FavoritePresenter extends UserProfileAbstractPresenter<FavoriteContract.View>
        implements FavoriteContract.Presenter{


    private static final String TAG = FavoritePresenter.class.getSimpleName();
    private DatabaseReference mDatabaseReference;

    public FavoritePresenter(){}

    @Override
    public void initialLoad() {
        loadFavoriteFoodTrucks();
        getView().updateUI();
    }

    @Override
    public void loadFavoriteFoodTrucks() {
        mDatabaseReference= FirebaseUtils.getCurrentUserFavoriteDatabaseRef();
        if(mDatabaseReference != null){
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedList<Business> businesses = new LinkedList<>();
                            for(DataSnapshot businessSnapshot : dataSnapshot.getChildren()){
                                Business business = businessSnapshot.getValue(Business.class);
                                if(business != null){
                                    Log.d(TAG,"Favorite's list :" + business.getName());
                                    businesses.addFirst(business);
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
}
