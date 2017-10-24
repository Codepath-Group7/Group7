package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.databinding.FragmentTakePhotoLayoutBinding;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class TakePhotoDialogFragment extends DialogFragment {
    private FragmentTakePhotoLayoutBinding mBinding;
    public final static String TAG = TakePhotoDialogFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String EXTRA_PHOTO_URI = "photo_uri";
    private Uri mCurrentPhotoPath=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_take_photo_layout,null,false);
        //View v = inflater.inflate(R.layout.fragment_take_photo_layout,null);
        mBinding.ivAddNewPhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        mBinding.btnUpload.setOnClickListener(view -> uploadPhoto());
        mBinding.btnCancel.setOnClickListener(view -> dismiss());
        alertDialog.setView(mBinding.getRoot());
        return alertDialog.create();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.codepath.com.sffoodtruck.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Uri.fromFile(image);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "From Camera " + mCurrentPhotoPath);
            mBinding.ivNewImagePlaceholder.setVisibility(View.GONE);
            mBinding.ivAddNewPhoto.setImageURI(mCurrentPhotoPath);
        }
    }

    private void uploadPhoto(){
        if(getTargetFragment()!=null){
            Intent photoData = new Intent();
            photoData.putExtra(EXTRA_PHOTO_URI,mCurrentPhotoPath);
            getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,photoData);
            dismiss();
        }

    }
}
