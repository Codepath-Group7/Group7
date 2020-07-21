package com.codepath.com.sffoodtruck.ui.login;

import android.content.Context;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.model.YelpAccessToken;
import com.codepath.com.sffoodtruck.data.remote.LoginService;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/13/17.
 */


public class LoginUtils {
    private final static String TAG = LoginUtils.class.getSimpleName();

    public interface YelpAccessTokenListener{
        void onSaveAccessToken();
        void onFailure();
    }

    public static void getYelpAccessToken(final Context context, YelpAccessTokenListener listener){
        LoginService service = RetrofitClient
                .getInstance()
                .getRetrofit()
                .create(LoginService.class);
        Call<YelpAccessToken> call = service.getAccessToken(context.getString(R.string.client_key),
                context.getString(R.string.client_secret),
                "authorization_code");

        call.enqueue(new Callback<YelpAccessToken>() {
            @Override
            public void onResponse(@NonNull Call<YelpAccessToken> call,
                                   @NonNull Response<YelpAccessToken> response) {
                YelpAccessToken accessToken = response.body();
                if(accessToken != null){

                    QueryPreferences.storeAccessToken(context
                            ,accessToken.getAccessToken()
                            ,accessToken.getTokenType());

                    listener.onSaveAccessToken();

                    //Just to test the OAuth flow.
                    //testSearchApiRequest(context);

                    Log.d(TAG,accessToken.getAccessToken() + " is the access token: "
                            + accessToken.getTokenType() );
                }
                else{
                    Log.d(TAG,"accessToken failed");
                }
            }

            @Override
            public void onFailure(Call<YelpAccessToken> call, Throwable t) {
                Log.e(TAG,"Failed" ,t);
                listener.onFailure();
            }
        });
    }


}
