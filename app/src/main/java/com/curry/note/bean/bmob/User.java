package com.curry.note.bean.bmob;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobUser;

/**
 * Created by curry on 2017/5/14.
 * id(可以用它的objectid)，用户名，密码，头像，
 */

@Entity
public class User extends BmobUser {
    private String headPortraitUrl;
    private String QQId;

    @Generated(hash = 586692638)
    public User() {
    }

    public String getHeadPortraitUrl() {
        return this.headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getQQId() {
        return this.QQId;
    }

    public void setQQId(String QQId) {
        this.QQId = QQId;
    }

    @Generated(hash = 1777243736)
    public User(String headPortraitUrl, String QQId) {
        this.headPortraitUrl = headPortraitUrl;
        this.QQId = QQId;
    }

}