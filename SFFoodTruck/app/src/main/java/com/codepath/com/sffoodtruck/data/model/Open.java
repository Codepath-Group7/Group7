package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akshaymathur on 10/17/17.
 */

public class Open implements Parcelable {

    @SerializedName("is_overnight")
    @Expose
    private Boolean isOvernight;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("day")
    @Expose
    private Integer day;

    public Boolean getIsOvernight() {
        return isOvernight;
    }

    public void setIsOvernight(Boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.isOvernight);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeValue(this.day);
    }

    public Open() {
    }

    protected Open(Parcel in) {
        this.isOvernight = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.start = in.readString();
        this.end = in.readString();
        this.day = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Open> CREATOR = new Parcelable.Creator<Open>() {
        @Override
        public Open createFromParcel(Parcel source) {
            return new Open(source);
        }

        @Override
        public Open[] newArray(int size) {
            return new Open[size];
        }
    };
}
