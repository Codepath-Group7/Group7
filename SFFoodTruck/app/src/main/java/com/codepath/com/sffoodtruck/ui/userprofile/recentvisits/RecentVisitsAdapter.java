package com.codepath.com.sffoodtruck.ui.userprofile.recentvisits;

import android.support.annotation.LayoutRes;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

class RecentVisitsAdapter extends UserProfileBaseAdapter<Business> {

    private List<Business> mBusinesses;

    RecentVisitsAdapter(List<Business> list) {
        super(list, R.layout.userprofile_business_item_layout);
        mBusinesses = list;
    }

    void addRecentVisits(List<Business> businessList) {
        mBusinesses.addAll(businessList);
        notifyDataSetChanged();
    }
}
