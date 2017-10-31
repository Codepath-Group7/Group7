package com.codepath.com.sffoodtruck.ui.homefeed;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FoodtruckFeedItemLayoutBinding;

import java.util.List;

/**
 * Created by saip92 on 10/31/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Business> mBusinesses;
    private onBusinessItemClickListener mListener;

    public interface onBusinessItemClickListener{
        void onClickBusinessItem(Business business, View view);
        void onClickFab(Business business);
    }

    FeedAdapter(List<Business> businesses, onBusinessItemClickListener listener){
        mBusinesses = businesses;
        mListener = listener;
    }

    public void setListener(onBusinessItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FoodtruckFeedItemLayoutBinding itemLayoutBinding =
                DataBindingUtil.inflate(inflater, R.layout.foodtruck_feed_item_layout,parent, false);
        return new FeedViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FeedViewHolder)holder).bindBusiness(mBusinesses.get(position));
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

    public void addAll(List<Business> businessList) {
        mBusinesses.addAll(businessList);
        notifyDataSetChanged();
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FoodtruckFeedItemLayoutBinding mFeedItemLayoutBinding;
        private Business mBusiness;

        public FeedViewHolder(FoodtruckFeedItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            mFeedItemLayoutBinding = itemLayoutBinding;
            mFeedItemLayoutBinding.rlFoodTruckFeedItem.setOnClickListener(this);
            mFeedItemLayoutBinding.fab.setOnClickListener(this);
        }

        void bindBusiness(Business business){
            mBusiness = business;
            mFeedItemLayoutBinding.setObj(business);
            mFeedItemLayoutBinding.executePendingBindings();


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fab:
                    mListener.onClickFab(mBusiness);
                    break;
                case R.id.rlFoodTruckFeedItem:
                    mListener.onClickBusinessItem(mBusiness,itemView);
                    break;
            }
        }
    }

}
