package com.codepath.com.sffoodtruck.ui.base.mvp;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;


/**
 * Created by saip92 on 10/15/2017.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater,
                viewType,parent,false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract int getLayoutIdForPosition(int position);

    protected abstract Object getObjForPosition(int position);


}
