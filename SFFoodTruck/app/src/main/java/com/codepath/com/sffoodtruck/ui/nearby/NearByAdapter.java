package com.codepath.com.sffoodtruck.ui.nearby;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.ItemChatBinding;

import java.util.List;

/**
 * Created by saip92 on 10/17/2017.
 */

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.NearByViewHolder> {

    private List<MessagePayload> mMessagePayloads;
    private Context mContext;
    private String userId;

    public NearByAdapter(Context context, List<MessagePayload> messagePayloads, String userId){
        mContext = context;
        mMessagePayloads = messagePayloads;
        this.userId = userId;
    }

    @Override
    public NearByViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChatBinding binding = DataBindingUtil.inflate(inflater,R.layout.item_chat,parent,false);
        return new NearByViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(NearByViewHolder holder, int position) {
        holder.bindMessagePayload(mMessagePayloads.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessagePayloads.size();
    }


    class NearByViewHolder extends RecyclerView.ViewHolder{

        private ItemChatBinding mBinding;

        public NearByViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindMessagePayload(MessagePayload payload){
            String messageUserId = payload.getUserId();
            final boolean isMe = messageUserId != null && messageUserId.equals(userId);
            if(isMe){
                mBinding.ivProfileMe.setVisibility(View.VISIBLE);
                mBinding.ivProfileOther.setVisibility(View.GONE);
                mBinding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            }else {
                mBinding.ivProfileMe.setVisibility(View.GONE);
                mBinding.ivProfileOther.setVisibility(View.VISIBLE);
                mBinding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            }
            mBinding.tvBody.setText(payload.getMessage());
        }
    }
}
