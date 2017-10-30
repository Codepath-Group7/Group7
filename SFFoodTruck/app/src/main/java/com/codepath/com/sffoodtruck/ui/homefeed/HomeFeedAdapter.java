package com.codepath.com.sffoodtruck.ui.homefeed;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/30/2017.
 * Adapter for loading favorites
 */

public class HomeFeedAdapter extends UserProfileBaseAdapter<Business> {

    private List<Business> mBusinesses;

    public HomeFeedAdapter(List<Business> list) {
        super(list, R.layout.foodtruck_feed_item_layout);
        mBusinesses = list;
    }


    @Override
    protected Object getObjForPosition(int position) {
        return mBusinesses.get(position);
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }
}
