package com.farshidabz.pushbotsclientmodule;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.firebase.iid.FirebaseInstanceId;

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
     *
     * A method to return user FCM token with promise to JS code
     *
     * @param promise {@link Promise} to get FCM token in callback
     * */
    @ReactMethod
    public void getFCMToken(Promise promise) {
        try {
            promise.resolve(FirebaseInstanceId.getInstance().getToken());
        } catch (Throwable e) {
            e.printStackTrace();
            promise.reject(null, e.getMessage());
        }
    }
}
