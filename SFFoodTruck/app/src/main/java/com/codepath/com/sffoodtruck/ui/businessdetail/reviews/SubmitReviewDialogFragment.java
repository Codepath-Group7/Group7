package com.codepath.com.sffoodtruck.ui.businessdetail.reviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Review;
import com.codepath.com.sffoodtruck.data.model.User;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.DialogFragment;

/**
 * Created by akshaymathur on 10/18/17.
 */

public class SubmitReviewDialogFragment extends DialogFragment {

    public final static String TAG = SubmitReviewDialogFragment.class.getSimpleName();
    private AppCompatEditText mReviewText;
    private AppCompatRatingBar mRatingBar;
    private TextInputLayout mInputLayout;
    public interface SubmitReviewListener{
        void onReviewSubmit(Review review);
    }
    private SubmitReviewListener mSubmitReviewListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.submit_review)
                .customView(R.layout.fragment_submit_review, true)
                .positiveText(R.string.label_submit)
                .onPositive((dialog1, which) -> submitReview())
                .negativeText(R.string.label_cancel)
                .onNegative((dialog1, which) -> dismiss())
                .build();
        View view = dialog.getCustomView();
        if(view!=null) {
            mReviewText = (AppCompatEditText) view.findViewById(R.id.et_review_text);
            mRatingBar = (AppCompatRatingBar) view.findViewById(R.id.rbFoodTruckRating);
            mInputLayout = (TextInputLayout) view.findViewById(R.id.layout_review_text);
        }
        return dialog;
    }

    private void submitReview() {
        if(TextUtils.isEmpty(mReviewText.getText())){
            mInputLayout.setError(getString(R.string.error_empty_review));
            return;
        }
        Review newReview = new Review();
        User user = new User();
        FirebaseUser firebaseUser = FirebaseUtils.getCurrentUser();
        user.setName(firebaseUser.getDisplayName());
        user.setImageUrl(firebaseUser.getPhotoUrl().toString());
        newReview.setText(mReviewText.getText().toString());
        newReview.setRating(mRatingBar.getRating());
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
