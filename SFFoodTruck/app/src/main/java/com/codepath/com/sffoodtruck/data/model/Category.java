package com.codepath.com.sffoodtruck.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saip92 on 10/11/2017.
 */

public class Category {

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("title")
    @Expose
    private String title;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
