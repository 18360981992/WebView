package com.dabao.androidjsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseActivity<V> extends AppCompatActivity {

    public abstract void getLodeurl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
//            getLodeurl();// 开始加载webview
            Log.i("jba","BaseActivity====什么情况。。。");
            getLodeurl();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {  // 获取手机串码
            if (grantResults[0] != -1) {  // 设置成功
                getLodeurl();
            } else {
                return;
            }
        }
    }
}
