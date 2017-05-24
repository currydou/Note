package com.curry.note.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jerry.guan on 7/7/2016.
 * 这一种只有当oncreate()或者可见的时候但是没有加载过数据，才会自动加载
 * ？？？？？？？（但是存在条件二这种情况吗？感觉条件一和二是一样的）???????
 * 知道了。
 * 使用懒加载的原因是想让使用viewpager不会预加载fragment，就像没使用viewpager切换fragment一样
 * LazyFragment2虽然只加载当前的可见的fragment，但是每次可见的时候都会自动加载。
 * 这个LazyBaseFragment2则是优化了上面的问题，实现了，就像没使用viewpager切换fragment一样
 * ？？？？？？？但是initData方法和lazyload方法会不会是重复的？？？？？？？？？？？？
 *
 */
public abstract class LazyBaseFragment2 extends Fragment{

    private boolean isVisible=false;//可否可见
    protected boolean isPrepared=false;//是否加载过
    private boolean isCreatedView=false;//是否加载完成试图

    protected View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreatedView=true;
        initDatas();
        //如果当前的frgment可见且没有加载过数据则 正常加载数据
        if(isVisible&&!isPrepared){
            isPrepared=true;
            lazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        isCreatedView=false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean isCreatedView() {
        return isCreatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }else{
            isVisible=false;
            onInvisible();
        }
    }


    /**
     * 当fragment可见时调用
     * 如果当前frgment可见，并且view是被创建好的且没有加载过数据的时候会自动网络请求加载数据
     */
    protected void onVisible(){
        if(isCreatedView&&!isPrepared){
            isPrepared=true;
            lazyLoad();
        }
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    protected abstract void initDatas();

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void lazyLoad();

//    protected abstract View initViews();
}
