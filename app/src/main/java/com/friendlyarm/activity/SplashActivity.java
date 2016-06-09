package com.friendlyarm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.WindowManager;

import com.friendlyarm.LEDDemo.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //3秒钟之后启动Activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 3000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,LoginMainActivity.class);
        startActivity(intent);
        finish();
    }

    private final MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<SplashActivity> weakReference;
        public MyHandler(SplashActivity activity){
            weakReference = new WeakReference<SplashActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

}
