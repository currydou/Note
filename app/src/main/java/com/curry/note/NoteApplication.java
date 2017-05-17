package com.curry.note;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by curry on 2017/5/14.
 */

public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

    }
}
