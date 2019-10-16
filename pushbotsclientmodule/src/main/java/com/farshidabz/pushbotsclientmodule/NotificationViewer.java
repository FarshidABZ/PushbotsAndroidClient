package com.farshidabz.pushbotsclientmodule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.net.URLDecoder;
import java.util.Objects;

import androidx.core.app.NotificationCompat;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 * <p>
 * A class to show notification with android NotificationCompat
 */
public class NotificationViewer {
    private static final long DEFAULT_VIBRATION = 300L;

    private static String TAG = "PushBots NotificationViewer";

    private Context mContext;

    public NotificationViewer(Context context) {
        mContext = context;
    }

    /**
     * showNotification
     *
     * @param bundle is Bundle contains notification title, body, and more data in future
     */
    public void showNotification(Bundle bundle) {
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(bundle);

        if (notificationBuilder == null) {
            return;
        }

        PendingIntent pendingIntent = getIntent(bundle);

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        notifyMessage(notificationBuilder);
    }

    /**
     * getIntent
     *
     * @param bundle is Bundle. a bundle that received from notification data.
     * @return PendingIntent.
     *
     * look for click_action in notification data to create pending action to navigate users to
     * the suitable destination
     * */
    private PendingIntent getIntent(Bundle bundle) {

        try {
            String intentClassName = getMainActivityClassName(mContext);
            if (intentClassName == null) {
                return null;
            }

            Intent intent = new Intent();
            intent.setClassName(mContext, intentClassName);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtras(bundle);

            String clickAction = bundle.getString("click_action");
            if (clickAction != null) clickAction = URLDecoder.decode(clickAction, "UTF-8");

            intent.setAction(clickAction);

            int notificationID = bundle.containsKey("id") ? bundle.getString("id", "").hashCode() :
                    (int) System.currentTimeMillis();

            return PendingIntent.getActivity(mContext, notificationID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            Log.e(TAG, "failed to find Intent", e);
            return null;
        }
    }

    /**
     * getNotificationBuilder
     *
     * set notification title, message, style, Icon and other related stuffs.
     * for now we just set some static icon and sounds, but we should gather them from PushBots.
     * Users should be able to set Icon, Color, Theme, Sound and something like that manually.
     *
     * @param bundle is Bundle. a bundle that received from notification data.
     * @return NotificationCompat.Builder to create a notification
     *
     * */
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
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bell))
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

    /**
     * notifyMessage
     *
     * @param builder is NotificationCompat.Builder to fire notification form created builder
     * */
    private void notifyMessage(NotificationCompat.Builder builder) {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, "Notifications", importance);

            // Create a notification and set the notification channel.
            Notification notification = builder
                    .setChannelId(channelID)
                    .build();

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
                notificationManager.notify(12312, notification);
            }
        } else {
            if (notificationManager != null) {
                notificationManager.notify(12312, builder.build());
            }
        }
    }

    /**
     * getMainActivityClassName
     *
     * @param context is Context to extract launch intent
     */
    private String getMainActivityClassName(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return launchIntent != null ? Objects.requireNonNull(launchIntent.getComponent()).getClassName() : null;
    }
}
