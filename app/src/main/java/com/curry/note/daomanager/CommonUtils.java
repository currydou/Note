package com.curry.note.daomanager;

import android.content.Context;

import com.curry.note.bean.User;
import com.curry.note.dao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 *  http://greenrobot.org/files/greendao/javadoc/3.1/
 *
 *
 * 先测试老师的单例模式，然后看完异常
 * 再看下面的
 * 看之前保存的Greendao使用经验文章
 * 解决下面的疑问z
 *
 * 局部变量必须赋初始值，没有默认值
 * 成员变量可以不赋初始值，有默认值
 *
 * 根据id来操作
 * 数据库中的id是  _id
 *
 * Created by curry on 2016/10/8.
 * <p>
 */

public class CommonUtils {

    private final GreenDaoManager manager;
    private Context context;

    public CommonUtils(Context context) {
        manager = GreenDaoManager.getInstance();
        manager.init(context);
        this.context = context;
    }

    /**
     * 完成对数据表user的插入操作
     */
    public boolean insertUser(User user) {
        boolean flag = false;
        try {
            flag = manager.getDaoSession().insert(user) != -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入多条数据
     *
     */
    public boolean insertMultiUser(final List<User> userList) {
        final boolean flag = false;
        try {
            manager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (User user : userList)  {
                        manager.getDaoSession().insertOrReplace(user);      //这个方法，查看api
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除数据
     */
    public boolean deleteUser(User user) {
        boolean flag = false;
        try {
            manager.getDaoSession().delete(user);
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
    public boolean updateUser(User user) {
        boolean flag = false;
        try {
            manager.getDaoSession().update(user);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询单条 数据
     */
    public User queryOne(Long key) {
        return manager.getDaoSession().load(User.class, key);
    }

    /**
     * 查询所有数据
     */
    public List<User> queryAll() {                  //这里不用捕获异常（还有下面）、？？？？
        return manager.getDaoSession().loadAll(User.class);
    }

    /**
     * 调用原生的查询
     */
    public List<User> query1() {
        //使用native sql 进行查询操作
        return manager.getDaoSession().queryRaw(User.class,
                "where name like ? and _id >?", new String[]{"%张$", "100"});
    }

    /**
     *ge  >=
     * gt >
     * le <=
     * lt <
     *
     */
    public List<User> query2(long key) {
        //查询构建器
        QueryBuilder<User> builder = manager.getDaoSession().queryBuilder(User.class);
        //并且
        builder.where(UserDao.Properties.Id.ge(key)).where(UserDao.Properties.Name.eq("%张三%"));
        return builder.list();
    }

    /**
     *
     */
    public List<User> query3(long key) {
        QueryBuilder<User> builder = manager.getDaoSession().queryBuilder(User.class);
        //and和or 同时用
        builder.where(UserDao.Properties.Id.gt(key),
                UserDao.Properties.Name.like("%张三%")).limit(2);
        return builder.list();
    }
}
