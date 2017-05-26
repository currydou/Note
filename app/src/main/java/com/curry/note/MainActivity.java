package com.curry.note;

import android.content.Intent;
import android.os.Bundle;

import com.curry.note.base.BaseActivity;
import com.curry.note.module.login.LoginActivity;
import com.curry.note.module.main.NoteListActivity;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BmobUser currentUser = BmobUser.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, NoteListActivity.class));
        }
        finish();

    }
}
