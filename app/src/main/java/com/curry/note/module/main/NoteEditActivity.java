package com.curry.note.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.Note;
import com.curry.note.constant.Constants;
import com.curry.note.daomanager.NoteDaoUtil;
import com.curry.note.util.KeyboardUtils;
import com.curry.note.util.LogUtil;
import com.curry.note.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class NoteEditActivity extends BaseActivity {

    @BindView(R.id.etNote)
    EditText etNote;
    private NoteDaoUtil noteDaoUtil;
    private long noteId;
    private int operateType;
    private String etNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);
        init();
        resolveIntent();

    }

    private void init() {
        noteDaoUtil = new NoteDaoUtil(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showSoftInput(etNote);
                //或者下面的
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    private void resolveIntent() {
        Intent intent = getIntent();
        noteId = intent.getLongExtra(Constants.NOTE_ID, 0);
        if (noteId != 0) {
            Note note = noteDaoUtil.queryOne(noteId);
            etNote.setText(note.getNoteContent());
            //将光标移到最后
            etNote.setSelection(note.getNoteContent().length());
        }
        operateType = intent.getIntExtra(Constants.TYPE, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void saveNote() {
        //   小米便签用什么做唯一标识的。找到了也没看懂什么意思
        etNoteContent = etNote.getText().toString();
        if (operateType == Constants.TYPE_ADD_NOTE) {
            //新增类型，退出之后不是空就保存本地和服务器
            if (!TextUtils.isEmpty(etNoteContent)) {
                //不是空，并且修改过内容
                saveLocalAndServer();
            }
        }
        if (operateType == Constants.TYPE_EDIT_NOTE) {
            //编辑类型，退出之后不是空就保存更新;删掉之前的note
            if (!TextUtils.isEmpty(etNoteContent)) {
                saveLocalAndServer();
            }
            deleteOldNote();
        }

    }

    //内容是否改变
    private boolean isModified() {
        Note note = noteDaoUtil.queryOne(noteId);
        return !etNoteContent.equals(note.getNoteContent());
    }

    private void saveLocalAndServer() {
        Note note = new Note();
        note.setId(System.currentTimeMillis());
        note.setNoteContent(etNoteContent);
        note.setTimestamp(System.currentTimeMillis());
        note.setUserId(BmobUser.getCurrentUser().getObjectId());
        noteDaoUtil.addOneNote(note);
        saveServer(note);
    }

    private void deleteOldNote() {
        deleteServer();
        //删掉原来的
        Note note = new Note();
        note.setId(noteId);
        noteDaoUtil.deleteUser(note);
    }

    //服务器同步失败的话，设想应该保存note下次什么时候再同步
    private void saveServer(final Note note) {
        note.setIsSaveServer(true);
        note.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    //success
                    ToastUtils.showShortToast("备份到服务器成功");
                    note.setIsSaveServer(true);
                    //第一次创建的便签没有objectid，设置一下
                    String localObjectId = note.getObjectId();
                    if (TextUtils.isEmpty(localObjectId)) {
                        note.setObjectId(objectId);
                    }
                } else {
                    //fail.失败的时候是没有objectid的。。。
                    ToastUtils.showShortToast("备份到服务器失败");
                    LogUtil.d("备份到服务器失败" + e.getMessage());
                    note.setIsSaveServer(false);
                }

                noteDaoUtil.updateUser(note);
            }
        });
    }


    // 要先上传才有objectid
    //还是删除的时候提示的没有objectid，新增不需要objectid
    //没网的时候新增，有网的时候修改功能(增新的，删旧的)，删除功能(删旧的),会有这个提示。(在删旧的的时候，提示没有objectid)
    private void deleteServer() {
        Note note = noteDaoUtil.queryOne(noteId);
        //如果objectid为空不让删除服务器的？因为服务器上也没有，而且也会报错。对！
        String objectId = note.getObjectId();
        if (TextUtils.isEmpty(objectId)) {
            return;
        }
        note.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShortToast("delete success");
                } else {
                    ToastUtils.showShortToast("delete fail" + e.getMessage());
                    LogUtil.d("delete fail" + e.getMessage());
                }
            }
        });
    }

}
