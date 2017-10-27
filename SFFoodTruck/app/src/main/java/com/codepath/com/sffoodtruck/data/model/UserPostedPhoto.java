package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saip92 on 10/26/2017.
 */

public class UserPostedPhoto implements Parcelable {

    private String mImageUrl;
    private Business mBusiness;

    public UserPostedPhoto(){

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public Business getBusiness() {
        return mBusiness;
    }

    public void setBusiness(Business business) {
        mBusiness = business;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageUrl);
        dest.writeParcelable(this.mBusiness, flags);
    }

    protected UserPostedPhoto(Parcel in) {
        this.mImageUrl = in.readString();
        this.mBusiness = in.readParcelable(Business.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserPostedPhoto> CREATOR = new Parcelable.Creator<UserPostedPhoto>() {
        @Override
        public UserPostedPhoto createFromParcel(Parcel source) {
            return new UserPostedPhoto(source);
        }

        @Override
        public UserPostedPhoto[] newArray(int size) {
            return new UserPostedPhoto[size];
        }
    };
}
