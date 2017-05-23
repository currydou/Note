package com.curry.note;

import android.app.Application;

import com.curry.note.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.rrtoyewx.andskinlibrary.manager.SkinLoader;

/**
 * Created by curry on 2017/5/14.
 */

public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        // TODO: 5/18/2017  这个工具类要项目依赖
        Utils.init(this);
        SkinLoader.getDefault().init(this);

    }
}
