package com.curry.note.listener;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by curry on 2016/11/3.
 */

public interface IBmobLoginListener {
    void sendMsgSuccess();

    void loginSuccess();

    void querySuccess(List<BmobObject> object);

    void queryFailure(BmobException e);


}
