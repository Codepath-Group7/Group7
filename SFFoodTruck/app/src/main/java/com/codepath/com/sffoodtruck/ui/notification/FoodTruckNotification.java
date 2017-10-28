package com.codepath.com.sffoodtruck.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.codepath.com.sffoodtruck.R;
import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.ui.businessdetail.BusinessDetailActivity;
import com.codepath.com.sffoodtruck.ui.util.DeviceDimensionsUtil;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

/**
 * Created by robl2e on 10/20/17.
 */

public class FoodTruckNotification {
    private static final int MESSAGE_NOTIFICATION_ID = 435345;

    // Creates notification based on title and body received
    public void createNotification(Context context, RemoteMessage.Notification notification) {
        createNotification(context, notification, null, null);
    }

    // Creates notification based on title and body received and business
    public void createNotification(Context context
            , RemoteMessage.Notification notification, Bitmap bitmap, Business business) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_truck_white_24dp)
                .setAutoCancel(true)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody());

        Bitmap largeIcon = getBitmapFromVectorDrawable(
                context, R.drawable.ic_food_truck);
        builder.setLargeIcon(largeIcon);

        if (bitmap != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(notification.getTitle())
                    .setSummaryText(notification.getBody())
                    .bigPicture(bitmap));/*Notification with Image*/
        }

        if (business != null) {
            PendingIntent pendingIntent = createPendingIntent(context, business);
            builder.setContentIntent(pendingIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build());
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        int size = (int) DeviceDimensionsUtil.convertDpToPixel(48, context);
        Bitmap bitmap = Bitmap.createBitmap(size,
                size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private PendingIntent createPendingIntent(Context context, Business business) {
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Intent resultIntent = BusinessDetailActivity.newIntent(context, business);
        PendingIntent piResult = PendingIntent.getActivity(context,
                (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);
        return piResult;
    }
}
