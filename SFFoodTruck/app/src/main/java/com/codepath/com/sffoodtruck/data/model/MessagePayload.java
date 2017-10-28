package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by saip92 on 10/18/2017.
 */

public class MessagePayload implements Parcelable {

    private String userId;
    private String message;
    private String imageUrl;
    private long timestamp;
    private String time;
    private UUID mUUID;

    public UUID getUUID() {
        return mUUID;
    }

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

    public long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "MessagePayload{" +
                "userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", timestamp=" + timestamp +
                ", time='" + time + '\'' +
                ", mUUID=" + mUUID +
                '}';
    }

    public MessagePayload() {
        mUUID = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        this.time = dateFormat.format(calendar.getTime());
        timestamp = System.currentTimeMillis();
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
        dest.writeLong(this.timestamp);
        dest.writeString(this.time);
        dest.writeSerializable(this.mUUID);
    }

    protected MessagePayload(Parcel in) {
        this.userId = in.readString();
        this.message = in.readString();
        this.imageUrl = in.readString();
        this.timestamp = in.readLong();
        this.time = in.readString();
        this.mUUID = (UUID) in.readSerializable();
    }

    public static final Creator<MessagePayload> CREATOR = new Creator<MessagePayload>() {
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
