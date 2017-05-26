package com.curry.note.bean.bmob;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobUser;

/**
 * Created by curry on 2017/5/14.
 * id(可以用它的objectid)，用户名，密码，头像，
 */

@Entity
public class User extends BmobUser{
    private String headPortraitUrl;
    @Generated(hash = 586692638)
    public User() {
    }
    public String getHeadPortraitUrl() {
        return this.headPortraitUrl;
    }
    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }
    @Generated(hash = 1728833896)
    public User(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

}