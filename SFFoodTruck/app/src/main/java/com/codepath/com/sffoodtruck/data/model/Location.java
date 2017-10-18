package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by saip92 on 10/11/2017.
 */

public class Location implements Parcelable {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address3")
    @Expose
    private String address3;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    /*Start of fields required for Business Details*/
    @SerializedName("display_address")
    @Expose
    private List<String> displayAddress = null;
    @SerializedName("cross_streets")
    @Expose
    private String crossStreets;
    /*End of fields required for Business Details*/

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /*Start of methods required for Business Details*/

    public List<String> getDisplayAddress() {
        return displayAddress;
    }

    public String getCompleteAddress() {
        if(displayAddress.size()==2){
            return displayAddress.get(0) + ", " + displayAddress.get(1);
        }
        else{
            return displayAddress.get(0);
        }

    }

    public void setDisplayAddress(List<String> displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getCrossStreets() {
        return crossStreets;
    }

    public void setCrossStreets(String crossStreets) {
        this.crossStreets = crossStreets;
    }

    /*End of methods required for Business Details*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.address2);
        dest.writeString(this.address3);
        dest.writeString(this.state);
        dest.writeString(this.address1);
        dest.writeString(this.zipCode);
        dest.writeStringList(this.displayAddress);
        dest.writeString(this.crossStreets);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.city = in.readString();
        this.country = in.readString();
        this.address2 = in.readString();
        this.address3 = in.readString();
        this.state = in.readString();
        this.address1 = in.readString();
        this.zipCode = in.readString();
        this.displayAddress = in.createStringArrayList();
        this.crossStreets = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
