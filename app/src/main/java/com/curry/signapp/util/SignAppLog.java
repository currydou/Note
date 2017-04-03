package com.curry.signapp.util;

import android.util.Log;


/**
 * @author wangkai
 * @Description: LogCat工具类, 传入的数据类型任意。
 * create at 2015/10/30 11:27
 */
public class SignAppLog {
    private static final String TAG = "SignApp";

    public static void e(Object object) {
        if (object == null) {
            return;
        }
        Log.e(TAG, object + "");
    }

    /*
    /**
    *可以直接打印json
    *
    public static void json(String json) {
        if (!TextUtils.isEmpty(json)) {
            JLog.json(TAG, json);
        }
    }
*/
    /**
     * 打印空日志&&超过4k的分开打印
     * @param text
     */
    public static void print4k(String text) {
        text = ">>" + text.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < text.length()) {
            // java的字符不允许指定超过总的长度end
            if (text.length() <= index + maxLength) {
                sub = text.substring(index);
            } else {
                sub = text.substring(index, index + maxLength);
            }

            index += maxLength;
            Log.e("SignApp", "-->" + sub.trim());
        }
    }

}
