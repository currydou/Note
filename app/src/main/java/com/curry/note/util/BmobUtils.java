package com.curry.note.util;

import android.util.Log;

import com.curry.note.bean.bmob.Note;
import com.curry.note.bean.bmob.User;
import com.curry.note.listener.IBmobLoginListener;
import com.curry.note.listener.IBmobRegisterListener;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 1：当bean增加了新的字段，但是在服务端没有增加，这时候插入，不会报错，但是新增的字段的值也不会插入成功，只会插入已有的字段
 *      结果：可以这样插入新的字段，要刷新整个页面才能看到
 *
 *
 */
public class BmobUtils {

    private static String objID;
    private static IBmobLoginListener mListener;
    private static BmobUtils instance = null;

    //单例
    public static BmobUtils getInstance(IBmobLoginListener mListener2) {
        mListener = mListener2;
        if (instance == null) {
            return new BmobUtils();
        }
        return instance;
    }

    /***
     * 发送手机验证码
     *
     * @param phoneNumber
     */
    public void sendMsg(String phoneNumber) {
        BmobSMS.requestSMSCode(phoneNumber, "layer", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    mListener.sendMsgSuccess();
                }
            }
        });
    }

    /***
     * 通过验证码登录
     *
     * @param phoneNumber
     * @param code
     */
    public void loginByMsg(String phoneNumber, String code) {
        BmobUser.loginBySMSCode(phoneNumber, code, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    mListener.loginSuccess();
                }
            }
        });
    }
    /***
     * 添加数据
     *
     * @param note
     */
    public static void insertData(Note note) {// TODO: 5/18/2017  这里的对象能不能用泛型或者多态？？方法以后想想能不能重构
        //注意：不能调用gameScore.setObjectId("")方法!!!!
        note.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    objID = objectId;
                } else {

                }
            }
        });
    }

    /***
     * 删除数据
     * <p>
     * //     * @param model
     */
    public static void deleteData(String objectId) {
        BmobObject favorInfo = new BmobObject();
        favorInfo.setObjectId(objectId);
        favorInfo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "成功");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    public static void updateData() {

    }


    /**
     * 查询数据 （好像会根据实体类自动创建表）
     *
     * @param model
     * @param queryKey
     * @param queryValue
     */
    public void queryData(BmobObject model, String queryKey, Object queryValue) {
        BmobQuery<BmobObject> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo(queryKey, queryValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<BmobObject>() {
            @Override
            public void done(List<BmobObject> object, BmobException e) {
                if (e == null) {
                    mListener.querySuccess(object);
                } else {
                    mListener.queryFailure(e);
                }
            }
        });
    }

    public void bmobRegister(String name, String password, final IBmobRegisterListener listener) {
        User myUser = new User();
        myUser.setUsername(name);
        myUser.setPassword(password);
        myUser.setHeadPortraitUrl("11");
        //注意：不能用save方法进行注册
        myUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if (e == null) {
                    listener.registerSuccess(s);
                } else {
                    listener.registerFailure(e);
                }
            }
        });
    }


}
