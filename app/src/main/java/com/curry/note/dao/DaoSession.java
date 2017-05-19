package com.curry.note.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.curry.note.bean.bmob.Note;
import com.curry.note.bean.bmob.User;

import com.curry.note.dao.NoteDao;
import com.curry.note.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteDaoConfig;
    private final DaoConfig userDaoConfig;

    private final NoteDao noteDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        noteDao = new NoteDao(noteDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Note.class, noteDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        noteDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
