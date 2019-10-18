package com.farshidabz.pushbotsclientmodule.service;

import com.farshidabz.pushbotsclientmodule.data.remote.repository.FCMRepository;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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
        FCMRepository.sendFCMTokenToServer(getApplicationContext(), refreshedToken);
    }
}
