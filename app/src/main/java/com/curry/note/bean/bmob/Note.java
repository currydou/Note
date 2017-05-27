package com.curry.note.bean.bmob;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import cn.bmob.v3.BmobObject;

/**
 * Created by curry on 2017/5/14.
 * <p>
 * 如果bean父类有字段，虽然生成的代码没有显示的把父类字段的值插入，但是查询的时候可以获取到
 * <p>
 * todo 之前看databinding时，记得说过第三方封装的不好弄，通过什么方式，然后就可以了。
 * 1.现在我想用bomb的objectid作为greendao的主键，需要在objectid(不能重写)上加上注解，不知道这种方式可不可以
 * 2.不知道是不是只能用主键来删除？
 */

@Entity
public class Note extends BmobObject {

    @Id
    private Long id;//做主键，用于数据库删除
    private Long timestamp;//用时间戳 （可以用时间戳作为主键，1.麻烦 ；2.容易弄乱）
    private String noteContent;//便签内容
    private String userId;//所属用户的id


    public Note() {
    }



    @Generated(hash = 775148314)
    public Note(Long id, Long timestamp, String noteContent, String userId) {
        this.id = id;
        this.timestamp = timestamp;
        this.noteContent = noteContent;
        this.userId = userId;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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


}