package com.friendlyarm.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


import com.friendlyarm.user.Food;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by Q on 2016-05-19.
 */
public class Load {
    private static LoadCompare loadCompare;
    //查找数据库图片路径
    public static void load_Photo(Context context,final String photo ) {
        loadCompare = (LoadCompare) context;
        String bql = "select * from Food where photo = ?";//根据id号查询
        new BmobQuery<Food>().doSQLQuery(context, bql, new SQLQueryListener<Food>() {
            @Override
            public void done(BmobQueryResult<Food> result, BmobException e) {
                if (e == null) {
                    List<Food> list = (List<Food>) result.getResults();
                    if (list != null && list.size() > 0) {
                        //将查到的食品存入里面
                        for (int i = 0; i < list.size(); i++) {
                            Food food = list.get(i);
                            BmobFile bmobFile = food.getIcon();
                            final String ur = "http://file.bmob.cn/" + bmobFile.getUrl();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //加载图片
                                    getPicture(ur, photo);
                                }
                            }).start();
                        }

                    } else {
                        Log.i("smile", "查询成功，无数据返回");
                    }
                } else {
                    Log.i("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        }, photo);
    }

    //下载图片的主方法
    public static void getPicture(String s, String photo) {

        System.out.println("下载图片");
        URL url = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //构建图片的url地址
             url = new URL(s);
            System.out.println("+++++"+s);
            //               http://file.bmob.cn/M03/79/E7/oYYBAFc9I-uANp-wAABRJa1UPCU449.png
            //url = new URL("http://file.bmob.cn/M03/79/E8/oYYBAFc9JB2ADsrFAABRMVjXLeE636.png");
            //开启连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时的时间，5000毫秒即5秒
            conn.setConnectTimeout(5000);
            //设置获取图片的方式为GET
            conn.setRequestMethod("GET");
            //响应码为200，则访问成功
            if (conn.getResponseCode() == 200) {
                //获取连接的输入流，这个输入流就是图片的输入流
                is = conn.getInputStream();
                //构建一个file对象用于存储图片
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+photo;
                File file = new File(path);
                fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                //将输入流写入到我们定义好的文件中
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                loadCompare.strPath(path);
                System.out.println("图片下载成功");
            }
        } catch (Exception e) {
            //告诉handler，图片已经下载失败
            //handler.sendEmptyMessage(LOAD_ERROR);
            e.printStackTrace();
        } finally {
            //在最后，将各种流关闭
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static interface LoadCompare{
        public void strPath(String path);
    }
}
