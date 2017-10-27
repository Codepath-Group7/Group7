package com.codepath.com.sffoodtruck.ui.userprofile.recentvisits;

import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
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

public class RecentVisitsPresenter extends UserProfileAbstractPresenter<RecentVisitsContract.View>
implements RecentVisitsContract.Presenter{


    private static final String TAG = RecentVisitsPresenter.class.getSimpleName();

    @Override
    public void initialLoad() {
        loadRecentVisits();
        getView().updateUI();
    }

    @Override
    public void loadRecentVisits() {
        DatabaseReference databaseReference= FirebaseUtils.getPreviousTripsDatabaseRef();
        if(databaseReference != null){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedList<Business> businesses = new LinkedList<>();

                            //update for handling duplicates
                            for (DataSnapshot businessSnapshot : dataSnapshot.getChildren()) {
                                Business business = businessSnapshot.getValue(Business.class);
                                if (business != null) {
                                    Log.d(TAG, "Recent visits list :" + business.getName());
                                    if ( businesses.size()> 0 &&
                                            businesses.getFirst().getId().equals(business.getId())){
                                        Log.d(TAG,"First: " + businesses.getFirst().getName()
                                                + ", duplicate: " + business.getName());
                                        continue;
                                    }

                                    businesses.addFirst(business);
                                }
                            }
                            getView().showProgressBar(false);
                            getView().showRecentVisits(businesses);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
