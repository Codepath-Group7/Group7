package com.codepath.com.sffoodtruck.ui.base.mvp;

/**
 * Created by saip92 on 10/15/2017.
 */

public abstract class SingleLayoutAdapter extends BaseAdapter {

    private final int layoutId;

    public SingleLayoutAdapter(int layoutId){
        this.layoutId = layoutId;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }
}
