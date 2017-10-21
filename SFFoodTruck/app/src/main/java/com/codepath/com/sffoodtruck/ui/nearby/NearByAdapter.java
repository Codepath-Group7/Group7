package com.codepath.com.sffoodtruck.ui.nearby;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.ItemChatBinding;
import com.codepath.com.sffoodtruck.ui.util.CircleTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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

        private static final String TAG = "NearByAdapter.class";
        private ItemChatBinding mBinding;

        public NearByViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindMessagePayload(MessagePayload payload){
            String messageUserId = payload.getUserId();
            final boolean isMe = messageUserId != null && messageUserId.equals(userId);
            Log.d(TAG,payload.getImageUrl() + " -> payload image url");
            if(isMe){
                mBinding.ivProfileMe.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(payload.getImageUrl())
                        .transform(new CircleTransform())
                        .into(mBinding.ivProfileMe);
                mBinding.ivProfileOther.setVisibility(View.GONE);
                mBinding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            }else {
                mBinding.ivProfileMe.setVisibility(View.GONE);
                mBinding.ivProfileOther.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(payload.getImageUrl())
                        .transform(new CircleTransform())
                        .into(mBinding.ivProfileOther);
                mBinding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            }
            mBinding.tvBody.setText(payload.getMessage());
        }
    }
}
