package com.curry.flowlayoutadapter.v2;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.View;

import com.curry.flowlayoutadapter.R;
import com.curry.flowlayoutadapter.XCFlowLayout;

import java.util.List;

/**
 * V2使用adapter。
 * 暂时不加入缓存view(删减单个View，先removeallview，删除这条数据，然后重新setadapter)
 */
public class XCFlowLayout2 extends XCFlowLayout {

    private Context mContext;
    private int mSelectedMax;
    private TagAdapter mTagAdapter;
    /*-------------------------------------------*/
    //    private List<Integer> mSelectedList = new ArrayList<>();
    private LongSparseArray<Integer> mSelectedArray = new LongSparseArray<>();


    public XCFlowLayout2(Context context) {
        this(context, null);
    }

    public XCFlowLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCFlowLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XCFlowLayoutItem);
        mSelectedMax = ta.getInt(R.styleable.XCFlowLayoutItem_max_select, -1);
        ta.recycle();
    }

    /*-----------------------------------接口----------------------------------------------*/
    private OnTagClickListener mOnTagClickListener;
    private OnSelectListener mOnSelectListener;

    public interface OnSelectListener {
        void onSelected(long[] selectedIds);
    }

    public interface OnTagClickListener {
        boolean onTagClick(int position, View view, XCFlowLayout2 parent);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }
    /*---------------------------------------------------------------------------------*/

    public void setAdapter(TagAdapter adapter) {
        mTagAdapter = adapter;
//        mTagAdapter.setOnDataChangedListener(this);
//        mSelectedView.clear();
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = mTagAdapter;// TODO: 2018/6/3   这种写法
        List allList = mTagAdapter.getAllList();
        for (int i = 0; i < allList.size(); i++) {   // TODO: 2018/6/3   内部使用数量，用getacount方法，还是直接用list的size？看listview
            final View tagView = adapter.getView(i, null, this);
            addView(tagView);

           /* //更新数据，设置选中
            if (preCheckedList.contains(i)) {
                setChildChecked(i, tagViewContainer);
            }
*/
//            tagView.setClickable(false); //设置不可点击
            final int position = i;
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSelect(position);
                    if (mOnTagClickListener != null) {
                        mOnTagClickListener.onTagClick(position, tagView, XCFlowLayout2.this);
                    }
                }
            });
        }
//        mSelectedView.addAll(preCheckedList);

    }

    private void doSelect(int position) {
        //只操作adapter里的选中的数据
        LongSparseArray selectedArray = mSelectedArray;// TODO: 2018/6/3  警告处理
        boolean existed = false;
        for (int i = 0; i < selectedArray.size(); i++) {
            if (selectedArray.keyAt(i) == position) {
                existed = true;
            }
        }
        if (existed) { //如果存在，移除掉
            selectedArray.remove(position);
        } else { //不存在，添加( 最大选中一个时，把之前所有的移除掉；不是一个时，选中的size大于等于max时，return)
            if (mSelectedMax == 1 && selectedArray.size() == 1) {
                selectedArray.clear();  // TODO: 2018/6/3  测试用这个selectedArray对不对，还是应该用mSelectedArray
            } else {
                if (mSelectedMax > 0 && selectedArray.size() >= mSelectedMax) {
                    return;
                }
            }
            selectedArray.put(position, position);
        }
        //调用getview刷新view
        for (int i = 0; i < getChildCount(); i++) { //getChildCount 应该是等于 aliList.size的
            View view = getChildAt(i);
            mTagAdapter.getView(i, view, this);
        }
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(getSelectedIds());
        }
    }

    public void remove(int position) {
        /*
        //删除单个的，原来的缓存view的监听参数不是最新的
        mTagAdapter.getAllList().remove(position);
        removeViewAt(position);*/
        mTagAdapter.getAllList().remove(position);
        removeAllViews();

        if (mSelectedArray != null && mSelectedArray.size() != 0) {
            mSelectedArray.clear();
        }
        changeAdapter();
    }

    // TODO: 2018/6/3   这个这次感觉可以不要，用的时候，直接在外面setadapter，其实remove也可以
    public void add() {

    }


    public long[] getSelectedIds() {
        final LongSparseArray<Integer> idStates = mSelectedArray; // TODO: 2018/6/3  直接给，什么情况复制返回
        final int count = idStates.size();
        final long[] ids = new long[count];

        for (int i = 0; i < count; i++) {
            ids[i] = idStates.keyAt(i);
        }

        return ids;
    }

    public int getmSelectedMax() {
        return mSelectedMax;
    }

    public void setmSelectedMax(int mSelectedMax) {
        this.mSelectedMax = mSelectedMax;
    }




    /*  public void setAdapter(TagAdapter adapter) {
        //当Adapter为空的时候，抛出异常
        if (adapter == null) {
            throw new NullPointerException("adapter not null");
        }

        mAdapter = adapter;

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, this);
            addView(view);
        }
    }*/


}