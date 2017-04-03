package com.curry.signapp.request.member;

import android.content.Context;

import com.curry.signapp.request.RequestResultListener;

/**
 * Created by curry on 2017/4/3.
 */

public interface MemberInterface {

    /**
     * 用户登录
     * @param context 上下文
     * @param userName 用户名
     * @param password 密码
     * @param listener 回调接口
     */
    void login(Context context,String userName, String password, RequestResultListener listener);

    void register();// TODO: 2017/4/3
}
