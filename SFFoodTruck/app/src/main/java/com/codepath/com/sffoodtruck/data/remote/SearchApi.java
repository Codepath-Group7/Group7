package com.codepath.com.sffoodtruck.data.remote;

import com.codepath.com.sffoodtruck.data.model.SearchResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by saip92 on 10/11/2017.
 */

public interface SearchApi {

    @GET("/v3/businesses/search")
    Call<SearchResults> getSearchResults(@Query("location") String location);

}
