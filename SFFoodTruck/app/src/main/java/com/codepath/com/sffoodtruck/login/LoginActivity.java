package com.codepath.com.sffoodtruck.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.codepath.com.sffoodtruck.HomeActivity;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.AccessToken;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.remote.LoginService;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(view -> setUpLogin());
    }

    private void setUpLogin() {
        LoginService service = RetrofitClient
                .getInstance()
                .getRetrofit()
                .create(LoginService.class);
        Call<AccessToken> call = service.getAccessToken(getString(R.string.client_key),
                getString(R.string.client_secret),
                "authorization_code");

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken accessToken = response.body();
                if(accessToken != null){

                    QueryPreferences.storeAccessToken(LoginActivity.this
                            ,accessToken.getAccessToken()
                            ,accessToken.getTokenType());

                    startNextActivity();
                    //Just to test the OAuth flow.
                   // testSearchApiRequest();

                    Log.d(TAG,accessToken.getAccessToken() + " is the access token: "
                            + accessToken.getTokenType() );
                }
                else{
                    Log.d(TAG,"accessToken failed");
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(TAG,"Failed" ,t);
            }
        });
    }

    private void startNextActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void testSearchApiRequest() {
        SearchApi services = RetrofitClient
                .createService(SearchApi.class,LoginActivity.this);
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
