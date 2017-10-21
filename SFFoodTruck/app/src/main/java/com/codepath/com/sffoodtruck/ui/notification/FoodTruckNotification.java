package com.codepath.com.sffoodtruck.ui.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.codepath.com.sffoodtruck.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by robl2e on 10/20/17.
 */

public class FoodTruckNotification {
    private static final int MESSAGE_NOTIFICATION_ID = 435345;

    // Creates notification based on title and body received
    public void createNotification(Context context, RemoteMessage.Notification notification) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notification.getTitle())
                .setContentText(notification.getBody());
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}
