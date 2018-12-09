package com.yodoo.android.baseutil.utils;

import android.graphics.Rect;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.yodoo.android.baseutil.base.AppManager;
import com.yodoo.android.baseutil.base.BaseRecyclerHolder;

import org.simple.eventbus.EventBus;

/**
 * Created by lib on 2017/7/26.
 */

public class UIUtil {
    /**
     * 输入法弹出时将指定view顶上去
     *
     * @param root
     * @param scrollToView
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        int rootInvisibleHeight = root.getRootView()
                                .getHeight() - rect.bottom;
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 100) {
                            //软键盘弹出来的时候
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);
                            // 计算root滚动高度，使scrollToView在可见区域的底部
                            int srollHeight = (location[1] + scrollToView
                                    .getHeight()) - rect.bottom;
                            root.scrollTo(0, srollHeight);
                        } else {
                            // 软键盘没有弹出来的时候
                            root.scrollTo(0, 0);
                        }
                    }
                });
    }

    /**
     * 设置view是否可用，包括子view
     *
     * @param view   要设置的view
     * @param enable true可用,false不可用
     */
    public static void setViewEnable(View view, boolean enable) {
        if (view == null) return;
        view.setEnabled(enable);
//        view.setClickable(enable);
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                if (child instanceof ViewGroup) {
                    setViewEnable(child, enable);
                } else {
                    child.setEnabled(enable);
//                    child.setClickable(enable);
                }
            }
        }
    }

}
