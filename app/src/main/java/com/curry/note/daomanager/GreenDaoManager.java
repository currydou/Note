package com.curry.note.daomanager;

import android.content.Context;

import com.curry.note.dao.DaoMaster;
import com.curry.note.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 3,数据库的CRUD操作
 * 4，数据库的升级
 * Created by curry on 2016/10/8.
 */

public class GreenDaoManager {
    private final static String TAG = GreenDaoManager.class.getSimpleName();
    private final static String DB_NAME = "DB_NAME";
    //多线程访问修饰符volatile
    private volatile static GreenDaoManager manager;
    private static DaoMaster.DevOpenHelper helper;
    private static DaoSession daoSession;
    private static DaoMaster daoMaster;
    private Context context;

    /**
     * 初始化上下文
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 使用单例模式获取数据库对象
     */
    public static GreenDaoManager getInstance() {
        if (manager == null) {
            synchronized (GreenDaoManager.class) {
                if (manager == null) {
                    manager = new GreenDaoManager();
                }
            }
        }
        return manager;
    }

    /**
     * 判断是否存在数据库，如果没有就创建
     */
    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        return daoMaster;
    }

    /**
     * 完成对数据库的增删改查操作，仅仅是一个接口
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 打开输出日志的操作，默认是关闭
     */
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭会话
     */
    public void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    /**
     * 关闭帮助类
     * <p>
     * 关闭之后再设置为null？
     */
    public void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

    /**
     * 全部关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

}
