package com.curry.signapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.OkHttpClient;


/**
 * Created by curry on 2017/4/3.
 */

public class BaseApplication extends Application {

    public static OkHttpClient mOKHttpClient;
    public static Handler mDelivery;
    public static BaseApplication instance = null;


    public static BaseApplication getInstance() {
        if (instance == null) {
            synchronized (BaseApplication.class) {
                if (instance == null) {
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initOKHttp();
    }

    /**
     * 初始化OKHttp请求
     */
    private void initOKHttp() {
        mOKHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取网络请求队列
     *
     * @return mRequestQueue
     */

    public static OkHttpClient getOKHttpClient() {
        return mOKHttpClient;
    }

    public static Handler getDelivery() {
        return mDelivery;
    }
}
