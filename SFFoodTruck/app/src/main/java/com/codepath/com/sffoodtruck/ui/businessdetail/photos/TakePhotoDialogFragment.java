package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.FragmentTakePhotoLayoutBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class TakePhotoDialogFragment extends BottomSheetDialogFragment {
    public final static String TAG = TakePhotoDialogFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri mCurrentPhotoPath = null;
    private ImageView mImageView;
    private boolean isPhotoCaptured = false;
    public static final String EXTRA_PHOTO_URI = "photo_uri";

    public interface TakePhotoListner {
        void OnPhotoSubmit(Uri photoPath);
    }

    public static DialogFragment newInstance(Uri photoPath){
        TakePhotoDialogFragment fragment = new TakePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PHOTO_URI,photoPath);
        fragment.setArguments(args);
        return fragment;
    }

    private TakePhotoListner mTakePhotoListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.upload_photo)
                .customView(R.layout.fragment_take_photo_layout, true)
                .positiveText(R.string.label_upload)
                .onPositive((dialog1, which) -> uploadPhoto())
                .negativeText(R.string.label_cancel)
                .onNegative((dialog1, which) -> dismiss())
                .build();
        View view = dialog.getCustomView();
        if(view!=null) {
            mImageView = (ImageView) view.findViewById(R.id.iv_add_new_photo);
        }
        //mImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_photo_black_24dp));
        mCurrentPhotoPath = getArguments().getParcelable(EXTRA_PHOTO_URI);
        Picasso.with(getActivity())
                .load(mCurrentPhotoPath)
                .placeholder(R.drawable.ic_photo_black_24dp)
                .into(mImageView);
        //dispatchTakePictureIntent();
        return dialog;
    }

    private void uploadPhoto() {
        mTakePhotoListner.OnPhotoSubmit(mCurrentPhotoPath);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TakePhotoListner) {
            mTakePhotoListner = (TakePhotoListner) context;
        }
    }
}
