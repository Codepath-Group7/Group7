package com.codepath.com.sffoodtruck.infrastructure.service;

import com.codepath.com.sffoodtruck.ui.notification.FoodTruckNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMMessageHandlerService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String from = remoteMessage.getFrom();

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        FoodTruckNotification foodTruckNotification = new FoodTruckNotification();
        foodTruckNotification.createNotification(getBaseContext(), notification);
    }
}