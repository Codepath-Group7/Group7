package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saip92 on 11/1/2017.
 */

public class CustomPlace implements Parcelable {

    String adddress;
    double latitude;
    double longitude;

    public CustomPlace() {
    }

    public CustomPlace(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAdddress() {
        return adddress;
    }

    public void setAdddress(String adddress) {
        this.adddress = adddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adddress);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected CustomPlace(Parcel in) {
        this.adddress = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<CustomPlace> CREATOR = new Parcelable.Creator<CustomPlace>() {
        @Override
        public CustomPlace createFromParcel(Parcel source) {
            return new CustomPlace(source);
        }

        @Override
        public CustomPlace[] newArray(int size) {
            return new CustomPlace[size];
        }
    };

    @Override
    public String toString() {
        return "CustomPlace{" +
                "adddress='" + adddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
