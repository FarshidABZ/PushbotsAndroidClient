package com.farshidabz.pushbotsclientmodule;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.farshidabz.pushbotsclientmodule.utils.ReactNativeJsonConverter;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

/**
 * Created by Farshid since 17 Oct 2019
 * <p>
 * PushBotsNativeModule extended ReactContextBaseJavaModule to implement some useful methods that
 * could be used in JS code
 */

public class PushBotsNativeModule extends ReactContextBaseJavaModule {

    @SuppressLint("StaticFieldLeak")
    private static ReactApplicationContext mReactContext;

    public PushBotsNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return PushBotsNativeModule.class.getSimpleName();
    }

    /**
     * getFCMToken
     * <p>
     * A method to return user FCM token with promise to JS code
     */
    @ReactMethod
    public void getFCMToken(Promise promise) {
        try {
            promise.resolve(FirebaseInstanceId.getInstance().getToken());
        } catch (Throwable e) {
            e.printStackTrace();
            promise.reject(null, e.getMessage());
        }
    }

    @ReactMethod
    public void setNotificationClickListener() {
        PushBots.with(mReactContext).setNotificationClickListener(new NotificationClickListener() {
            @Override
            public void onNotificationClicked(Object result) {
                sendJSEvent("notificationClicked", ReactNativeJsonConverter.toWritableMap((JSONObject) result));
            }
        });
    }

    private void sendJSEvent(String eventName, WritableMap params) {
        mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
