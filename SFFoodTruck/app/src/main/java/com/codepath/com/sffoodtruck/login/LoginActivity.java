package com.codepath.com.sffoodtruck.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.com.sffoodtruck.HomeActivity;
import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.codepath.com.sffoodtruck.data.model.SearchResults;
import com.codepath.com.sffoodtruck.data.model.YelpAccessToken;
import com.codepath.com.sffoodtruck.data.remote.LoginService;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.databinding.ActivityLoginBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding mBinding;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setUpLogin();
            }
        });
        mBinding.fbLoginButton.setReadPermissions("email","public_profile");
        mBinding.fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                else{
                    Log.d(TAG,"User not logged in");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void setUpLogin() {
        LoginService service = RetrofitClient
                .getInstance()
                .getRetrofit()
                .create(LoginService.class);
        Call<YelpAccessToken> call = service.getAccessToken(getString(R.string.client_key),
                getString(R.string.client_secret),
                "authorization_code");

        call.enqueue(new Callback<YelpAccessToken>() {
            @Override
            public void onResponse(Call<YelpAccessToken> call, Response<YelpAccessToken> response) {
                YelpAccessToken accessToken = response.body();
                if(accessToken != null){

                    QueryPreferences.storeAccessToken(LoginActivity.this
                            ,accessToken.getAccessToken()
                            ,accessToken.getTokenType());

                    //Just to test the OAuth flow.
                    testSearchApiRequest();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
