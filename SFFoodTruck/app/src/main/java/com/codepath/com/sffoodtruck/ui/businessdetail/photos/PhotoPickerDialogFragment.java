package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.com.sffoodtruck.R;

/**
 * Created by akshaymathur on 11/17/17.
 */

public class PhotoPickerDialogFragment extends DialogFragment {
    public final static String TAG = PhotoPickerDialogFragment.class.getSimpleName();
    private Button btnTakePhoto, btnChoosePhoto;

    public interface PhotoPickerListener{
        void onTakePhoto();
        void onChoosePhoto();
    }

    private PhotoPickerListener mPhotoPickerListener;
    public static DialogFragment newInstance(){
        PhotoPickerDialogFragment photoPickerDialogFragment = new PhotoPickerDialogFragment();
        Bundle args = new Bundle();
        photoPickerDialogFragment.setArguments(args);
        return photoPickerDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.label_choose_photo))
                .customView(R.layout.fragment_photo_picker_layout,false)
                .build();
        View view = dialog.getCustomView();
        if(view!=null){
            btnTakePhoto = (Button) view.findViewById(R.id.btnTakePhoto);
            btnChoosePhoto = (Button) view.findViewById(R.id.btnChoosePhoto);
            btnTakePhoto.setOnClickListener(view1 -> dispatchTakePictureIntent());
            btnChoosePhoto.setOnClickListener(view1 -> dispatchGalleryIntent());
        }
        return dialog;
    }

    private void dispatchGalleryIntent() {
        mPhotoPickerListener.onChoosePhoto();
        dismiss();
    }

    private void dispatchTakePictureIntent() {
        mPhotoPickerListener.onTakePhoto();
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhotoPickerListener) {
            mPhotoPickerListener = (PhotoPickerListener) context;
        }
    }
}
