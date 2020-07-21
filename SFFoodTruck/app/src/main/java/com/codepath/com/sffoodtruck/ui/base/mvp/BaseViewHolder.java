package com.codepath.com.sffoodtruck.ui.base.mvp;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.com.sffoodtruck.BR;

/**
 * Created by saip92 on 10/15/2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    private final ViewDataBinding mBinding;

    public BaseViewHolder(ViewDataBinding binding){
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(Object obj){
        mBinding.setVariable(BR.obj,obj);
        mBinding.executePendingBindings();
    }

}

