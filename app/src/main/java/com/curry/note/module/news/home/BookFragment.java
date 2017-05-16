package com.curry.note.module.news.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.base.LazyFragment2;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookFragment extends LazyFragment2 {

    public static BookFragment getInstance(String name) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bookFragment.setArguments(bundle);
        return bookFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(name);
        return view;
    }

    @Override
    public void getData() {

    }


}
