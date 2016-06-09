package com.friendlyarm.application;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.bmob.v3.Bmob;

/**
 * Created by Q on 2016-05-18.
 */
public class QzlApplication extends Application {
    public static int flag_date = 3;//默认保质期天数为 3 天
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化语音配置对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= 572ef5b8");
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "7111b1b0ca4263017a2765f3cdeacfd2");
    }
}
