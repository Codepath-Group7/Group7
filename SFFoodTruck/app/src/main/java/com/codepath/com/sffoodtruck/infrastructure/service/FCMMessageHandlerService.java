package com.codepath.com.sffoodtruck.infrastructure.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.remote.RetrofitClient;
import com.codepath.com.sffoodtruck.data.remote.SearchApi;
import com.codepath.com.sffoodtruck.ui.notification.FoodTruckNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import retrofit2.Call;
import retrofit2.Response;

@WorkerThread
public class FCMMessageHandlerService extends FirebaseMessagingService {
    private static final String TAG = FCMMessageHandlerService.class.getSimpleName();
    private static final String ID = "id";

    @Override
    public void handleIntent(Intent intent) {
        /**
         * Ensures Notification is built correctly even when
         * application is in the background
         */
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder(TAG);

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: id = " + remoteMessage.getMessageId() +
                " from = " + remoteMessage.getFrom() +
                " messageType = " + remoteMessage.getMessageType() +
                " sentTime = " + remoteMessage.getSentTime()
        );
        Map<String, String> data = remoteMessage.getData();
        String businessId = findBusinessId(data);
        Business business = getBusiness(businessId);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        createNotification(notification, business);
    }

    private void createNotification(RemoteMessage.Notification notification, Business business) {
        FoodTruckNotification foodTruckNotification = new FoodTruckNotification();
        if (business == null) {
            foodTruckNotification.createNotification(getBaseContext(), notification);
            return;
        }
        try {
            Bitmap bitmap = Picasso.with(this)
                    .load(business.getImageUrl())
                    .get();
            foodTruckNotification.createNotification(getBaseContext(), notification, bitmap, business);
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            foodTruckNotification.createNotification(getBaseContext(), notification);
        }
    }

    @Nullable
    private String findBusinessId(Map<String, String> data) {
        if (data == null || data.isEmpty()) return null;
        if (!data.containsKey(ID)) return null;

        return data.get(ID);
    }

    @Nullable
    private Business getBusiness(String id) {
        if (TextUtils.isEmpty(id)) return null;

        SearchApi searchApi = RetrofitClient
                .createService(SearchApi.class, getBaseContext());

        Call<Business> callResults = searchApi.getBusiness(id);
        try {
            Response<Business> response = callResults.execute();
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }


}