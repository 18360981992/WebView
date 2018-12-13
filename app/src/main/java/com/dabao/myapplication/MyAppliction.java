package com.dabao.myapplication;

import android.app.Application;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by zzt on 2018/9/14.
 */

public class MyAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.setDebugMode(true);
//        JAnalyticsInterface.initCrashHandler(this);

    }
}
