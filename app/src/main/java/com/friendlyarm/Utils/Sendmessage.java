package com.friendlyarm.Utils;
import android.telephony.SmsManager;
import java.util.ArrayList;

/**
 * Created by Q on 2016-05-23.
 */
public class Sendmessage {
    //发送短信
    public static void sendMessageClick(String message,String num){
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
        //拆分长短信
        ArrayList<String> list = smsManager.divideMessage(message);

        System.out.println("list size :"+list.size());
        int size = list.size();
        for (int i = 0; i < size; i++){
            smsManager.sendTextMessage(num,null,list.get(i),null,null);
        }
    }
}
