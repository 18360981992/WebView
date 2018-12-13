package com.dabao.androidjsdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

/**
 * Created by zzt on 2018/4/20.
 */

public class MyAppliction extends Application {
    public static MyWebView myWebView;
    //在自定义的webview中加上一个单例方法
    public static MyWebView getInstance(Context context){
        if(myWebView == null){
            myWebView = new MyWebView(context);
        }
        return myWebView;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        MyAppliction mInstance = this;
        getInstance(getApplicationContext());

//        //判断当前进程是否主进程，在主进程中打开一个EmptyActivity
//        if(SysUtils.isMainProcess(getApplicationContext())){
//            mainPrecessInit();
//            Intent intent = new Intent(this,EmptyActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }

//        //判断是否:web进程，在：web进程中初始化全局webview
//        if(isProessRunning(this, "com.dabao.androidjsdemo:web")){
//            Log.i("jba","com.dabao.androidjsdemo:web====是web进程。。。。");
//            //web process
//            getInstance(getApplicationContext());
//            return;
//        }

    }

    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

}
