package com.codepath.com.sffoodtruck.data.remote;

import com.codepath.com.sffoodtruck.data.model.YelpAccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by saip92 on 10/11/2017.
 */

public interface LoginService {
    @Headers("content-type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<YelpAccessToken> getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("grant_type") String grantType);
}
