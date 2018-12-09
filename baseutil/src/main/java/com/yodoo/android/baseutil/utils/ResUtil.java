package com.yodoo.android.baseutil.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by lib on 2017/7/12.
 * 资源ID工具
 */

public class ResUtil {
    public static int getMipMapId(Context context, String name) {
        return context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
    }

    public static Drawable getDrawable(Context context, int id) {
        Drawable imgOff = context.getResources().getDrawable(id);
        imgOff.setBounds(0, 0, imgOff.getMinimumWidth(),
                imgOff.getMinimumHeight());
        return imgOff;
    }
}
