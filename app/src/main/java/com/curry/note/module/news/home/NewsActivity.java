package com.curry.note.module.news.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.curry.note.R;
import com.curry.note.module.news.movie.view.MovieFragment;
import com.curry.note.module.news.music.view.MusicFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by curry on 2017/5/15.
 */

public class NewsActivity extends AppCompatActivity {
    //BottomNavigationBar有时候也莫名其妙找不到，invalidate and restart
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private DouBanFragment bookFragment;
    private MusicFragment musicFragment;
    private MovieFragment movieFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //设置默认颜色
        bottomNavigationBar
                .setInActiveColor(R.color.colorInActive)//设置未选中的Item的颜色，包括图片和文字
                .setActiveColor(R.color.colorPrimary)        //设置的好像是整个bottom的背景？？？？？
                .setBarBackgroundColor(R.color.colorBarBg);//设置整个控件的背景色
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.book_normal, "书"))
                .addItem(new BottomNavigationItem(R.mipmap.music_normal, "音乐"))
                .addItem(new BottomNavigationItem(R.mipmap.movie_normal, "电影"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
//                Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
                FragmentManager fm = getSupportFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (bookFragment == null) {
                            bookFragment = DouBanFragment.getInstance("书");
                        }
                        transaction.replace(R.id.flNewsContent, bookFragment);
                        break;
                    case 1:
                        if (musicFragment == null) {
                            musicFragment = MusicFragment.getInstance("音乐");
                        }
                        transaction.replace(R.id.flNewsContent, musicFragment);
                        break;
                    case 2:
                        if (movieFragment == null) {
                            movieFragment = MovieFragment.getInstance("电影");
                        }
                        transaction.replace(R.id.flNewsContent, movieFragment);
                        break;
                    default:
                        break;
                }
                // 事务提交
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        setDefaultFragment();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        bookFragment = DouBanFragment.getInstance("书");
        transaction.replace(R.id.flNewsContent, bookFragment);
        transaction.commit();
    }
}
