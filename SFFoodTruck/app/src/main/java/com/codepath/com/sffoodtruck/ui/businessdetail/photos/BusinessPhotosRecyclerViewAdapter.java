package com.codepath.com.sffoodtruck.ui.businessdetail.photos;

import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.ui.base.mvp.SingleLayoutAdapter;

import java.util.List;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class BusinessPhotosRecyclerViewAdapter extends SingleLayoutAdapter {

    private List<String> mPhotoList;

    public BusinessPhotosRecyclerViewAdapter(List<String> photoList) {
        super(R.layout.photo_item);
        mPhotoList = photoList;
    }

    public void addPhotos(List<String> photoList){
        Log.d("Adapter","Photo List " + photoList);
        int initialSize = getItemCount();
        mPhotoList.addAll(photoList);
        Log.d("Adapter","Photo List after adding: " + mPhotoList );
        notifyItemRangeInserted(initialSize,photoList.size());
    }

    public void addPhoto(String photo){
        mPhotoList.add(photo);
        notifyItemInserted(mPhotoList.size());
    }

    @Override
    protected Object getObjForPosition(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }
}
