package com.curry.note.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.curry.note.R;


/**
 */
public class MenuPopupWindow extends PopupWindow {
    private ClickResultListener listener;
    private View view;

    public MenuPopupWindow(Context context, ClickResultListener listener) {
        this.listener = listener;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup_menu, null);
        setContentView(view);
        setProperty();
        initView();
    }

    private void setProperty() {
        // 设置弹窗体宽度，高度
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
//        this.setAnimationStyle(R.style.MyAnimationStyle);
    }


    private void initView() {
        TextView tvSyncToLocal = (TextView) view.findViewById(R.id.tvSyncToLocal);
        TextView tvUpload = (TextView) view.findViewById(R.id.tvUpload);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1500);
//        vCancel.setAnimation(animation);
        tvSyncToLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickResult(true);
                    dismiss();
                }
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ClickResult(false);
                    dismiss();
                }
            }
        });

    }


    public interface ClickResultListener {
        void ClickResult(boolean tag);
    }
}
