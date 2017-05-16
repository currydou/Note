package com.curry.note.daomanager;

import android.content.Context;

import com.curry.note.bean.Note;

import java.util.List;

/**
 * Created by curry.zhang on 5/15/2017.
 */

public class NoteDaoUtil {

    private final GreenDaoManager manager;
    private Context context;

    public NoteDaoUtil(Context context) {
        manager = GreenDaoManager.getInstance();
        manager.init(context);
        this.context = context;
    }

    public boolean addOneNote(Note note) {
        boolean flag = false;
        try {
            flag = manager.getDaoSession().insert(note) != -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除数据
     */
    public boolean deleteUser(Note note) {
        boolean flag = false;
        try {
            manager.getDaoSession().delete(note);
            //删除所有的
//            manager.getDaoSession().deleteAll(User.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 修改数据
     */
    public boolean updateUser(Note note) {
        boolean flag = false;
        try {
            manager.getDaoSession().update(note);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有数据
     */
    public List<Note> queryAll() {                  //这里不用捕获异常（还有下面）、？？？？
        return manager.getDaoSession().loadAll(Note.class);
    }

    /**
     * 查询单条 数据
     */
    public Note queryOne(Long id) {
        return manager.getDaoSession().load(Note.class, id);
    }
}
