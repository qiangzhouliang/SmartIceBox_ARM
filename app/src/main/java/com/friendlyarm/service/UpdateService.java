package com.friendlyarm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.Utils.Constant;
import com.friendlyarm.Utils.Sendmessage;
import com.friendlyarm.Utils.util;
import com.friendlyarm.application.QzlApplication;
import com.friendlyarm.broadcastReceiver.SMSReceiver;
import com.friendlyarm.user.Note;
import com.friendlyarm.user.Temp;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * started service:服务同时只会被创建一次，可以通过外部调运stopService或调运stopSelf来终止服务
 */
public class UpdateService extends Service implements SMSReceiver.SMSHandler{

    private String stt_foodList = "";//获取食物列表信息，通过短信发送出去
    private String stt_foodDeadline = "";//存储将要过期或已过期食品
    private String phone;//存储短信电话号码
    private QzlApplication app;

    private String devName = "/dev/s3c2410_serial0";
    private int speed = 115200;
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    //服务被创建
    @Override
    public void onCreate() {
        super.onCreate();
        app = new QzlApplication();
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        readflag();
                    }
                }
            }).start();
        } else {
            devfd = -1;
            Toast.makeText(this, "打开串口出错", Toast.LENGTH_SHORT).show();
        }
    }
    private final int BUFSIZE = 4;
    private byte[] buf = new byte[BUFSIZE];
    private void readflag() {
        if (HardwareControler.select(devfd, 0, 0) == 1) {
            int retSize = HardwareControler.read(devfd, buf, BUFSIZE);
            if (retSize > 0) {
                String[] str1 = util.Bytes2HexString_1(buf,1);
                if(str1[0].equals("01") || str1[0].equals("02")) {
                    //上传温度与数据库
                    temperatureTo(str1[0]);
                }
            }
        }
    }

    private void temperatureTo(String st) {
        Temp t = new Temp();
        t.setOpenflag(st);
        //更新数据
        t.update(this, "10166ef327", new UpdateListener() {
            @Override
            public void onSuccess() {
                //保存成功
                //Toast.makeText(getApplicationContext(), "修改数据成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "修改数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //再该方法中实现核心业务
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phone = intent.getStringExtra("PhoneNumber");//拿到电话号码
        System.out.println("电话号码是："+phone);
        //更新保质期
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 30);//86400000
                        loadData(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    //从后台取出数据
    private void loadData(final int in) {
        //直接查询
        BmobQuery<Note> query = new BmobQuery<>();
        query.setLimit(50);//限定条数（默认是10条）
        //查询多个
        query.findObjects(this, new FindListener<Note>() {
            @Override
            public void onSuccess(final List<Note> list) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            //检查是否有过期的食品
                            checkDeadline(list);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                for (int i = 0; i < list.size(); i++) {
                    final Note note = list.get(i);
                    //短信查询食品的功能
                    if (in == 1) {
                        stt_foodList += "食物名称：" + note.getFoodname() + "、" + "数量：" + note.getNum() + "、" + "保质期：" + note.getShelf_life() + "、" + "购买日期：" + note.getCreatedAt() + "\n";
                        if (i == (list.size() - 1)) {
                            //将字符串发送出去
                            Sendmessage.sendMessageClick(stt_foodList, phone);
                            stt_foodList = "";
                        }
                    }
                    String id = note.getObjectId();//拿到id
                    int num_flag = Integer.parseInt(note.getShelf_life());
                    String num_shelf_life = (num_flag - 1) + "";
                    note.setShelf_life(num_shelf_life);

                    note.update(UpdateService.this, id, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //检查是否有过期的食品
    private void checkDeadline(List<Note> list) {
        for (int i = 0; i <list.size(); i++){
            Note note = list.get(i);
            if(app.flag_date >= Integer.parseInt(note.getShelf_life())){
                stt_foodDeadline += "食物名称：" + note.getFoodname() + "、" + "数量：" + note.getNum() + "、" + "保质期：" + note.getShelf_life() + "、" + "购买日期：" + note.getCreatedAt() + "\n";
                if(i == list.size()-1){
                    //System.out.println(stt_foodDeadline);
                    Sendmessage.sendMessageClick(stt_foodDeadline, phone);
                    stt_foodDeadline = "";
                }
            }
        }
    }

    //服务被销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("服务停止");
    }

    //通过接收到的短信消息来判断该做什么事
    @Override
    public void message(String str) {
        //食品查询
        if(str.equals(Constant.Food_query)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadData(1);
                }
            }).start();
        }else if(str.equals("")){//购物清单

        }
    }

}
