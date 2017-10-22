package com.codepath.com.sffoodtruck.ui.userprofile;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/22/2017.
 */

public class UserProfileAdapter extends SingleLayoutAdapter{

    private List<Business> mBusinesses;

    public UserProfileAdapter(List<Business> businesses) {
        super(R.layout.userprofile_business_item_layout);
        mBusinesses = businesses;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return mBusinesses.get(position);
    }

    @Override
    public int getItemCount() {
        if(mBusinesses.size() <= 5){
            return mBusinesses.size();
        }else{
            return 5;
        }
    }
}
