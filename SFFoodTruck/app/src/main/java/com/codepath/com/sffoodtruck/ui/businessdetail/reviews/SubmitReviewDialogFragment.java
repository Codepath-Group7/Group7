package com.codepath.com.sffoodtruck.ui.businessdetail.reviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.data.model.User;
import com.codepath.com.sffoodtruck.databinding.FragmentSubmitReviewBinding;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by akshaymathur on 10/18/17.
 */

public class SubmitReviewDialogFragment extends DialogFragment {

    private FragmentSubmitReviewBinding mBinding;
    public final static String EXTRA_REVIEW = "review_extra";
    public final static String TAG = SubmitReviewDialogFragment.class.getSimpleName();
    public interface SubmitReviewListener{
        void onReviewSubmit(Review review);
    }
    private SubmitReviewListener mSubmitReviewListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_submit_review,null,false);
        //View v = inflater.inflate(R.layout.fragment_take_photo_layout,null);
        mBinding.btnSubmit.setOnClickListener(view -> submitReview());
        mBinding.btnCancel.setOnClickListener(view -> dismiss());
        alertDialog.setView(mBinding.getRoot());
        return alertDialog.create();
    }

    private void submitReview() {
        if(TextUtils.isEmpty(mBinding.etReviewText.getText())){
            mBinding.layoutReviewText.setError(getString(R.string.error_empty_review));
            return;
        }
        Review newReview = new Review();
        User user = new User();
        FirebaseUser firebaseUser = FirebaseUtils.getCurrentUser();
        user.setName(firebaseUser.getDisplayName());
        user.setImageUrl(firebaseUser.getPhotoUrl().toString());
        newReview.setText(mBinding.etReviewText.getText().toString());
        newReview.setRating(mBinding.rbFoodTruckRating.getRating());
        newReview.setUser(user);
        mSubmitReviewListener.onReviewSubmit(newReview);
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SubmitReviewListener){
            mSubmitReviewListener = (SubmitReviewListener) context;
        }
    }
}
