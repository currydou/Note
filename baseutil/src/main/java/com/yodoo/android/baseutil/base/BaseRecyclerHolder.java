package com.yodoo.android.baseutil.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by lib on 2017/11/8.
 */

public abstract class BaseRecyclerHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected final String TAG = this.getClass().getSimpleName();
    private OnItemViewClickListener mClickListener;
    private OnItemViewLongClickListener mLongClickListener;
    protected OnCheckBoxClickListener mCheckBoxListner;

    public BaseRecyclerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    /**
     * 释放资源
     */
    protected void onRelease() {
        mClickListener = null;
        mLongClickListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemViewClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemViewLongClick(v, getLayoutPosition());
            return true;
        }
        return false;
    }

    public interface OnItemViewClickListener {
        void onItemViewClick(View view, int position);
    }

    public interface OnItemViewLongClickListener {
        void onItemViewLongClick(View view, int position);
    }

    public void setOnItemViewClickListener(OnItemViewClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnItemViewLongClickListener(OnItemViewLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public interface OnCheckBoxClickListener {
        void onItemCheckBoxClick(View view, int position);
    }

    public void setOnItemCheckBoxClickListener(OnCheckBoxClickListener listener) {
        this.mCheckBoxListner = listener;
    }

    public OnItemViewClickListener getClickListener() {
        return mClickListener;
    }

    public OnItemViewLongClickListener getLongClickListener() {
        return mLongClickListener;
    }

    public abstract void setData(T data, int position, int count);
}
