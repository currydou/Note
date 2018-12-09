package com.yodoo.android.baseutil.base;

import android.view.View;

public interface IBaseRecyclerAdapter {

    void setOnItemClickListener(OnItemClickListener listener);

    void setOnItemLongClickListener(OnItemLongClickListener listener);

    interface OnItemClickListener<T> {
        void onItemClick(View view, int viewType, T data, int position);
    }

    interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, int viewType, T data, int position);
    }

    interface ExpandListener {
        /**
         * 展开收起时
         *
         * @param countToGroup 本组之前所有item的数量，不包含本组，此数量仅为显示在界面上的，收起的组内数据不算
         */
        void onExpand(boolean isOpen, int groupPosition, int countToGroup, int groupItemCount);
    }
}
