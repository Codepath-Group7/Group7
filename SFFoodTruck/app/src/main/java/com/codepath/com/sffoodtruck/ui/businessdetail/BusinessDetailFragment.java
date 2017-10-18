package com.codepath.com.sffoodtruck.ui.businessdetail;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.databinding.FragmentBusinessDetailBinding;
import com.codepath.com.sffoodtruck.ui.base.mvp.AbstractMvpFragment;
import com.codepath.com.sffoodtruck.ui.util.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailFragment extends AbstractMvpFragment<BusinessDetailContract.MvpView
        , BusinessDetailContract.Presenter> implements BusinessDetailContract.MvpView {

    public static final String TAG = BusinessDetailFragment.class.getSimpleName();
    public static final String BUSINESS_KEY = "business_key";
    public static final int REQUEST_PHOTO = 99;
    private Business mBusiness;
    private FragmentBusinessDetailBinding mBinding;
    private List<String> mPhotoList;
    private BusinessPhotosRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public BusinessDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public BusinessDetailContract.Presenter createPresenter() {
        return new BusinessDetailPresenter();
    }

    public static Fragment newInstance(Business business){
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUSINESS_KEY,business);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusiness = getArguments().getParcelable(BUSINESS_KEY);
        mPhotoList = new ArrayList<>();
        mAdapter = new BusinessPhotosRecyclerViewAdapter(mPhotoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_business_detail, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        mBinding.btnAddPhotos.setOnClickListener((v)->{
                DialogFragment fragment = new TakePhotoDialogFragment();
            fragment.setTargetFragment(BusinessDetailFragment.this,REQUEST_PHOTO);
                fragment.show(getFragmentManager(),TakePhotoDialogFragment.TAG);
            });
        getPresenter().loadBusiness(getActivity(),mBusiness.getId());
    }
    private void setupRecyclerView(){
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mBinding.rvPhotosList.setLayoutManager(mLayoutManager);
        mBinding.rvPhotosList.setAdapter(mAdapter);
    }

    @Override
    public void renderBusiness(Business data) {
        mBinding.tvBusinessName.setText(data.getName());
        mBinding.tvBusinessAddress.setText(data.getLocation().getCompleteAddress());
        mBinding.tvBusinessDesc.setText(data.getAllCategories());
        mBinding.tvBusinessPhone.setText(data.getDisplayPhone());
        mBinding.rbFoodTruckRating.setRating(data.getRating());
        mBinding.tvPrice.setText(data.getPrice());
        mBinding.tvBusinessHrs.setText(data.getHours().get(0).getTodaysHours());
        mAdapter.addPhotos(data.getPhotos());
        fetchPhotosFromFirebase();
        Picasso.with(getActivity())
                .load(data.getImageUrl())
                .fit()
                .into(mBinding.ivBusinessImage);
    }

    private void fetchPhotosFromFirebase(){
        DatabaseReference  databaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(mBusiness.getId());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String photoName = dataSnapshot.getValue(String.class);
                Log.d(TAG,"Firebase photos path--> " + photoName);
                mAdapter.addPhoto(photoName);
                /*StorageReference storageReference = FirebaseUtils.getBusinessPhotoReference(photoName);
                if(storageReference!=null){
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG,"Firebase photos path--> " + uri.toString());
                            mAdapter.addPhoto(uri.toString());
                        }
                    });

                }*/


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "From dialog fragment " + data.getParcelableExtra(TakePhotoDialogFragment.EXTRA_PHOTO_URI));
            uploadPhotoToStorage(data.getParcelableExtra(TakePhotoDialogFragment.EXTRA_PHOTO_URI));
        }
    }

    private void uploadPhotoToStorage(Uri photoUri){
        StorageReference photoRef = FirebaseUtils.getBusinessPhotoReference(photoUri.getLastPathSegment());
        UploadTask mUploadTask = photoRef.putFile(photoUri);
        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG,"File upload failed");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.
                        getTotalByteCount();
                Log.d(TAG,"Upload is " + progress + "% done");
                //progressDialog.setProgress((int) progress);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG,"File upload successfully");
                DatabaseReference databaseReference = FirebaseUtils.getBusinessDatabasePhotoRef(mBusiness.getId());
                databaseReference.push().setValue(taskSnapshot.getDownloadUrl().toString());
                //progressDialog.dismiss();
            }
        });
    }
}
