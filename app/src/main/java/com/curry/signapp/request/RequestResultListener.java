package com.curry.signapp.request;

/**
 * @author wangkai
 * @Description: App请求结果的回调接口
 * create at 2015/11/4 11:31
 */
public interface RequestResultListener {
    /**
     * 请求成功
     *
     * @param Json
     */
    void onSuccess(String Json);

    /**
     * 请求失败
     */
    void onFailed();

}
