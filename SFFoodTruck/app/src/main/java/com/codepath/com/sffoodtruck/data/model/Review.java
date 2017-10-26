package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class Review implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;

    @Nullable
    private Business mBusiness;

    @Nullable
    public Business getBusiness() {
        return mBusiness;
    }

    public void setBusiness(@Nullable Business business) {
        mBusiness = business;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Review() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.text);
        dest.writeValue(this.rating);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.timeCreated);
        dest.writeParcelable(this.mBusiness, flags);
    }

    protected Review(Parcel in) {
        this.url = in.readString();
        this.text = in.readString();
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.timeCreated = in.readString();
        this.mBusiness = in.readParcelable(Business.class.getClassLoader());
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
