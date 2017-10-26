package com.codepath.com.sffoodtruck.ui.userprofile;

import android.support.annotation.LayoutRes;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/22/2017.
 */

public class UserProfileAdapter extends SingleLayoutAdapter{

    private List<Business> mBusinesses;

    @LayoutRes
    protected static int getLayoutRes(){
        return R.layout.userprofile_business_item_layout;
    }

    public UserProfileAdapter(List<Business> businesses) {
        super(getLayoutRes());
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

    public void addFavorites(List<Business> businessList) {
        mBusinesses.addAll(businessList);
        notifyDataSetChanged();
    }
}
