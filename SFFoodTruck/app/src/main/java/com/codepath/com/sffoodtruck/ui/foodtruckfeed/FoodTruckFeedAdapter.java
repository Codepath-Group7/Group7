package com.codepath.com.sffoodtruck.ui.foodtruckfeed;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/15/2017.
 */

public class FoodTruckFeedAdapter extends SingleLayoutAdapter {

    private List<Business> mBusinesses;

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

    public void addAll(List<Business> businesses){
        mBusinesses.clear();
        mBusinesses.addAll(businesses);
        notifyDataSetChanged();
    }

    public Business getBusinessForPos(int pos){
        if(pos >= 0 && pos < mBusinesses.size())
            return mBusinesses.get(pos);
        else
            return null;
    }
}
