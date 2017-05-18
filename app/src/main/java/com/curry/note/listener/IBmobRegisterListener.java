package com.curry.note.listener;

import cn.bmob.v3.BmobUser;

/**
 * 注册接口
 * Created by curry on 2016/11/13.
 */

public interface IBmobRegisterListener {
    void registerSuccess(BmobUser bmobUser);
    void registerFailure(Exception e);
}
