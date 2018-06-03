package com.curry.flowlayoutadapter.v1;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.flowlayoutadapter.R;
import com.curry.flowlayoutadapter.XCFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo：
 * 初始标签
 * List<TagItem> invoiceTypeTextList = new ArrayList<>();
 * invoiceTypeTextList.add(new TagItem(10, "已报销")); //带数字只是为了得到选中的时候得到这个数字方便点
 * invoiceTypeTextList.add(new TagItem(20, "未报销"));
 * invoiceTypeFlowLayout.setTextList(invoiceTypeTextList);
 * //匹配文字删除
 * invoiceTypeFlowLayout.setTextSelectedList("已报销");
 *
 * 刷新需要重新 初始标签 用setTextList()方法
 * 内部逻辑：用map存储选中状态
 * 指定样式,有选中和未选中状态；
 * 可以设置是否单选；
 */
public class XCFlowLayoutItem extends XCFlowLayout {

    private List<TagItem> mTextList = new ArrayList<>();
    private Context mContext;
    private Map<String, TagItem> mSelectedMap = new HashMap<>();// TODO: 2017/12/6
    private List<SelectAbleTextView> textViewList = new ArrayList<>();
    private boolean isSingleMode;
    private SelectAbleTextView lastTextView;

    public XCFlowLayoutItem(Context context) {
        this(context, null);
    }

    public XCFlowLayoutItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCFlowLayoutItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    /**
     * 设置数据源 TagItem List
     *
     * @param list
     */
    public void setTextList(List<TagItem> list) {
        this.mTextList.clear();
        this.mTextList.addAll(list);
        for (TagItem tagItem : mTextList) {
            addView(tagItem);
        }
        invalidate();
    }

    /**
     * 设置选中Integer List
     *
     * @param numberList
     */
    public void setTextSelectedIntegerList(List<Integer> numberList) {
        List<String> stringList = new ArrayList<>();
        for (Integer number : numberList) {
            stringList.add(number + "");
        }
        setTextSelectedList(stringList);
    }

    /**
     * 设置选中String List
     *
     * @param numberList
     */
    public void setTextSelectedList(List<String> numberList) {
        for (String number : numberList) {
            setTextSelected(number);
        }
    }

    /**
     * 匹配文字选中
     *
     * @param number
     */
    public void setTextSelected(String number) {
        if (TextUtils.isEmpty(number)) {
            return;
        }
        for (SelectAbleTextView textView : textViewList) {
            if (TextUtils.equals(textView.getTag() + "", number)) {
                setSelected(textView);
                TagItem tagItem = new TagItem(number, textView.getText().toString());
                mSelectedMap.put(tagItem.getNumber(), tagItem);
                textView.setSelected(!textView.isSelected());
                if (isSingleMode) {
                    lastTextView = textView;
                }
            }
        }
    }

    /**
     * 清除选中状态
     */
    public void reset() {
        for (SelectAbleTextView textView : textViewList) {
            textView.setSelected(false);
            setUnSelected(textView);
        }
        mSelectedMap.clear();
    }

    /**
     * 设置选中模式
     *
     * @param singleMode
     */
    public void setSingleMode(boolean singleMode) {
        isSingleMode = singleMode;
    }

    // TODO: 2017/12/6  网格布局
    public Map<String, TagItem> getmSelectedMap() {
        return mSelectedMap;
    }

    public void addView(TagItem tagItem) {
// TODO: 2017/12/6  警告的合适的处理方式
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        SelectAbleTextView view = new SelectAbleTextView(mContext);
        // TODO: 2017/12/6  抽取属性
        view.setPadding(dip2px(mContext, 15), dip2px(mContext, 5), dip2px(mContext, 15), dip2px(mContext, 5));
        view.setText(tagItem.getText());
        view.setTag(tagItem.getNumber());
        setUnSelected(view);
        view.setGravity(Gravity.CENTER);
//        view.setTextSize(Utils.sp2px(mContext,13));
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SelectAbleTextView selectAbleTextView = (SelectAbleTextView) v;
                String number = (String) selectAbleTextView.getTag();
                if (isSingleMode) {
                    if (selectAbleTextView != lastTextView) {
                        setSelected(selectAbleTextView);
                        setUnSelected(lastTextView);
                        lastTextView = selectAbleTextView;
                        mSelectedMap.clear();
                        setDataSelected(number);
                    }
                } else {
                    if (selectAbleTextView.isSelected()) {//设置为未选中状态
                        setUnSelected(selectAbleTextView);
                        mSelectedMap.remove(number);
                    } else {
                        setSelected(selectAbleTextView);
                        setDataSelected(number);
                    }
                    selectAbleTextView.setSelected(!selectAbleTextView.isSelected());
                    if (mOnTagClickListener != null) {
                        mOnTagClickListener.onTagClick(getXCFLayout(), v);
                    }
                }

            }
        });
        addView(view, lp);
        textViewList.add(view);
    }

    private XCFlowLayoutItem getXCFLayout() {
        return this;
    }

    private void setDataSelected(String number) {
        for (TagItem tagItem : mTextList) {
            if (TextUtils.equals(tagItem.getNumber() + "", number + "")) {
                mSelectedMap.put(number, tagItem);
            }
        }
    }

    private void setSelected(TextView textView) {
        if (textView != null) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.tag_pressed);
        }
    }

    private void setUnSelected(TextView textView) {
        if (textView != null) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.textBlackTag));
            textView.setBackgroundResource(R.drawable.tag_normal);
        }
    }

    public interface OnTagClickListener {
        void onTagClick(XCFlowLayoutItem xcFlowLayout2, View view);
    }

    private OnTagClickListener mOnTagClickListener;

    public void setOnTagClickListener(OnTagClickListener listener) {
        this.mOnTagClickListener = listener;
    }


    public void addView2(String text) {
//        feeTags.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for (int i = 0; i < mTextList.size(); i++) {
//            if (i > 2) break;
            final TextView view = new TextView(mContext);
            view.setPadding(20, 10, 20, 10);
            view.setText(mTextList.get(i).getText());
            view.setTextColor(Color.WHITE);
            view.setTag(i);
            view.setBackgroundResource(R.drawable.approve_search_pressed);
//            if (checkedTag == i) {
            view.setBackgroundResource(R.drawable.approve_search_pressed);
            view.setTextColor(Color.WHITE);
//            } else {
//                view.setBackgroundResource(R.drawable.approve_search_normal);
//                view.setTextColor(getResources().getColor(R.color.textBlack));
//        }
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = (int) v.getTag();
//                    if (position != checkedTag) {
//                        checkedTag = position;
//                    } else {
//                        checkedTag = -1;
//                    }
//                    showFeeTag();
//                }
//            });
            addView(view, lp);
        }
    }

}