package com.curry.note.module.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.Note;
import com.curry.note.bean.bmob.User;
import com.curry.note.constant.Constants;
import com.curry.note.daomanager.NoteDaoUtil;
import com.curry.note.util.LogUtil;
import com.curry.note.util.ToastUtils;
import com.curry.note.widget.dialog.CardPickerDialog;
import com.curry.note.widget.popupwindow.MenuPopupWindow;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rrtoyewx.andskinlibrary.manager.SkinLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * 这个activity的功能列表
 * 1.进来的时候要更新便签列表
 * <p>
 * activity context 内存泄漏
 * http://blog.csdn.net/matrix_xu/article/details/8424554
 * http://blog.csdn.net/qq_32618417/article/details/51703414
 */
public class NoteListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tvMenu)
    TextView tvMenu;
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
    @BindView(R.id.tvUnloadFail)
    TextView tvUnloadFail;

    //    @BindView(R.id.sdvUserHeadPortrait)
    SimpleDraweeView sdvUserHeadPortrait;
    //    @BindView(R.id.tvUserName)
    TextView tvUserName;
    //    @BindView(R.id.llUser)
    LinearLayout llUser;

    private NoteDaoUtil noteDaoUtil;
    private List<Note> noteList = new ArrayList<>();
    private List<BmobObject> noteListFail = new ArrayList<>();
    private NoteListAdapter noteListAdapter;
    private String userName;
    private String headPortraitUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);


        resolveIntent();
        initToolbar();
        initNavigationView();
        initFloatingActionButton();
        initRecyclerView();
        temp();
    }


    // TODO: 6/9/2017  没网的时候进不去！！！
    private void resolveIntent() {
        Intent intent = getIntent();
        userName = intent.getStringExtra(Constants.USER_NAME2);
        headPortraitUrl = intent.getStringExtra(Constants.HEAD_PORTRAIT_URL);
    }

    private void temp() {
    }


    private void initToolbar() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            // TODO: 5/24/2017  同样的图片，同样的分辨率下，mipmap里的和drawable里的显示的大小不一样
            //写在布局文件里了
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //这个工具类的颜色要把颜色写在这里才行
        //换肤的库有改变状态栏的颜色的功能，同时 存在会有冲突，所以这里注释掉
//        BarUtils.setColor(this, 0xff80b9cf);//0xFFFF0000

        tvMenu.setVisibility(View.VISIBLE);
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MenuPopupWindow(NoteListActivity.this, new MenuPopupWindow.ClickResultListener() {
                    @Override
                    public void ClickResult(boolean tag) {
                        if (tag) {
                            //同步到本地
                            syncToLocal();
                        } else {
                            //上传
                            unloadToServer();
                        }
                    }
                }).showAsDropDown(tvMenu, 0, 10);
            }
        });

        tvUnloadFail.setText("未同步服务器" + noteListFail.size() + "条");
    }

    private void syncToLocal() {// TODO: 2017/5/29  更新UI抽取，初始化调一次，请求道数据调一次
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<Note> noteBmobQuery = new BmobQuery<>();
        noteBmobQuery.addWhereEqualTo("userId", currentUser.getObjectId());
        noteBmobQuery.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                if (e == null) {
                    noteList = list;
                    //先删除掉本地的数据，再保存本地数据库
                    List<Note> notes = noteDaoUtil.queryAll();
                    for (Note note : notes) {
                        noteDaoUtil.deleteUser(note);
                    }
                    // TODO: 2017/5/29  每次都查数据库？
                    for (Note note : noteList) {
                        noteDaoUtil.addOneNote(note);
                    }
                    updateUI();
                    ToastUtils.showLongToast("同步成功");
                } else {
                    ToastUtils.showLongToast("同步失败" + e.getMessage());
                    LogUtil.d("同步失败" + e.getMessage());
                }
            }
        });

    }

    private void unloadToServer() {
        //
        noteListFail.clear();
        for (Note note : noteList) {
            Boolean isSaveServer = note.getIsSaveServer();
            if (!isSaveServer) {
                noteListFail.add(note);
            }
        }
        if (noteListFail.size() == 0) {
            ToastUtils.showLongToast("没有未上传的便签 ~-~");
            return;
        }
        //批量上传
        //第二种方式：v3.5.0开始提供
        new BmobBatch().insertBatch(noteListFail).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    ToastUtils.showLongToast("批量成功");
                    //更新本地数据库
                    for (BmobObject bmobObject : noteListFail) {
                        Note note = (Note) bmobObject;
                        note.setIsSaveServer(true);
                        noteDaoUtil.updateUser(note);
                    }
                    //更新列表的状态
                    updateUI();
                    //noListFail清空
                    noteListFail.clear();
                } else {
                    ToastUtils.showLongToast("批量失败");
                }
            }
        });
    }


    private void initNavigationView() {
        // TODO: 2017/8/15  换了账号显示之前的数据？？？

        ColorStateList colorStateList = getResources().getColorStateList(R.color.item_menu_selector);
        navigationview.setItemTextColor(colorStateList);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_1:
//                        startActivity(new Intent(NoteListActivity.this, NewsActivity.class));
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
                    case R.id.nav_exit:
                        onBackPressed();
                        break;
                    case R.id.nav_4:
                        changeTextSize(NoteListActivity.this, 0.3f);
                        finish();
                        startActivity(new Intent(NoteListActivity.this, NoteListActivity.class));
                        break;
                }
                return true;
            }
        });
        //init navigationview header
        initNavigationHeader();
    }


    public void changeTextSize(Activity activity, float multiple) {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = multiple;    //1为标准字体，multiple为放大的倍数
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMetrics.scaledDensity = configuration.fontScale * displayMetrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, displayMetrics);
    }

    private void initNavigationHeader() {
        llUser = (LinearLayout) navigationview.getHeaderView(0);
        sdvUserHeadPortrait = (SimpleDraweeView) llUser.findViewById(R.id.sdvUserHeadPortrait);// TODO: 5/25/2017  这里怎么用butterknife
        tvUserName = (TextView) llUser.findViewById(R.id.tvUserName);
        //显示头像，和名字
        if (TextUtils.isEmpty(userName)) {
            //显示已有账户中的
            User currentUser = BmobUser.getCurrentUser(User.class);
            sdvUserHeadPortrait.setImageURI(currentUser.getHeadPortraitUrl());
            tvUserName.setText(currentUser.getUsername());
        } else {
            //显示传来的
            sdvUserHeadPortrait.setImageURI(headPortraitUrl);
            tvUserName.setText(userName);
        }

        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //不是空，已经登录，跳到用户信息界面
                intent.setClass(NoteListActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initFloatingActionButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
                intent.putExtra(Constants.TYPE, Constants.TYPE_ADD_NOTE);
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
                intent.putExtra(Constants.NOTE_ID, note.getId());
                intent.putExtra(Constants.TYPE, Constants.TYPE_EDIT_NOTE);
                intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteDaoUtil = new NoteDaoUtil(this);
        updateUI();
    }

    /**
     * 查询数据库，显示便签
     */
    private void updateUI() {
        noteList = noteDaoUtil.queryAll();
        noteListAdapter.setData(noteList);
        noteListFail.clear();
        for (Note note : noteList) {
            Boolean isSaveServer = note.getIsSaveServer();
            if (!isSaveServer) {
                noteListFail.add(note);
            }
        }
        tvUnloadFail.setText("未同步服务器" + noteListFail.size() + "条");
    }

    @Override
    protected void onStop() {
        super.onStop();
//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// TODO: 2017/8/4  什么时候关闭？？？
//        noteDaoUtil.closeDB();
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

    @OnClick({/*R.id.llUser*/})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llUser:

                break;

        }
    }

    // TODO: 6/8/2017  退出前先把drawer退出
}
