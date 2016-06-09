package com.friendlyarm.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    private SMSHandler smsHandler;
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        smsHandler = (SMSHandler) context;
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Object[] objs = (Object[]) bundle.get("pdus");//pdus:是一个短信协议
            SmsMessage[] smsmessages = new SmsMessage[objs.length];
            for (int i = 0;i < objs.length;i++){
                smsmessages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
                //发送方的号码
                String number = smsmessages[i].getOriginatingAddress();
                //获取短信内容
                String content = smsmessages[i].getDisplayMessageBody();
                System.out.println("收到的消息为："+content);
                smsHandler.message(content);
                Toast.makeText(context, number + "--" + content, Toast.LENGTH_SHORT).show();
            }
            abortBroadcast();//拦截系统的接收短信系统
        }
    }

    public static interface SMSHandler{
        public void message(String str);
    }
}
