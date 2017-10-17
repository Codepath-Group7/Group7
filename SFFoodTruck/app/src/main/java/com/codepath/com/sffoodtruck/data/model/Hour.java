package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class Hour implements Parcelable {

    @SerializedName("open")
    @Expose
    private List<Open> open = null;
    @SerializedName("hours_type")
    @Expose
    private String hoursType;
    @SerializedName("is_open_now")
    @Expose
    private Boolean isOpenNow;

    public List<Open> getOpen() {
        return open;
    }

    public void setOpen(List<Open> open) {
        this.open = open;
    }

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public Boolean getIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(Boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.open);
        dest.writeString(this.hoursType);
        dest.writeValue(this.isOpenNow);
    }

    public Hour() {
    }

    protected Hour(Parcel in) {
        this.open = in.createTypedArrayList(Open.CREATOR);
        this.hoursType = in.readString();
        this.isOpenNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Hour> CREATOR = new Parcelable.Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
