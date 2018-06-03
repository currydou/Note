package com.curry.flowlayoutadapter.v2;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 */

public abstract class TagAdapter<T> {
    //内部应该需要维持一个数据的list
    protected List<T> mAllList = new ArrayList<>();


    public TagAdapter(List<T> list) {
        if (list != null) {
            mAllList.addAll(list);
        }
    }

    public List<T> getAllList() {
        return mAllList;//
    }


    //获取view的数量
    public abstract int getCount();

    /**
     * return position
     */
//    public abstract int getItemId();

    //获取View
    public abstract View getView(int position, View view, ViewGroup parent);

    public T getItem(int position) {
        return mAllList.get(position);
    }
}