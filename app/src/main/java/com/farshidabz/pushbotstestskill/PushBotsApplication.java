package com.farshidabz.pushbotstestskill;

import android.app.Application;

import com.farshidabz.pushbotsclientmodule.PushBots;
import com.farshidabz.pushbotsclientmodule.annotataion.LogLevel;

public class PushBotsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initPushBots();
    }

    private void initPushBots() {
        PushBots.with(this)
                .setLogLevel(LogLevel.DEBUG)
                .init();
    }
}
