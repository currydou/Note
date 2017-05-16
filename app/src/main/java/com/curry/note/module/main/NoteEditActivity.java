package com.curry.note.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.curry.note.R;
import com.curry.note.bean.Note;
import com.curry.note.daomanager.NoteDaoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEditActivity extends AppCompatActivity {

    @BindView(R.id.etNote)
    EditText etNote;
    private NoteDaoUtil noteDaoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);
        etNote = (EditText) findViewById(R.id.etNote);

        noteDaoUtil = new NoteDaoUtil(this);

        Intent intent = getIntent();
        long note_id = intent.getLongExtra("note_id", 0);
        if (note_id != 0) {
            Note note = noteDaoUtil.queryOne(note_id);
            etNote.setText(note.getNoteContent());
        }

        // TODO: 5/15/2017  进来之后就要启用软键盘
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void saveNote() {//是插入，是怎么做唯一标识的
        // TODO: 5/16/2017  更新后的是一条新的，原来的要删掉，空格不保存
        String etNoteContent = etNote.getText().toString();
        if (!TextUtils.isEmpty(etNoteContent)) {
            Note note = new Note();
            note.setId(System.currentTimeMillis());
            note.setNoteContent(etNoteContent);
            note.setTimestamp(System.currentTimeMillis());
            noteDaoUtil.addOneNote(note);
        }
    }
}
