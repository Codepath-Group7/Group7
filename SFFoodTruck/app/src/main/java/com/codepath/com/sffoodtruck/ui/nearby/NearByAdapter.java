package com.codepath.com.sffoodtruck.ui.nearby;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.MessagePayload;
import com.codepath.com.sffoodtruck.databinding.ItemChatWithBgBinding;
import com.codepath.com.sffoodtruck.ui.userprofile.UserProfileActivity;
import com.codepath.com.sffoodtruck.ui.util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by saip92 on 10/17/2017.
 */

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.NearByViewHolder> {

    private LinkedList<MessagePayload> mMessagePayloads;
    private Context mContext;
    private String userId;
    private static boolean isSuccess = false;

    public NearByAdapter(Context context, LinkedList<MessagePayload> messagePayloads, String userId){
        mContext = context;
        mMessagePayloads = messagePayloads;
        this.userId = userId;
    }

    @Override
    public NearByViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChatWithBgBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_chat_with_bg,parent,false);
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

    public void addMessagePayload(MessagePayload payload){
        mMessagePayloads.add(0,payload);
        notifyItemInserted(0);
    }

    public void addAllMessagePayloads(List<MessagePayload> payloads){
        /*int oldSize = mMessagePayloads.size();
        mMessagePayloads.addAll(payloads);
        notifyItemRangeInserted(oldSize,mMessagePayloads.size());*/
        for(int i = 0 ; i < payloads.size(); i++){
            mMessagePayloads.addFirst(payloads.get(i));
            notifyItemInserted(0);
        }

    }

    public void clearAll(){
        mMessagePayloads.clear();
        notifyDataSetChanged();
    }

    public void postedMessageDisplay(boolean isSuccess, UUID messageId){
        this.isSuccess = isSuccess;
        for(int i = mMessagePayloads.size() - 1; i >= 0 ; i--){
            if(mMessagePayloads.get(i).getUUID().equals(messageId)){
                Log.d("Adapter","Found the item and making the required UI updates");
                notifyItemChanged(i);
            }
        }
    }

    class NearByViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "NearByAdapter.class";
        private ItemChatWithBgBinding mBinding;
        private MessagePayload mMessagePayload;

        public NearByViewHolder(ItemChatWithBgBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.ivProfileMe.setOnClickListener(this);
            mBinding.ivProfileOther.setOnClickListener(this);
        }

        void bindMessagePayload(MessagePayload payload){
            mMessagePayload = payload;
            String messageUserId = payload.getUserId();
            final boolean isMe = messageUserId != null && messageUserId.equals(userId);
            Log.d(TAG,payload.getImageUrl() + " -> payload image url");
           /* String chatBody = String.format("%s %n %n %s %n %n %s",payload.getUserName()
                    ,payload.getMessage(),payload.getTime());*/

           Spanned chatBody = fromHtml( "<b><i>" + payload.getUserName()
                   + "</i></b>  <br/> <br/>" +
                        payload.getMessage() +"  <br/> <br/> <b><i>"+ payload.getTime() +"</i></b>") ;

            if(isMe){
                mBinding.ivProfileMe.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(payload.getImageUrl())
                        .transform(new CircleTransform())
                        .into(mBinding.ivProfileMe);
                mBinding.ivProfileOther.setVisibility(View.INVISIBLE);
                mBinding.tvProfileOther.setVisibility(View.INVISIBLE);
                mBinding.tvProfileMe.setVisibility(View.VISIBLE);
                mBinding.tvProfileMe.setText(chatBody);
                //mBinding.tvBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            }else {
                mBinding.ivProfileMe.setVisibility(View.INVISIBLE);
                mBinding.tvProfileMe.setVisibility(View.INVISIBLE);
                mBinding.ivProfileOther.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(payload.getImageUrl())
                        .transform(new CircleTransform())
                        .into(mBinding.ivProfileOther);
                mBinding.tvProfileOther.setVisibility(View.VISIBLE);
                mBinding.tvProfileOther.setText(chatBody);
            }
        //    mBinding.tvBody.setText(payload.getMessage());
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.ivProfileMe:
                case R.id.ivProfileOther:
                    mContext.startActivity(UserProfileActivity.newIntent(mContext,mMessagePayload));
                    break;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


}
