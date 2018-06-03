package com.curry.flowlayoutadapter.v2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.flowlayoutadapter.R;

import java.util.List;

/**
 * Created by curry on 2018/6/3.
 */

public class TestTagAdapter extends TagAdapter<String> {
    private Context mContext;

    public TestTagAdapter(Context context, List<String> list) {
        super(list);
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return mAllList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XCFlowLayout2 xcFlowLayout2 = (XCFlowLayout2) parent;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_textview, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.item_textview);
        textView.setText(mAllList.get(position));
        if (isSelected(position, xcFlowLayout2)) {
            textView.setBackgroundColor(Color.RED);
        } else {
            textView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    private boolean isSelected(int position, XCFlowLayout2 xcFlowLayout2) {
        for (int i = 0; i < xcFlowLayout2.getSelectedIds().length; i++) {
            if ((int) xcFlowLayout2.getSelectedIds()[i] == position) {
                return true;
            }
        }
        return false;
    }
}
