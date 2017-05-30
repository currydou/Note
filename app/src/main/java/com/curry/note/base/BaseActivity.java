package com.curry.note.base;

import android.os.Bundle;

import com.curry.note.NoteApplication;
import com.rrtoyewx.andskinlibrary.base.BaseSkinActivity;

import cn.bmob.v3.Bmob;

public class BaseActivity extends BaseSkinActivity {

    protected NoteApplication noteApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 5/26/2017  其他项目这里是不是这样写的
        noteApplication = NoteApplication.getInstance();
        //第一：默认初始化
        Bmob.initialize(this, "be37ef7cc39a53617c68dddd3187f8b0");
        NoteApplication.getRefWatcher().watch(this);
    }

}
