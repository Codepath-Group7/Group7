package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saip92 on 10/18/2017.
 */

public class MessagePayload implements Parcelable {

    private String userId;
    private String message;
    private String imageUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return "MessagePayload{" +
                "userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.message);
        dest.writeString(this.imageUrl);
    }

    public MessagePayload() {
    }

    protected MessagePayload(Parcel in) {
        this.userId = in.readString();
        this.message = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<MessagePayload> CREATOR = new Parcelable.Creator<MessagePayload>() {
        @Override
        public MessagePayload createFromParcel(Parcel source) {
            return new MessagePayload(source);
        }

        @Override
        public MessagePayload[] newArray(int size) {
            return new MessagePayload[size];
        }
    };
}
