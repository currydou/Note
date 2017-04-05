package com.curry.signapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.curry.signapp.bean.SimpleLocation;
import com.curry.signapp.db.LocationDao;


public class LocationService extends Service {

    //请求定位信息的间隔
    public static final int SPAN = 6000;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initAndStartLocation();
    }

    /**
     * 配置定位的参数
     */
    private void initAndStartLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //语义化信息
        option.setIsNeedLocationDescribe(true);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(SPAN);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        //开始定位
        mLocationClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //暂时没什么放这里的
        return super.onStartCommand(intent, flags, startId);
    }

    // 定位的监听
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
//            Toast.makeText(getApplicationContext(), "service  经度：" + location.getLongitude() + "  纬度:"
//                    + location.getLatitude(), Toast.LENGTH_SHORT).show();
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                String mLongitude = location.getLongitude() + "";
                String mLatitude = location.getLatitude() + "";
                //保存到数据库
                SimpleLocation simpleLocation = new SimpleLocation(System.currentTimeMillis() + "",
                        mLongitude, mLatitude);
                LocationDao.getInstance(getApplicationContext()).add(simpleLocation);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
