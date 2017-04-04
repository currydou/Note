package com.curry.signapp.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

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
import com.curry.signapp.constant.Constants;
import com.curry.signapp.db.LocationDao;

import java.util.List;

/**
 * 备份
 *
 * 只在服务里获取经纬度，用广播更新UI，但是好像更新不及时
 *
 */
public class MainActivity extends AppCompatActivity {


    private ListView lvLocation;
    private List<SimpleLocation> locationList;
    //
    // 定位相关
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private BitmapDescriptor mIconLocation;

    private boolean first = true;
    private HomeReceiver homeReceiver;


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
        //注册广播
        registerHomeReceiver();

    }


    private void initLocation() {
        mMapView = (MapView) findViewById(R.id.map);
        // 初始化比例，大概500米左右
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);

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


    @Override
    protected void onStart() {
        super.onStart();
        // 允许定位图层
        mBaiduMap.setMyLocationEnabled(true);
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
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //注销receiver
        unregisterReceiver(homeReceiver);
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


    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = (Location) intent.getSerializableExtra("location");
            if (first) {
                MyLocationData data = new MyLocationData.Builder()//
                        .latitude(location.getLatitude())// 纬度
                        .longitude(location.getLongitude())// 经度
                        .build();
                // 数据设置给map
                mBaiduMap.setMyLocationData(data);
                // 设置自定义图标（由圆点变成三角形），根据模式的不同更换图标
                MyLocationConfiguration config = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
                mBaiduMap.setMyLocationConfigeration(config);
                first = false;
            }
            Toast.makeText(getApplicationContext(), "activity  经度：" + location.getLongitude() + "  纬度:"
                    + location.getLatitude(), Toast.LENGTH_SHORT).show();
            centerToMyLocation(location.getLatitude(), location.getLongitude());
        }
    }

    private void registerHomeReceiver() {
        homeReceiver = new HomeReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_HOMERECEIVER);
        registerReceiver(homeReceiver, intentFilter);
    }
}
