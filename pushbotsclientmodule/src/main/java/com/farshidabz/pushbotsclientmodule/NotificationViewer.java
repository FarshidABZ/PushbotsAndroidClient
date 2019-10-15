package com.farshidabz.pushbotsclientmodule;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.net.URLDecoder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 *
 * A class to show notification with android NotificationCompat
 */
public class NotificationViewer {
    private static final long DEFAULT_VIBRATION = 300L;

    /**
     * showNotification
     *
     * @param context is application context
     * @param bundle is Bundle contains notification title, body, and more data in future
     * */
    public void showNotification(Context context, Bundle bundle) {
        try {
            String intentClassName = getMainActivityClassName(context);
            if (intentClassName == null) {
                return;
            }

            String body = bundle.getString("body");

            if (body != null) {
                body = URLDecoder.decode(body, "UTF-8");
            }

            Resources res = context.getResources();
            String packageName = context.getPackageName();

            String title = bundle.getString("title");
            if (title == null) {
                ApplicationInfo appInfo = context.getApplicationInfo();
                title = context.getPackageManager().getApplicationLabel(appInfo).toString();
            }

            title = URLDecoder.decode(title, "UTF-8");

            // create NotificationCompat builder with some default values and title and body
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, bundle.getString("channel", "marketing"))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setVibrate(new long[]{0, DEFAULT_VIBRATION})
                    .setContentInfo("Notification")
                    .setExtras(bundle.getBundle("data"));

            // create intent to handle click listener and also deep link maybe in future
            Intent intent = new Intent();
            intent.setClassName(context, intentClassName);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtras(bundle);

            String clickAction = bundle.getString("click_action");
            if (clickAction != null) clickAction = URLDecoder.decode(clickAction, "UTF-8");

            intent.setAction(clickAction);

            int notificationID = bundle.containsKey("id") ? bundle.getString("id", "").hashCode() : (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notification.setContentIntent(pendingIntent);

            Notification info = notification.build();

            NotificationManagerCompat.from(context).notify(notificationID, info);

        } catch (Exception e) {
            Log.e(">>>>", "failed to send local notification", e);
        }
    }

    /**
     * getMainActivityClassName
     *
     * @param context is Context to extract launch intent
     * */
    private String getMainActivityClassName(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return launchIntent != null ? launchIntent.getComponent().getClassName() : null;
    }
}
