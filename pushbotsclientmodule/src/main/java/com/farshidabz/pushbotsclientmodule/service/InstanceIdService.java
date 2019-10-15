package com.farshidabz.pushbotsclientmodule.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 * InstanceIdService is a FirebaseInstanceIdService
 * <p>
 * FirebaseInstanceIdService is deprecated, we can get new token on message service
 * or we can get from firebase instance.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    /**
     * onTokenRefresh
     * <p>
     * send New token to PushBot server
     */
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: 2019-10-15 send id to server
    }
}
