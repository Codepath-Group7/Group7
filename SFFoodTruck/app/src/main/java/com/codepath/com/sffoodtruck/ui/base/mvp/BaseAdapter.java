package com.codepath.com.sffoodtruck.ui.base.mvp;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.BR;

/**
 * Created by saip92 on 10/15/2017.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{
        private final ViewDataBinding mBinding;

        public BaseViewHolder(ViewDataBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Object obj){
            mBinding.setVariable(BR._all,obj);
        }

    }

}
