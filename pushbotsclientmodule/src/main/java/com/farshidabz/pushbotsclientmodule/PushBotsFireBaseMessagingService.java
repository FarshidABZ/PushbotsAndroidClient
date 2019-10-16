package com.farshidabz.pushbotsclientmodule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.farshidabz.pushbotsclientmodule.utils.LocalMessagingHelper;
import com.farshidabz.pushbotsclientmodule.utils.ReactNativeJsonConverter;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 * Firebase Messaging Service
 */

public class PushBotsFireBaseMessagingService extends FirebaseMessagingService {

    /**
     * get new notification and show it
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() == null) {
            return;
        }

        buildLocalNotification(remoteMessage);
    }

    public void buildLocalNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            try {
                Bundle bundle = ReactNativeJsonConverter.convertToBundle(new JSONObject(data));
                LocalMessagingHelper helper = new LocalMessagingHelper(getApplicationContext());
                helper.sendNotification(bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
