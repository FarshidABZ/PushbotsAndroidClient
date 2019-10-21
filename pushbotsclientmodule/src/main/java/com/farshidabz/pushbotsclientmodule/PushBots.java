package com.farshidabz.pushbotsclientmodule;

import android.annotation.SuppressLint;
import android.content.Context;
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

    private static Builder mBuilder = null;

    private static String TAG = "PushBots";

    public static Builder with(Context context) {
        mContext = context;
        if (mBuilder == null) {
            return new Builder();
        }

        return mBuilder;
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
            //TODO initialize PushBots here if we want. it most useful on android not react native
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage(), "something is wrong"));
        }
    }

    public static void notificationClicked(Bundle bundle) {
        if (mBuilder != null && mBuilder.mNotificationClickListener != null) {
            mBuilder.mNotificationClickListener.onNotificationClicked(bundle);
        }
    }

    /**
     * Builder class to set PushBots variable.
     */
    public static class Builder {
        @LogLevel
        String mLogLevel;

        NotificationClickListener mNotificationClickListener = null;

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
         */
        public Builder setNotificationClickListener(NotificationClickListener notificationClickListener) {
            mNotificationClickListener = notificationClickListener;
            return this;
        }

        /**
         * init PushBots
         */
        public void init() {
            PushBots.init(this);
        }
    }
}
