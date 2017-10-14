package com.codepath.com.sffoodtruck.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.model.YelpAccessToken;
import com.codepath.com.sffoodtruck.data.remote.LoginService;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaymathur on 10/13/17.
 */

public class LoginUtils {
    private final static String TAG = LoginUtils.class.getSimpleName();

    public static void getYelpAccessToken(final Context context){
        LoginService service = RetrofitClient
                .getInstance()
                .getRetrofit()
                .create(LoginService.class);
        Call<YelpAccessToken> call = service.getAccessToken(context.getString(R.string.client_key),
                context.getString(R.string.client_secret),
                "authorization_code");

        call.enqueue(new Callback<YelpAccessToken>() {
            @Override
            public void onResponse(Call<YelpAccessToken> call, Response<YelpAccessToken> response) {
                YelpAccessToken accessToken = response.body();
                if(accessToken != null){

                    QueryPreferences.storeAccessToken(context
                            ,accessToken.getAccessToken()
                            ,accessToken.getTokenType());

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
            }
        });
    }

    private void testSearchApiRequest(Context context) {
        SearchApi services = RetrofitClient
                .createService(SearchApi.class,context);
        Call<SearchResults> callResults = services.getSearchResults("95112");
        callResults.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call,
                                   Response<SearchResults> response) {
                if(response.body() == null){
                    Log.d(TAG,"response has failed "
                            + response.code());
                    return;
                }

                Log.d(TAG,response.body()
                        .getBusinesses().get(0).getName());
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                Log.e(TAG,"Failed",t)
                ;                                  }
        });
    }
}
