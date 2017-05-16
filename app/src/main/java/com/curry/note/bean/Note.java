package com.curry.note.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by curry on 2017/5/14.
 */

@Entity
public class Note {
    @Id
    private Long id;
    private String userName;
    private String title;
    private String noteContent;
    private Long timestamp;
    @Transient
    private int tempUsageCount; // not persisted
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Note() {
    }

    public Note(Long id, String userName, String title, String noteContent, Long timestamp, int tempUsageCount) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.noteContent = noteContent;
        this.timestamp = timestamp;
        this.tempUsageCount = tempUsageCount;
    }
    @Generated(hash = 1956976561)
    public Note(Long id, String userName, String title, String noteContent, Long timestamp) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.noteContent = noteContent;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTempUsageCount() {
        return tempUsageCount;
    }

    public void setTempUsageCount(int tempUsageCount) {
        this.tempUsageCount = tempUsageCount;
    }
}