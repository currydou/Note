package com.yodoo.android.baseutil.base;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.yodoo.android.baseutil.R;

/**
 * Created by libin-com on 2017/12/13.
 */

public class DefaultProgressDialog implements ProgressDialog {
    android.app.ProgressDialog mDialog;

    @Override
    public void showProgressDialog(Context context, int msg) {
        String message = context.getString(msg);
        showProgressDialog(context, message);
    }

    @Override
    public void showProgressDialog(Context context, String msg) {
        if (mDialog == null) {
            mDialog = new android.app.ProgressDialog(context);
            mDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage(msg);
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    return false;
                }
            });
//            Timber.d("create progress dialog " + mDialog.toString());
        }
        mDialog.show();
//        Timber.d("show progress dialog " + mDialog.toString());
    }

    @Override
    public void showProgressDialog(Context context) {
        if (mDialog == null) {
            mDialog = new android.app.ProgressDialog(context);
            mDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage(context.getString(R.string.bLoading));
            mDialog.setCancelable(false);
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    return false;
                }
            });
//            Timber.d("create progress dialog " + mDialog.toString());
        }
        mDialog.show();
//        Timber.d("show progress dialog " + mDialog.toString());
    }

    @Override
    public void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
//            Timber.d("dismiss progress dialog " + mDialog.toString());
        }
    }

    @Override
    public void onDestroy() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
//                Timber.d("show progress dialog " + mDialog.toString());
            }
//            Timber.d("destroy progress dialog " + mDialog.toString());
            mDialog = null;
        }
    }

    @Override
    public void onPause() {
        dismissProgressDialog();
    }
}
