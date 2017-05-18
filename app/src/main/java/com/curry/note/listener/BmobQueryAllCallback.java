package com.curry.note.listener;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by layer on 16/11/8.
 */
public abstract class BmobQueryAllCallback implements IBmobListener {
    @Override
    public void queryOrderSuccess(List<BmobObject> object) {

    }

    @Override
    public void queryOrderFailure(BmobException e) {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailure() {

    }

    @Override
    public void querySuccess(BmobObject object) {

    }

    @Override
    public void queryFailure(BmobException e) {

    }


}
