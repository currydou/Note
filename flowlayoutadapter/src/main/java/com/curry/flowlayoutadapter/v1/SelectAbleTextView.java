package com.curry.flowlayoutadapter.v1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by next on 2017/12/6.
 */

public class SelectAbleTextView extends AppCompatTextView {

    private boolean selected;

    public SelectAbleTextView(Context context) {
        super(context);
    }

    public SelectAbleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectAbleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean mSelected) {
        this.selected = mSelected;
    }

}
