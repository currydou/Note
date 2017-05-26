package com.curry.note.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by curry.zhang on 5/26/2017.
 */

public class Test2 extends BmobObject {
    private String test;
    private User user;

    public Test2(String test, User user) {
        this.test = test;
        this.user = user;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
