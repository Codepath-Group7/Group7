package com.codepath.com.sffoodtruck.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saip92 on 10/11/2017.
 */

public class Business implements Parcelable {
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_closed")
    @Expose
    private Boolean isClosed;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("distance")
    @Expose
    private Float distance;
    @SerializedName("transactions")
    @Expose
    private List<String> transactions = null;

    /* Start of fields needed for business detail */
    @SerializedName("is_claimed")
    @Expose
    private Boolean isClaimed;
    @SerializedName("display_phone")
    @Expose
    private String displayPhone;
    @SerializedName("photos")
    @Expose
    private List<String> photos = null;
    @SerializedName("hours")
    @Expose
    private List<Hour> hours = null;
    //Field to identify it as a favorite of user or not
    private boolean isFavorite;
    /* End of fields needed for business detail */


    public Boolean getClosed() {
        return isClosed;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getCategory(){
        if(categories == null || categories.size() == 0){
            return null;
        }else{
            return categories.get(0).getTitle();
        }
    }
    /* Start of changes
    Added by: Akshay
    Reason: method to get all business categories excluding 'foodtrucks'
     */
    public String getAllCategories(){
        if(categories == null || categories.size() == 0){
            return null;
        }else{
            StringBuilder categoryStringBuilder = new StringBuilder();
            for(Category category : categories){
                if(!category.getAlias().equalsIgnoreCase("foodtrucks")){
                    categoryStringBuilder.append(category.getTitle());
                    categoryStringBuilder.append(", ");
                }
            }
            String categoryString = categoryStringBuilder.toString();
            if(categoryString.length()>0) return categoryString.substring(0,categoryString.length()-2);
            else return null;
        }
    }
    /* End of changes*/

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Float getDistance() {
        return distance;
    }

    public String getDistanceInString(){
        double miles = Math.round(distance * 0.00018939 * 100.0)/100.0;
        if(miles < 0.1){
            return String.valueOf(Math.round(distance * 100.0)/100.0 + " ft");
        }
        return String.valueOf(String.valueOf(miles +" mi"));
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

     /* Start of methods needed for business detail view */

    public Boolean getClaimed() {
        return isClaimed;
    }

    public void setClaimed(Boolean claimed) {
        isClaimed = claimed;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }
     /* End of fields needed for business detail view */


    public Business() {
        isFavorite = false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.rating);
        dest.writeString(this.price);
        dest.writeString(this.phone);
        dest.writeString(this.id);
        dest.writeValue(this.isClosed);
        dest.writeTypedList(this.categories);
        dest.writeValue(this.reviewCount);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeParcelable(this.coordinates, flags);
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.location, flags);
        dest.writeValue(this.distance);
        dest.writeStringList(this.transactions);
        dest.writeValue(this.isClaimed);
        dest.writeString(this.displayPhone);
        dest.writeStringList(this.photos);
        dest.writeTypedList(this.hours);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    protected Business(Parcel in) {
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.price = in.readString();
        this.phone = in.readString();
        this.id = in.readString();
        this.isClosed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.categories = in.createTypedArrayList(Category.CREATOR);
        this.reviewCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.url = in.readString();
        this.coordinates = in.readParcelable(Coordinates.class.getClassLoader());
        this.imageUrl = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.distance = (Float) in.readValue(Float.class.getClassLoader());
        this.transactions = in.createStringArrayList();
        this.isClaimed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.displayPhone = in.readString();
        this.photos = in.createStringArrayList();
        this.hours = in.createTypedArrayList(Hour.CREATOR);
        this.isFavorite = in.readByte() != 0;
    }

    public static final Creator<Business> CREATOR = new Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel source) {
            return new Business(source);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };
}
