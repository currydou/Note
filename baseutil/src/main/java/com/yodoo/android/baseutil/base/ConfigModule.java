package com.yodoo.android.baseutil.base;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

import com.yodoo.android.baseutil.base.delegate.AppDelegate;
import com.yodoo.android.baseutil.di.module.GlobalConfigModule;

/**
 * 此接口可以给框架配置一些参数,需要实现类实现后,并在AndroidManifest中声明该实现类
 */

public interface ConfigModule {
    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 使用{@link AppDelegate.Lifecycle}在Application的生命周期中注入一些操作
     * @return
     */
    void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
