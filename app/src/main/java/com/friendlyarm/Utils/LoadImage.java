package com.friendlyarm.Utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/28.
 */
public class LoadImage extends Thread {
    private ImageView imageView;
    private String URL;
    private  List<Map<String, String>> list;
    private  Bitmap [] bitmap;
    public Handler handler = new Handler();
    public LoadImage(List<Map<String ,String>> list) {
        this.imageView = imageView;
        this.URL = URL;
        this.list=list;
        bitmap=new Bitmap[list.size()];
    }
    @Override
    public void run() {
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                bitmap[i]=HttpUtil.loadImage(list.get(i).get("img").toString());
            }
        }
    }
}
