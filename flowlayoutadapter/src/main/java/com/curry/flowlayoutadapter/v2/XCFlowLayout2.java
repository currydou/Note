package com.curry.flowlayoutadapter.v2;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.curry.flowlayoutadapter.XCFlowLayout;

/**
 * Demo：
 * 初始标签
 * List<TagItem> invoiceTypeTextList = new ArrayList<>();
 * invoiceTypeTextList.add(new TagItem(10, "已报销")); //带数字只是为了得到选中的时候得到这个数字方便点
 * invoiceTypeTextList.add(new TagItem(20, "未报销"));
 * invoiceTypeFlowLayout.setTextList(invoiceTypeTextList);
 * //匹配文字删除
 * invoiceTypeFlowLayout.setTextSelectedList("已报销");
 * <p>
 * 刷新需要重新 初始标签 用setTextList()方法
 * 内部逻辑：用map存储选中状态
 * 指定样式,有选中和未选中状态；
 * 可以设置是否单选；
 */
public class XCFlowLayout2 extends XCFlowLayout {

    private Context mContext;
    private BaseAdapter mAdapter;

    public XCFlowLayout2(Context context) {
        this(context, null);
    }

    public XCFlowLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCFlowLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    public void setAdapter(BaseAdapter adapter) {
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
    }

}