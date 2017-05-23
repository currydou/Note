package com.curry.note.base;

import android.os.Bundle;

import com.rrtoyewx.andskinlibrary.base.BaseSkinActivity;

import cn.bmob.v3.Bmob;

public class BaseActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一：默认初始化
        Bmob.initialize(this, "be37ef7cc39a53617c68dddd3187f8b0");
    }

}
