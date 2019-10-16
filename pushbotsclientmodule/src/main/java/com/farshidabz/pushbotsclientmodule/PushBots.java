package com.farshidabz.pushbotsclientmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.farshidabz.pushbotsclientmodule.annotataion.LogLevel;

import java.util.Objects;

/**
 * Created by Farshid since 15 Oct 2019
 * <p>
 * A singleton class to initialized PushBot client module.
 */
public class PushBots {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static Builder mBuilder;

    private static String TAG = "PushBots";

    public static Builder with(Context context) {
        mContext = context;
        return new Builder();
    }

    /**
     * Init PushBots
     *
     * @param builder is PushBots [{@link Builder}]
     */
    private static void init(Builder builder) {
        if (builder == null) {
            mBuilder = new Builder();
        }

        try {
            // read application id and sender id from application meta data.
            ApplicationInfo applicationInfo = mContext
                    .getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);

            Bundle bundle = applicationInfo.metaData;
            String sender_id = bundle.getString("push_bot_sender_id");

            // todo set application id if necessary, initialize other things

        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage(), "something is wrong"));
        }
    }

    /**
     * Builder class to set PushBots variable.
     */
    public static class Builder {
        @LogLevel
        String mLogLevel;

        NotificationClickListener mNotificationClickListener;

        private Builder() {
        }

        /**
         * setLogLevel to set PushBots logging strategy
         *
         * @param logLevel is [{@link LogLevel}]
         *                 if [LogLevel.DEBUG] show all logs
         *                 if [LogLevel.RELEASE] doesn't show any log
         */
        public Builder setLogLevel(@LogLevel String logLevel) {
            mLogLevel = logLevel;
            return this;
        }

        /**
         * setNotificationClickListener to handle notification click listener
         *
         *
         * */
        public Builder setNotificationClickListener(NotificationClickListener mNotificationClickListener) {
            mNotificationClickListener = mNotificationClickListener;
            return this;
        }

        /**
         * init PushBots
         * */
        public void init() {
            PushBots.init(this);
        }
    }
}
