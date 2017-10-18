package com.codepath.com.sffoodtruck.ui.nearby;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.com.sffoodtruck.databinding.ItemChatBinding;

/**
 * Created by saip92 on 10/17/2017.
 */

public class NearByAdapter {





    class NearByViewholder extends RecyclerView.ViewHolder{

        private ItemChatBinding mBinding;

        public NearByViewholder(ItemChatBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
