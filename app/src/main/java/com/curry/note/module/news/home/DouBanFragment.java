package com.curry.note.module.news.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curry.note.R;
import com.curry.note.module.news.book.adapter.BookAdapter;
import com.curry.note.module.news.book.presenter.BookInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public class DouBanFragment extends Fragment {

    @BindView(R.id.tabLayoutBook)
    TabLayout tabLayoutBook;
    @BindView(R.id.vpBook)
    ViewPager vpBook;
    Unbinder unbinder;
    private BookInfo bookInfo;
    private BookAdapter bookAdapter;

    public static DouBanFragment getInstance(String name) {
        DouBanFragment douBanFragment = new DouBanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        douBanFragment.setArguments(bundle);
        return douBanFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookAdapter = new BookAdapter(getChildFragmentManager());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置TabLayout的模式
        tabLayoutBook.setTabMode(TabLayout.MODE_FIXED);

        //viewpager加载adapter
        vpBook.setAdapter(bookAdapter);
        vpBook.setOffscreenPageLimit(3);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tabLayoutBook.setupWithViewPager(vpBook);
        //tab_FindFragment_title.set
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
