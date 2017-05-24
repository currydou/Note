package com.curry.note.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 这一种每次可见的时候都会加载
 */
public abstract class LazyFragment2 extends Fragment {

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    //标志当前页面是否可见
    private boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {//这里判断一次可见，后面还有一次，可以优化
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();//这里有可能没准备好，如果做一些动画结束操作是不是有些缺陷？暂时这样，没见用到这个方法。
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        getData();//数据请求
    }

    protected void onInvisible() {
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void getData();//是不是要每次可见的时候都加载数据，还是加载过了，这里就不再自动加载。
    //如果需要加个isPrepared来判断，具体看莱聚+里的
}
