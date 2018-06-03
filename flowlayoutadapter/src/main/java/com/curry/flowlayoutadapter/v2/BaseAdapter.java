package com.curry.flowlayoutadapter.v2;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DELL on 2017/9/9.
 * Description :
 */

public abstract class BaseAdapter {

    //获取view的数量
    public abstract int getCount();

    //获取View
    public abstract View getView(int position, ViewGroup parent);

}