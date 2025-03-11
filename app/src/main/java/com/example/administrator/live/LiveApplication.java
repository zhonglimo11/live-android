package com.example.administrator.live;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import androidx.room.Room;

import com.danikula.videocache.HttpProxyCacheServer;

public class LiveApplication extends Application {

    public static LiveApplication sApplication;
    private static HttpProxyCacheServer proxy;

    public static int screenWidth;
    public static int screenHeight;
    private static Context mContext;


    public static LiveApplication getApplication() {
        return sApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mContext = this;
        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;
    }

    public static Context getContext() {
        return mContext;
    }


    public static HttpProxyCacheServer getProxy(Context context) {
        return proxy == null ? (proxy = newProxy(context)) : proxy;
    }

    public static HttpProxyCacheServer getProxy() {
        return newProxy(mContext);
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .build();
    }


}
