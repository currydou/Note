/*
 * Copyright (C) 2016 Bilibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.curry.note.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.curry.note.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author xyczero
 * @time 16/5/29
 */
public class CardPickerDialog extends DialogFragment {
    public static final String TAG = "CardPickerDialog";
    public static final int THEME_DEFAULT = 0x0;
    public static final int THEME_RED = 0x1;
    public static final int THEME_BLUE = 0x2;

    @BindView(R.id.llRedTheme)
    LinearLayout llRedTheme;
    @BindView(R.id.llBlueTheme)
    LinearLayout llBlueTheme;
    @BindView(R.id.llDefaultTheme)
    LinearLayout llDefaultTheme;

    Unbinder unbinder;
    private ClickListener mClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_theme_picker, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.llRedTheme, R.id.llBlueTheme, R.id.llDefaultTheme})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llRedTheme:
                if (mClickListener != null) {
                    mClickListener.onConfirm(THEME_RED);
                }
                break;
            case R.id.llBlueTheme:
                if (mClickListener != null)
                    mClickListener.onConfirm(THEME_BLUE);
                break;
            case R.id.llDefaultTheme:
                if (mClickListener != null)
                    mClickListener.onConfirm(THEME_DEFAULT);
                break;
        }
    }

    public interface ClickListener {
        void onConfirm(int currentTheme);
    }
}
