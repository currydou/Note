package com.curry.note.listener;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by layer on 16/11/8.
 */
public interface IBmobListener {

    void loginSuccess();

    void loginFailure();

    void querySuccess(BmobObject object);

    void queryFailure(BmobException e);

    void queryAllSuccess(List<BmobObject> object);

    void queryAllFailure(BmobException e);

    void queryOrderSuccess(List<BmobObject> object);

    void queryOrderFailure(BmobException e);

}
