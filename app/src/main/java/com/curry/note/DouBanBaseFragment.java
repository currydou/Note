package com.curry.note;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.curry.note.base.LazyBaseFragment2;

/**
 * Created by curry.zhang on 5/25/2017.
 */

// TODO: 5/25/2017  看之前的项目基类的涉及到布局的抽取方式。基类怎么处理布局

public abstract class DouBanBaseFragment extends LazyBaseFragment2 {

    protected void setFailPageVisibility(TextView tvLoading, RecyclerView recyclerView, TextView tvLoadError) {
        tvLoading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.VISIBLE);
    }

    protected void setSuccessPageVisibility(TextView tvLoading, RecyclerView recyclerView, TextView tvLoadError) {
        tvLoading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        tvLoadError.setVisibility(View.GONE);
    }

    protected void setLoadingPageVisibility(TextView tvLoading,RecyclerView recyclerView, TextView tvLoadError) {
        tvLoading.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.GONE);
    }

    protected void setPageVisible(TextView tvLoading, SwipeRefreshLayout swipeRefreshLayout, TextView tvLoadError) {

    }

}
