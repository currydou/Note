package com.curry.note.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.bean.Note;
import com.curry.note.daomanager.NoteDaoUtil;
import com.curry.note.module.news.home.NewsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 这个activity的功能列表
 * 1.进来的时候要更新便签列表
 */
public class NoteListActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.rvNoteList)
    RecyclerView rvNoteList;
    //昨天好的，今天到公司打开项目后，找不到NavigationView，过了一会又自己好了。。。
    @BindView(R.id.navigationView)
    NavigationView navigationview;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private NoteDaoUtil noteDaoUtil;
    private List<Note> noteList = new ArrayList<>();
    private NoteListAdapter noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// TODO: 5/16/2017  现在是最近的在下面，要把顺序倒过来
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        noteDaoUtil = new NoteDaoUtil(this);
        noteList = noteDaoUtil.queryAll();
        noteListAdapter = new NoteListAdapter(this, noteList);
        rvNoteList.setAdapter(noteListAdapter);
        rvNoteList.setLayoutManager(new LinearLayoutManager(this));
        tvAdd.setVisibility(View.VISIBLE);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteListActivity.this, NoteEditActivity.class));
            }
        });
        noteListAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position, Note note) {
                Intent intent = new Intent();
                intent.putExtra("note_id", note.getId());
                intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_1:
                        startActivity(new Intent(NoteListActivity.this, NewsActivity.class));
                        break;
                    case R.id.nav_2:

                        break;
                    case R.id.nav_3:

                        break;
                }
                return true;
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

}
