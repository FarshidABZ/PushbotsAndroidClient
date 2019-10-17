package com.farshidabz.pushbotsclientmodule.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.farshidabz.pushbotsclientmodule.NotificationViewer;
import com.farshidabz.pushbotsclientmodule.PushBotsBroadcastHandler;
import com.farshidabz.pushbotsclientmodule.R;

import java.net.URLDecoder;

import androidx.core.app.NotificationCompat;

/**
 * Created by Farshid since 16 Oct 2019
 *
 * <p>
 * <p>
 * A helper class to handle notifications,
 * like fire schedules notification, cancel notifications and etc.
 */

public class LocalMessagingHelper {
    private Context mContext;

    private static String TAG = "PushBots LocalMessagingHelper";
    private static final long DEFAULT_VIBRATION = 300L;

    public LocalMessagingHelper(Context context) {
        mContext = context;
    }

    /**
     * sendNotification
     *
     * @param bundle is Bundle contains notifications data, like title and body
     */
    public void sendNotification(Bundle bundle) {
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(bundle);
        if (notificationBuilder == null) {
            return;
        }

        PendingIntent pendingIntent = getIntent(bundle);

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        new NotificationViewer(mContext).showNotification(notificationBuilder);
    }

    /**
     * getIntent
     *
     * @param bundle is Bundle. a bundle that received from notification data.
     * @return PendingIntent.
     * <p>
     * look for click_action in notification data to create pending action to navigate users to
     * the suitable destination
     */
    private PendingIntent getIntent(Bundle bundle) {
        Intent broadcastIntent = new Intent("com.farshidabz.pushbotsclientmodule.MESSAGE_OPEND");
        broadcastIntent.putExtra("data", bundle);
        broadcastIntent.putExtra("com.farshidabz.pushbotsclientmodule.MESSAGE_OPEND", bundle);

        broadcastIntent.setClass(this.mContext, PushBotsBroadcastHandler.class);

        int notificationID = bundle.containsKey("id") ? bundle.getString("id", "").hashCode() :
                (int) System.currentTimeMillis();

        return PendingIntent.getBroadcast(mContext, notificationID, broadcastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * getNotificationBuilder
     * <p>
     * set notification title, message, style, Icon and other related stuffs.
     * for now we just set some static icon and sounds, but we should gather them from PushBots.
     * Users should be able to set Icon, Color, Theme, Sound, Channel and something like that manually.
     *
     * @param bundle is Bundle. a bundle that received from notification data.
     * @return NotificationCompat.Builder to create a notification
     */
    private NotificationCompat.Builder getNotificationBuilder(Bundle bundle) {
        try {
            String body = bundle.getString("body");

            if (body != null) {
                body = URLDecoder.decode(body, "UTF-8");
            }

            String title = bundle.getString("title");
            if (title == null) {
                ApplicationInfo appInfo = mContext.getApplicationInfo();
                title = mContext.getPackageManager().getApplicationLabel(appInfo).toString();
            }

            title = URLDecoder.decode(title, "UTF-8");

            // create NotificationCompat builder with some default values and title and body
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "marketing");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
            } else {
                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            }

            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.bell)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setCategory("Feeds")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setVibrate(new long[]{0, DEFAULT_VIBRATION})
                    .setContentInfo("Notification");

            return notificationBuilder;
        } catch (Exception e) {
            Log.e(TAG, "failed build notification", e);
            return null;
        }
    }
}
