package com.codepath.com.sffoodtruck.ui.businessdetail;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class BusinessReviewsRecyclerViewAdapter extends SingleLayoutAdapter {
    private List<Review> mReviewList;
    public BusinessReviewsRecyclerViewAdapter(List<Review> reviewList){
        super(R.layout.review_item_layout);
        mReviewList = reviewList;
    }

    public void addReviews(List<Review> reviewList){
        int initialSize = getItemCount();
        mReviewList.addAll(reviewList);
        notifyItemRangeInserted(initialSize,reviewList.size());
    }

    public void addReview(Review review){
        mReviewList.add(review);
        notifyItemInserted(mReviewList.size());
    }
    @Override
    protected Object getObjForPosition(int position) {
        return mReviewList.get(position);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
