package com.curry.note.listener;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by layer on 16/11/8.
 */
public abstract class BmobQueryCallback implements IBmobListener {
    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailure() {

    }



    @Override
    public void queryAllSuccess(List<BmobObject> object) {

    }

    @Override
    public void queryAllFailure(BmobException e) {

    }
}
