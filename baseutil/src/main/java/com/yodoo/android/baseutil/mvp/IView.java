package com.yodoo.android.baseutil.mvp;

import android.content.Intent;
import android.support.v4.app.DialogFragment;

/**
 * Created by lib on 2017/7/18.
 */

public interface IView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent, Class target);

    /**
     * 跳转activity
     */
    void launchActivityForResult(Intent intent, Class target, int requestCode);

    /**
     * 杀死自己
     */
    void killMyself();

    void showDialogFragment(DialogFragment dialogFragment);
}
