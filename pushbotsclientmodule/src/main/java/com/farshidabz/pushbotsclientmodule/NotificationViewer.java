package com.farshidabz.pushbotsclientmodule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.util.Random;

import androidx.core.app.NotificationCompat;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 * <p>
 * A class to show notification with android NotificationCompat
 */
public class NotificationViewer {
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
    public void showNotification(NotificationCompat.Builder bundle) {
        notifyMessage(bundle);
    }

    /**
     * notifyMessage
     *
     * @param builder is NotificationCompat.Builder to fire notification form created builder
     */
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
                notificationManager.notify((new Random()).nextInt(), notification);
            }
        } else {
            if (notificationManager != null) {
                notificationManager.notify((new Random()).nextInt(), builder.build());
            }
        }
    }
}
