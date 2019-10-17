package com.farshidabz.pushbotsclientmodule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import androidx.core.app.NotificationManagerCompat;

/**
 * Created by Farshid since 17 Oct 2019
 * <p>
 * PushBotsBroadcastHandler extended from BroadcastReceiver to handle notification click listener
 * <p>
 * of course we can set destination activity on click_action value inside our data message,
 * but if we want to detect notification click listener before opening the activity
 * we can raise a broadcast and listen it.
 */

public class PushBotsBroadcastHandler extends BroadcastReceiver {

    private static final String OPEN_NOTIF_ACTION = "com.farshidabz.pushbotsclientmodule.MESSAGE_OPEND";
    private static final String TAG = PushBotsBroadcastHandler.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "on notification clicked");

        if (Objects.requireNonNull(intent.getAction(), "").equals(OPEN_NOTIF_ACTION)) {
            Bundle bundle = intent.getExtras().getBundle(OPEN_NOTIF_ACTION);
            openApplication(context, bundle);

            // send notification click callback, maybe users wants them
            PushBots.notificationClicked(bundle);

            NotificationManagerCompat.from(context).cancelAll();
        }
    }

    /**
     * openApplication
     *
     * @param context is {@link Context}
     * @param bundle  is {@link Bundle} is notification data
     *
     * look notification data to check destination activity
     * if destination is exist open it, else open application launcher activity
     */

    private void openApplication(Context context, Bundle bundle) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));

        if (bundle != null && !bundle.isEmpty()) {
            String destinationActivity = bundle.getString("activity");
            try {
                if (destinationActivity != null && !destinationActivity.isEmpty()) {
                    intent = new Intent(context, Class.forName(destinationActivity));
                }
            } catch (Exception e) {
                Log.e(TAG, "creating intent for destination activity", e);
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
