package com.curry.note.module.news.book.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.curry.note.module.news.book.view.BookFragment;
import com.curry.note.module.news.movie.view.MovieFragment;
import com.curry.note.module.news.music.view.MusicFragment;

/**
 * Created by curry on 2016/12/8.
 */

public class BookAdapter extends FragmentPagerAdapter {
    private String[] titleList = {"文学", "音乐", "电影"}; //tab名的列表

    public BookAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
            case 0:
                fragment = BookFragment.getInstance("文学");
                break;
            case 1:
                fragment = MusicFragment.getInstance("音乐");
                break;
            case 2:
                fragment = MovieFragment.getInstance("电影");
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position % titleList.length];
    }
}

