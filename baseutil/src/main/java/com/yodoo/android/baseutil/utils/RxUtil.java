package com.yodoo.android.baseutil.utils;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.yodoo.android.baseutil.mvp.IView;

/**
 * Created by lib on 2017/11/8.
 */

public class RxUtil {
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof LifecycleProvider){
            return ((LifecycleProvider)view).bindToLifecycle();
        }else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }
}
