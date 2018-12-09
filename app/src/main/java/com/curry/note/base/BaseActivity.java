package com.curry.note.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.curry.note.NoteApplication;

public class BaseActivity extends AppCompatActivity {

    protected NoteApplication noteApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  其他项目这里是不是这样写的
//        noteApplication = NoteApplication.getInstance();
//        星巴克是在要用的组件里面这样获取到对象，再调用。没有写在基类里
//        NoteApplication noteApplication = (NoteApplication) getApplicationContext();
//       但是为什么不这样写：
//        NoteApplication noteApplication = (NoteApplication) getApplication();
//        其他的项目一般没在application里new 实例，都是用的时候直接getInstance()
        //第一：默认初始化
        Bmob.initialize(this, "be37ef7cc39a53617c68dddd3187f8b0");
//        RefWatcher refWatcher = NoteApplication.getRefWatcher(this);
//        refWatcher.watch(this);
    }

}
