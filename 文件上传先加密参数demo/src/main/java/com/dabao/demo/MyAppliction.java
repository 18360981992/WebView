package com.dabao.demo;

import android.app.Application;
import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zzt on 2018/4/20.
 */

public class MyAppliction extends Application {
    // 这是为了打印retrofit的log日志
    //打印retrofit日志
    public static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.i("RetrofitLog","retrofitBack = "+message);
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        // 对log 的初始化
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

}
