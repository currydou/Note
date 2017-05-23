package com.curry.note.module.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.Note;
import com.curry.note.constant.SharedTag;
import com.curry.note.daomanager.NoteDaoUtil;
import com.curry.note.module.news.home.NewsActivity;
import com.curry.note.widget.dialog.CardPickerDialog;
import com.rrtoyewx.andskinlibrary.manager.SkinLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 这个activity的功能列表
 * 1.进来的时候要更新便签列表
 */
public class NoteListActivity extends BaseActivity {

    @BindView(R.id.tvSyncToLocal)
    TextView tvSyncToLocal;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.rvNoteList)
    RecyclerView rvNoteList;
    //昨天好的，今天到公司打开项目后，找不到NavigationView，过了一会又自己好了。。。
    @BindView(R.id.navigationView)
    NavigationView navigationview;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    private NoteDaoUtil noteDaoUtil;
    private List<Note> noteList = new ArrayList<>();
    private NoteListAdapter noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        noteDaoUtil = new NoteDaoUtil(this);
        initToolbar();
        initNavigationView();
        initFloatingActionButton();
        initRecyclerView();

    }

    private void initToolbar() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //同样的图片，同样的分辨率下，mipmap里的和drawable里的显示的大小不一样
            //写在布局文件里了
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //这个工具类的颜色要把颜色写在这里才行
        //换肤的库有改变状态栏的颜色的功能，同时 存在会有冲突，所以这里注释掉
//        BarUtils.setColor(this, 0xff80b9cf);//0xFFFF0000

        tvSyncToLocal.setVisibility(View.VISIBLE);
        tvSyncToLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncToLocal();
            }
        });
    }

    private void syncToLocal() {
        BmobQuery<Note> noteBmobQuery = new BmobQuery<>();
        noteBmobQuery.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                if (e == null) {
                    noteList = list;
                    noteListAdapter.setData(list);
                    //保存本地数据库
                    for (Note note: noteList){
                        noteDaoUtil.addOneNote(note);
                    }
                }
            }
        });
    }

    private void initNavigationView() {
        ColorStateList colorStateList = getResources().getColorStateList(R.color.item_menu_selector);
        navigationview.setItemTextColor(colorStateList);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_1:
                        startActivity(new Intent(NoteListActivity.this, NewsActivity.class));
                        break;
                    case R.id.nav_2:
                        final CardPickerDialog cardPickerDialog = new CardPickerDialog();
                        cardPickerDialog.setClickListener(new CardPickerDialog.ClickListener() {
                            @Override
                            public void onConfirm(int currentTheme) {
                                switch (currentTheme) {
                                    case CardPickerDialog.THEME_DEFAULT:
                                        SkinLoader.getDefault().restoreDefaultSkin();
                                        break;
                                    case CardPickerDialog.THEME_RED:
                                        SkinLoader.getDefault().loadSkin("red");
                                        break;
                                    case CardPickerDialog.THEME_BLUE:
                                        SkinLoader.getDefault().loadSkin("blue");
                                        break;
                                }
                                cardPickerDialog.dismiss();
                            }
                        });
                        cardPickerDialog.show(getSupportFragmentManager(), "theme");
                        break;
                    case R.id.nav_3:

                        break;
                }
                return true;
            }
        });
    }

    private void initFloatingActionButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
                intent.putExtra(SharedTag.TYPE, SharedTag.TYPE_ADD_NOTE);
                startActivity(intent);
            }
        });

    }

    private void initRecyclerView() {
        noteListAdapter = new NoteListAdapter(this, noteList);
        rvNoteList.setAdapter(noteListAdapter);
        rvNoteList.setLayoutManager(new LinearLayoutManager(this));

        noteListAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Note note) {
                Intent intent = new Intent();
                intent.putExtra(SharedTag.NOTE_ID, note.getId());
                intent.putExtra(SharedTag.TYPE, SharedTag.TYPE_EDIT_NOTE);
                intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //更新便签
        noteList = noteDaoUtil.queryAll();
        noteListAdapter.setData(noteList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home: //Menu icon
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
