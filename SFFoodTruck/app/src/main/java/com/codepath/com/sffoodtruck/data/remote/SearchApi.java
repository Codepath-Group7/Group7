package com.codepath.com.sffoodtruck.data.remote;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.ReviewsResponse;
import com.codepath.com.sffoodtruck.data.model.SearchResults;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by saip92 on 10/11/2017.
 */

public interface SearchApi {

    @GET("/v3/businesses/search")
    Call<SearchResults> getSearchResults(@Query("location") String location, @Query("categories") String category);

    @GET("/v3/businesses/search")
    Call<SearchResults> getSearchResults(@QueryMap Map<String,String> queryParams);

    @GET("/v3/businesses/{id}")
    Call<Business> getBusiness(@Path("id") String businessId);

    @GET("/v3/businesses/{id}/reviews")
    Call<ReviewsResponse> getBusinessReviews(@Path("id") String businessId);

}
