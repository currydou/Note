package com.curry.note;

import android.app.Application;

import com.curry.note.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.rrtoyewx.andskinlibrary.manager.SkinLoader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by curry on 2017/5/14.
 */

public class NoteApplication extends Application {

    private static NoteApplication instance;
    private RefWatcher mRefWatcher;

    public static NoteApplication getInstance() {
        if (instance == null) {
            synchronized (NoteApplication.class) {
                if (instance == null) {
                    instance = new NoteApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Utils.init(this);
        SkinLoader.getDefault().init(this);
        ShareSDK.initSDK(this);
        mRefWatcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;

    }

    public static RefWatcher getRefWatcher() {
        return getInstance().mRefWatcher;
    }
}
