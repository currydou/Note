package com.yodoo.android.baseutil.base;

import android.app.Application;

import com.yodoo.android.baseutil.di.module.AppModule;

import org.greenrobot.greendao.AbstractDaoSession;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lib on 2017/11/23.
 */
@Singleton
public class DBHelper {
    private Map<String, AbstractDaoSession> mSessions = new HashMap<>();

    @Inject
    public DBHelper(Application application, AppModule.DataBase dataBase) {
    }

    public void registerSession(String module, AbstractDaoSession session) {
        mSessions.put(module, session);
    }

    public AbstractDaoSession getSession(String module) {
        AbstractDaoSession session = mSessions.get(module);
        if (session == null) throw new NullPointerException(module + " is not register");
        return mSessions.get(module);
    }
}
