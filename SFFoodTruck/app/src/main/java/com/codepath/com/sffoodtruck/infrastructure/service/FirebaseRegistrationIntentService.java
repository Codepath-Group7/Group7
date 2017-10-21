package com.codepath.com.sffoodtruck.infrastructure.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.local.QueryPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * IntentService to ensure push notifications are received
 * even while away from activity during registration
 * Created by robl2e on 10/19/17.
 */

public class FirebaseRegistrationIntentService extends IntentService {
    private static final String TAG = FirebaseRegistrationIntentService.class.getSimpleName();
    private static final String INTENT_EXTRA_FORCE_REFRESH = "INTENT_EXTRA_FORCE_REFRESH";

    public FirebaseRegistrationIntentService() {
        super(TAG);
    }

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean forceRefresh) {
        Intent intent = new Intent(context, FirebaseRegistrationIntentService.class);
        intent.putExtra(INTENT_EXTRA_FORCE_REFRESH, forceRefresh);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        boolean forceRefresh = false;
        if (intent.hasExtra(INTENT_EXTRA_FORCE_REFRESH)) {
            forceRefresh = intent.getBooleanExtra(INTENT_EXTRA_FORCE_REFRESH, false);
        }
        if (hasTokenAlready() && !forceRefresh) return;

        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);

        // request token that will be used by the server to send push notifications
        String token = instanceID.getToken();
        Log.d(TAG, "FCM Registration Token: " + token);

        QueryPreferences.storeString(getApplicationContext()
                , QueryPreferences.PREF_FCM_TOKEN, token);

        // pass along this data
        sendRegistrationToServer(token);
    }

    private boolean hasTokenAlready() {
        String token = QueryPreferences.getStoredString(getApplicationContext()
                , QueryPreferences.PREF_FCM_TOKEN);
        return !TextUtils.isEmpty(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
