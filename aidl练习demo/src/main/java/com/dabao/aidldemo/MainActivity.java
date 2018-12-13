package com.dabao.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dabao.myapplication.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    public IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent("com.dabao.myapplication.MyService"), new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service){

                iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name){

            }
        }, BIND_AUTO_CREATE);

    }

    public void btn(View view) {
        try{
            Toast.makeText(MainActivity.this, iMyAidlInterface.getName()+"", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "您没有安装第三方应用", Toast.LENGTH_SHORT).show();
        }

    }
}
