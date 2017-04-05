package com.curry.signapp.module.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.curry.signapp.R;
import com.curry.signapp.adapter.LocationAdapter;
import com.curry.signapp.bean.SimpleLocation;
import com.curry.signapp.db.LocationDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView lvLocation;
    private List<SimpleLocation> locationList;
    //
    // 定位相关
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private BitmapDescriptor mIconLocation;

    private boolean first = true;
    private LocationClient mLocationClient;
    private float mCurrentX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        try {
            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
        } catch (NoSuchFieldException e) {
            // Ignore since this field won't exist in most versions of Android
        } catch (IllegalAccessException e) {
            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);
        }

        setContentView(R.layout.activity_main);
        lvLocation = (ListView) findViewById(R.id.lvLocation);

        // TODO: 2017/4/4 进程优先级问题
        initLocation();

    }


    private void initLocation() {
        mMapView = (MapView) findViewById(R.id.map);
        // 初始化比例，大概500米左右
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());
        //注册监听函数

        //配置定位参数
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //语义化信息
        option.setIsNeedLocationDescribe(true);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(3000);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否需要位置语义化结果
        option.setIsNeedLocationDescribe(true);

        //可选，默认gcj02，设置返回的定位结果坐标系（--------》没有这个参数定位不准确，相关介绍还得看看）
        option.setCoorType("bd09ll");
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
        // 罗盘模式， 初始化图
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked);
    }


    /**
     * 定位到我的位置
     */
    private void centerToMyLocation(double latitude, double longtitude) {
        LatLng latLng = new LatLng(latitude, longtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    // 定位的监听
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)// 罗盘模式，方向的设置
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())// 纬度
                    .longitude(location.getLongitude())// 经度
                    .build();
            // 数据设置给map
            mBaiduMap.setMyLocationData(data);
            // 设置自定义图标（由圆点变成三角形），根据模式的不同更换图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            // 是否第一次进入，把当前位置设置为中心点
            if (first) {
                centerToMyLocation(location.getLatitude(), location.getLongitude());
                first = false;
            }
//            Toast.makeText(MainActivity.this, location.getTime() + "经度：" + location.getLongitude() + "  纬度:"
//                    + location.getLatitude(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 允许定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            // 开启定位
            mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locationList = LocationDao.getInstance(getApplicationContext()).findAll();
                System.out.println(locationList);
                final LocationAdapter locationAdapter = new LocationAdapter(MainActivity.this, locationList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvLocation.setAdapter(locationAdapter);
                    }
                });
            }
        }).start();
    }


}
