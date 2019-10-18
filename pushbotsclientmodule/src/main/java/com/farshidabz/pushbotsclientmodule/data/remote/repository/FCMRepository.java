package com.farshidabz.pushbotsclientmodule.data.remote.repository;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.farshidabz.pushbotsclientmodule.data.remote.network.HttpRequest;
import com.farshidabz.pushbotsclientmodule.data.remote.network.OnFailureCallback;
import com.farshidabz.pushbotsclientmodule.data.remote.network.OnResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Farshid since 18 Oct 2019
 * <p>
 * {@link FCMRepository}
 * <p>
 * Repository class to handle sending FCM api calls and some logic like storing tokens into db.
 */

public class FCMRepository {
    private static final String TAG = FCMRepository.class.getSimpleName();

    /**
     * sendFCMTokenToServer
     *
     * @param context is {@link Context} to read manifest placeholders like push bots appId.
     * @param token   is {@link String} is FCM user token
     *                <p>
     *                send user FCM token to PushBots server.
     */
    public static void sendFCMTokenToServer(Context context, String token) {


        new HttpRequest.Builder().setBody(getPushBotsSubscriptionsBody(token))
                .setRequestMethod(HttpRequest.RequestMethod.POST)
                .setToken(getAppId(context))
                .setUrl("http://api.pushbots.com/2/subscriptions")
                .run(new OnResponseCallback() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d(TAG, "onResponse");
                    }
                }, new OnFailureCallback() {
                    @Override
                    public void onFailure(Object throwable) {
                        Log.e(TAG, "onFailure");
                    }
                });
    }

    /**
     * getPushBotsSubscriptionsBody
     *
     * @param token is {@link String} FCM token
     * @return JSONObject return body
     * <p>
     * create {@link JSONObject} for push bots subscriptions api
     */
    private static JSONObject getPushBotsSubscriptionsBody(String token) {
        JSONObject body = new JSONObject();

        try {
            body.put("platform", 1);
            body.put("token", token);

            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return body;
        }
    }

    /**
     * getAppId
     *
     * @param context is {@link Context} to read manifest placeholders like push bots appId.
     * @return String, is push bots app id defined in manifest.
     * <p>
     * get manifest AppId variable defined in meta.data tag.
     */
    private static String getAppId(Context context) {
        ApplicationInfo ai;

        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String appId = bundle.getString("com.farshidabz.pushbotsclientmodule.AppId");

            if (appId != null) {
                return appId;
            }

            return "";

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
