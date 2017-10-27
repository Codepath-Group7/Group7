package com.codepath.com.sffoodtruck.ui.userprofile.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by saip92 on 10/22/2017.
 */

public class UserProfileBaseAdapter<T> extends SingleLayoutAdapter{

    private List<T> mList;

    public UserProfileBaseAdapter(List<T> list, @LayoutRes int layoutRes) {
        super(layoutRes);
        mList = list;
    }

    @Override
    protected Object getObjForPosition(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<T> extraItems){
        mList.addAll(extraItems);
        notifyDataSetChanged();
    }

}
