package com.codepath.com.sffoodtruck.data.remote;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.BuildConfig;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by saip92 on 9/26/2017.
 * Singleton class that handles Retrofit client creation
 */

public class RetrofitClient {

    private static final String TAG = RetrofitClient.class.getSimpleName();

    private static final String BASE_URL = "https://api.yelp.com/";

    private static Retrofit mRetrofit = null;

    private static RetrofitClient sRetrofitClient;

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private RetrofitClient(){
        mRetrofit = sBuilder.build();
    }

    public static RetrofitClient getInstance(){
        if(sRetrofitClient == null){
            sRetrofitClient = new RetrofitClient();
        }
        return sRetrofitClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }


    //Generic service creator that adds authentication headers while creating new API interfaces
    public static <S> S createService(
            Class<S> serviceClass, Context context)  {
        if(context == null) try {
            throw new Exception("context cannot be null");
        } catch (Exception e) {
            Log.d(TAG,"Context cannot be null inside createService",e);
        }

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        String authToken = QueryPreferences.getAccessToken(context);
        if (!TextUtils.isEmpty(authToken)) {

                AuthenticationInterceptor interceptor = new
                        AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                sBuilder.client(httpClient.build());
                mRetrofit = sBuilder.build();
            }

        }

        return mRetrofit.create(serviceClass);
    }
}
