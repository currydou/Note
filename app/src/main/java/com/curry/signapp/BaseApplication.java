package com.curry.signapp;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.baidu.mapapi.SDKInitializer;
import com.curry.signapp.db.LocationDao;
import com.curry.signapp.service.LocationService;
import com.curry.signapp.util.NetUtil;
import com.curry.signapp.util.SystemInfoUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;


/**
 * Created by curry on 2017/4/3.
 */

public class BaseApplication extends Application {

    public static OkHttpClient mOKHttpClient;
    public static Handler mDelivery;
    public static BaseApplication instance = null;
    private Handler controlHandler;
    private String locationProvider;


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
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());

        controlHandler = new Handler(getMainLooper());
        controlHandler.post(reportRunnable);

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



    Runnable reportRunnable = new Runnable() {
        @Override
        public void run() {
            //启动一个服务，防止进程空掉
            boolean serviceRunning = SystemInfoUtils.isServiceRunning(getApplicationContext(),
                    "com.curry.signapp.service.LocationService");
            if (!serviceRunning) {
                startService(new Intent(getApplicationContext(), LocationService.class));
            }
            //定时
            controlHandler.postDelayed(reportRunnable, TimeUnit.MINUTES.toMillis(5));

            // (没有网络的时候不删除)
            // TODO: 2017/4/4 是不是需要判断不同类的网络，因为有的网络可能收不到经纬度，在调研
            if (NetUtil.checkNet(getApplicationContext())) {
                // TODO: 2017/4/4  上报

                //删除之前的。
                LocationDao.getInstance(getApplicationContext()).deleteAll();
            }
// TODO: 2017/4/4  移动位置是否变化，5分钟是否删除，权限
        }
    };

    public void startRecordLocation() {
//        controlHandler.postDelayed(locationRunnable );
    }

    public void stopAllTimer() {
//        if(){
//
//        }
    }




}
