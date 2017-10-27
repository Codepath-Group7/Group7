package com.codepath.com.sffoodtruck.ui.userprofile.favorites;


import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.userprofile.base.UserProfileBaseAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/26/2017.
 */

class FavoritesAdapter extends UserProfileBaseAdapter<Business> {

    private List<Business> mBusinesses;

    FavoritesAdapter(List<Business> businesses) {
        super(businesses, R.layout.userprofile_business_item_layout);
        mBusinesses = businesses;
    }


}
