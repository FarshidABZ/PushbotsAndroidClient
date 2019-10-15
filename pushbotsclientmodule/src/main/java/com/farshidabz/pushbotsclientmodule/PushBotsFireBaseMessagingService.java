package com.farshidabz.pushbotsclientmodule;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
        super.onMessageReceived(remoteMessage);
        //TODO show notification
    }
}
