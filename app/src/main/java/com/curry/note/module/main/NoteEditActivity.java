package com.curry.note.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.Note;
import com.curry.note.constant.SharedTag;
import com.curry.note.daomanager.NoteDaoUtil;
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
    private long note_id;
    private int operateType;
    private String etNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        noteDaoUtil = new NoteDaoUtil(this);

        resolveIntent();

        // TODO: 5/15/2017  进来之后就要启用软键盘
    }

    private void resolveIntent() {
        Intent intent = getIntent();
        note_id = intent.getLongExtra(SharedTag.NOTE_ID, 0);
        if (note_id != 0) {
            Note note = noteDaoUtil.queryOne(note_id);
            etNote.setText(note.getNoteContent());
        }
        operateType = intent.getIntExtra(SharedTag.TYPE, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void saveNote() {
        // TODO: 5/16/2017  小米便签用什么做唯一标识的
        etNoteContent = etNote.getText().toString();
        if (operateType == SharedTag.TYPE_ADD_NOTE) {
            //新增类型，退出之后不是空就保存本地和服务器
            if (!TextUtils.isEmpty(etNoteContent)) {
                //不是空，并且修改过内容
//                saveLocalAndServer();
            }
        }
        if (operateType == SharedTag.TYPE_EDIT_NOTE) {
            //编辑类型，退出之后不是空就保存更新;删掉之前的note
            if (!TextUtils.isEmpty(etNoteContent)) {
//                saveLocalAndServer();
            }
            deleteOldNote();
        }

    }

    //内容是否改变
    private boolean isModified() {
        Note note = noteDaoUtil.queryOne(note_id);
        return !etNoteContent.equals(note.getNoteContent());
    }

    private void saveLocalAndServer() {
        Note note = new Note();
        note.setNoteContent(etNoteContent);
        note.setTimestamp(System.currentTimeMillis());
        note.setUserId(BmobUser.getCurrentUser().getObjectId());
        noteDaoUtil.addOneNote(note);
        saveServer(note);
    }

    private void deleteOldNote() {
        //删掉原来的
        Note note = new Note();
        note.setTimestamp(note_id);
        noteDaoUtil.deleteUser(note);
        deleteServer();
    }

    // TODO: 5/18/2017  服务器同步失败的话，设想应该保存note下次什么时候再同步
    private void saveServer(Note note) {
        note.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    //success
                    ToastUtils.showShortToast("备份到服务器成功");
                } else {
                    //fail
                    ToastUtils.showShortToast("备份到服务器失败");
                }
            }
        });
    }

    private void deleteServer() {
        Note note = noteDaoUtil.queryOne(note_id);
        note.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShortToast("delete success");
                    // TODO: 5/18/2017  是不是应该打印日志  日志工具类，toast工具类找一个合适的
                } else {
                    ToastUtils.showShortToast("delete fail");
                }
            }
        });
    }

}
