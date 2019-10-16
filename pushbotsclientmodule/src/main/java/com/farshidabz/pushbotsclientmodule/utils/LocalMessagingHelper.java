package com.farshidabz.pushbotsclientmodule.utils;

import android.content.Context;
import android.os.Bundle;

import com.farshidabz.pushbotsclientmodule.NotificationViewer;

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

    public LocalMessagingHelper(Context context) {
        mContext = context;
    }

    /**
     * sendNotification
     *
     * @param bundle is Bundle contains notifications data, like title and body
     */
    public void sendNotification(Bundle bundle) {
        new NotificationViewer(mContext).showNotification(bundle);
    }
}
