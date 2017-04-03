package com.curry.signapp.request.member;

import android.content.Context;

import com.curry.signapp.constant.URLConfig;
import com.curry.signapp.request.OKHttpRequest;
import com.curry.signapp.request.RequestResultListener;
import com.curry.signapp.request.RequestTag;
import com.curry.signapp.util.SignAppLog;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by curry on 2017/4/3.
 */

public class MemberManager implements MemberInterface {

    private static MemberManager instance;

    public static MemberManager getInstance() {
        if (instance == null) {
            synchronized (MemberManager.class) {
                if (instance == null) {
                    instance = new MemberManager();
                }
            }
        }
        return instance;
    }

    /**
     * 登录
     *
     * @param context  上下文
     * @param userName 用户名
     * @param password 密码
     * @param listener 回调接口
     */
    @Override
    public void login(Context context, String userName, String password, RequestResultListener listener) {
        String url = URLConfig.BASEADDRESS + "";
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", password + "");
        SignAppLog.e("login params======" + new Gson().toJson(map));
        OKHttpRequest.RequestPost(context, url, RequestTag.MEMBER_LOGIN, map, listener);
    }

    @Override
    public void register() {

    }
}
