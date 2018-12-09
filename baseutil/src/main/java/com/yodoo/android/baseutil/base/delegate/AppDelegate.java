package com.yodoo.android.baseutil.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.yodoo.android.baseutil.base.ActivityLifeCycle;
import com.yodoo.android.baseutil.base.App;
import com.yodoo.android.baseutil.base.ConfigModule;
import com.yodoo.android.baseutil.di.component.AppComponent;
import com.yodoo.android.baseutil.di.component.DaggerAppComponent;
import com.yodoo.android.baseutil.di.module.AppModule;
import com.yodoo.android.baseutil.di.module.ClientModule;
import com.yodoo.android.baseutil.di.module.GlobalConfigModule;
import com.yodoo.android.baseutil.imageloader.glide.GlideImageConfig;
import com.yodoo.android.baseutil.utils.ManifestParser;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by lib on 2017/7/25.
 */

public class AppDelegate implements App {
    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject
    protected ActivityLifeCycle mActivityLifecycle;
    private final List<ConfigModule> mModules;
    private List<Lifecycle> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Context context) {
        this.mModules = new ManifestParser(context).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    public void onCreate(Application application) {
        this.mApplication = application;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))//提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))//全局配置
                .build();
        mAppComponent.inject(this);

        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        for (Lifecycle lifecycle : mAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }

        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);

        mApplication.registerComponentCallbacks(mComponentCallback);

    }

    public void onTerminate() {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (Lifecycle lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mAppLifecycles = null;
        this.mApplication = null;
    }

    public void attachBaseContext(Context base) {
        for (Lifecycle lifecycle : mAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public interface Lifecycle {
        void attachBaseContext(Context base);

        void onCreate(Application application);

        void onTerminate(Application application);
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {

        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
            mAppComponent.imageLoader().clear(mApplication, GlideImageConfig
                    .builder()
                    .isClearMemory(true)
                    .build());
        }
    }
}
