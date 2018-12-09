package com.yodoo.android.baseutil.base;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BasePagingAdapter<T> extends PagedListAdapter<T, BaseRecyclerHolder<T>> implements IBaseRecyclerAdapter {
    protected List<T> dataList;
    protected OnItemClickListener mClickListener;
    protected OnItemLongClickListener mLongClickListener;
    protected BaseRecyclerHolder<T> mHolder;

    protected BasePagingAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    protected BasePagingAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    @Override
    public BaseRecyclerHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false);
        mHolder = getHolder(view, viewType);
        if (mHolder == null) throw new IllegalStateException("view can not be null");
        mHolder.setOnItemViewClickListener(new BaseRecyclerHolder.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(view, viewType, getItemAt(position), position);
                }
            }
        });
        mHolder.setOnItemViewLongClickListener(new BaseRecyclerHolder.OnItemViewLongClickListener() {
            @Override
            public void onItemViewLongClick(View view, int position) {
                if (mLongClickListener != null) {
                    mLongClickListener.onItemLongClick(view, viewType, getItemAt(position), position);
                }
            }
        });
        return mHolder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder<T> holder, int position) {
        holder.setData(getItemAt(position), position, getItemCount());
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder<T> holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            onBindViewHolder(holder, position);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void clearAll() {
        if (dataList != null) {
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 获得item的数据
     *
     * @param position
     * @return
     */
    public T getItemAt(int position) {
        return dataList == null ? null : dataList.size() > position ? dataList.get(position) : null;
    }

    /**
     * 子类实现提供holder
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseRecyclerHolder<T> getHolder(View v, int viewType);

    /**
     * 提供Item的布局
     *
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutId(int viewType);

    /**
     * 遍历所有hodler,释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseRecyclerHolder) {
                ((BaseRecyclerHolder) viewHolder).onRelease();
            }
        }
    }
}
