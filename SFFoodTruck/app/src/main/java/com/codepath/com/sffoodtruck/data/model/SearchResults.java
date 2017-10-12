package com.codepath.com.sffoodtruck.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by saip92 on 10/11/2017.
 */

public class SearchResults {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = null;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }




}
