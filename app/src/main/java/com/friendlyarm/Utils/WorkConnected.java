package com.friendlyarm.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Q on 2016-04-03.
 */
public class WorkConnected {
    //检查网络是否可用
    public boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();//拿到已经激活的网络信息
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //检查WiFi网络是否可用
    public boolean isWifiConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//拿到已经激活的网络信息
            if(mWifiNetworkInfo != null){
                return mWifiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //检查MOBILE网络是否可用
    public boolean isMobileConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//拿到已经激活的网络信息
            if(mMobileNetworkInfo != null){
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //检查网络类型
    public static int getConnectedType(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();//拿到已经激活的网络信息
            if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
