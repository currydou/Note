package com.curry.note.module.news.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.base.LazyFragment2;
import com.curry.note.bean.BookBean;
import com.curry.note.module.news.book.presenter.BookInfo;
import com.curry.note.module.news.book.presenter.BookInfoImpl;
import com.curry.note.module.news.book.view.IBookView;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookFragment extends LazyFragment2 implements IBookView {

    private BookInfo bookInfo;

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
        bookInfo = new BookInfoImpl(this);
        bookInfo.getList("文学", 0, 10);
        Log.e("curry", "getData: ");
        return view;
    }

    @Override
    public void getData() {// TODO: 2017/5/16  这个方法有问题，没有执行到

    }


    @Override
    public void showSuccessPage(BookBean bookBean) {//考虑要不要换个api
        Log.e("curry", bookBean.toString());s
    }

    @Override
    public void showFailPage() {

    }
}
