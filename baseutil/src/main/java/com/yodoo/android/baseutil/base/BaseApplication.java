package com.yodoo.android.baseutil.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.yodoo.android.baseutil.base.delegate.AppDelegate;
import com.yodoo.android.baseutil.di.component.AppComponent;
import com.yodoo.android.baseutil.sp.SPUtil;
import com.yodoo.android.baseutil.utils.Utils;

/**
 * Created by lib on 2017/7/12.
 */

public class BaseApplication extends MultiDexApplication implements App {
    private AppDelegate mAppDelegate;
    public static String GET_BASE_URL = "http://dev.feikongbao.net";
    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil.init(this);
        //初始化工具
        Utils.init(this);
        this.mAppDelegate.onCreate(this);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
