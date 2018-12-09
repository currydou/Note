package com.yodoo.android.baseutil.base;

import android.content.Context;

/**
 * Created by libin-com on 2017/12/13.
 */

public interface ProgressDialog {
    void showProgressDialog(Context context, int msg);

    void showProgressDialog(Context context, String msg);

    void showProgressDialog(Context context);

    void dismissProgressDialog();

    void onDestroy();

    void onPause();
}
