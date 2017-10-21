package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/15/2017.
 */

public class FoodTruckFeedAdapter extends SingleLayoutAdapter {

    private List<Business> mBusinesses;
    private static final String TAG = FoodTruckFeedAdapter.class.getSimpleName();

    public FoodTruckFeedAdapter(List<Business> businesses) {
        super(R.layout.foodtruck_feed_item_layout);
        mBusinesses = businesses;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return mBusinesses.get(position);
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

    public void addData(List<Business> businesses){
        if(mBusinesses.size() == 0){
            Log.d(TAG,"Adding data for the first time to the adapter");
            mBusinesses.addAll(businesses);
            notifyDataSetChanged();
            return;
        }
        Log.d(TAG,"Appending data to existing list of businesses in the adapter");
        int oldSize = mBusinesses.size();
        mBusinesses.addAll(businesses);
        notifyItemRangeInserted(oldSize, mBusinesses.size());
    }

    public void clearData(){
        mBusinesses.clear();
    }

    public Business getBusinessForPos(int pos){
        if(pos >= 0 && pos < mBusinesses.size())
            return mBusinesses.get(pos);
        else
            return null;
    }
}
