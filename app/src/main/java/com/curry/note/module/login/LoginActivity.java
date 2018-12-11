package com.curry.note.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.User;
import com.curry.note.constant.Constants;
import com.curry.note.module.main.NoteListActivity;
import com.curry.note.util.LogUtil;
import com.curry.note.util.ToastUtils;
import com.curry.note.util.imageloader.ImageLoader;
import com.curry.note.util.imageloader.glide.GlideImageConfig;
import com.curry.note.util.imageloader.glide.GlideImageLoaderStrategy;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * qq登录的问题
 * http://bbs.mob.com/thread-118-1-1.html
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvRegister) TextView tvRegister;
    @BindView(R.id.etUserName) EditText etUserName;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.qq_account) TextView qqAccount;
    @BindView(R.id.sina_weibo) TextView sinaWeibo;
    @BindView(R.id.qq_weixin) TextView qqWeixin;
    @BindView(R.id.ivBack) ImageView ivBack;
    @BindView(R.id.imageView) ImageView imageView;

    public static final int THIRD_LOGIN = 0x11;
    public static final int UPDATE_INFO = 0x12;

    private MyHandler handler = new MyHandler(this);

    // TODO: 6/6/2017  星巴克的handler为什么没有这样写，而且还加了mainlooper参数，本来不就是在activity中，就是主线程吗？？
    private static class MyHandler extends Handler {

        private final WeakReference<LoginActivity> activityWeakReference;

        MyHandler(LoginActivity loginActivity) {
            activityWeakReference = new WeakReference<>(loginActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (activityWeakReference.get() == null) {
                return;
            }
            Bundle bundle = (Bundle) msg.obj;
            switch (msg.what) {
                case THIRD_LOGIN:
                    activityWeakReference.get().thirdLogin(bundle);
                    break;
                case UPDATE_INFO:
                    activityWeakReference.get().updateInfo(bundle);
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            Log.e("curry1-getObjectId1", currentUser.getObjectId());
        }
        initData();


        ImageLoader imageLoader = new ImageLoader(new GlideImageLoaderStrategy());
        imageLoader.loadImage(this, GlideImageConfig.builder()
                .url("https://upload-images.jianshu.io/upload_images/3385286-4e9aa5fa367341f5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/601/format/webp")
                .imageView(imageView)
                .build());

    }

    private void initData() {
        tvTitle.setText("登录");
        tvRegister.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btnLogin, R.id.tvRegister, R.id.qq_account, R.id.sina_weibo, R.id.qq_weixin, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                countLogin();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.qq_account:
                authorize(ShareSDK.getPlatform(QQ.NAME));
                break;
            case R.id.sina_weibo:
                break;
            case R.id.qq_weixin:
                break;
            case R.id.ivBack:
                onBackPressed();
                break;

        }
    }

    /***
     * 账号登录操作
     */
    public void countLogin() {

        final String username = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "您的用户名或者密码为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        User.loginByAccount(username, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功1", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, NoteListActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "登录失败1" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    /**
     * 第三方登录
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        //判断是否完成授权
        if (platform.isAuthValid()) {
            String userId = platform.getDb().getUserId();
            if (userId != null) {
//                saveUserInfo(platform);
                return;
            }
        }
        platform.setPlatformActionListener(new MyPlatformListener());
//        platform.authorize();
        platform.showUser(null);
    }

    /**
     * 回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
     */
    class MyPlatformListener implements PlatformActionListener {

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
//            showToast("登录失败" + arg2.getMessage());
            LogUtil.d("curry1-onError    " + arg2.getMessage());
            // TODO: 5/27/2017  Must be called from main thread of fragment host！  onComplete执行了然后又到这里报这个错？
        }

        @Override
        public void onComplete(Platform platform, int arg1, HashMap<String, Object> arg2) {
            String userName = platform.getDb().getUserName();
            String url = platform.getDb().getUserIcon();//暂时将qqid保存一个字段，登录时判断是否有这个用户，有就用已有的用户信息，没有就用qq信息，密码默认123456
            String QQId = platform.getDb().getUserId();
            String token = platform.getDb().getToken();
            String expiresIn = platform.getDb().getExpiresIn() + "";

            Message msg = Message.obtain();
            msg.what = THIRD_LOGIN;
            Bundle bundle = new Bundle();
            bundle.putString("userName", userName);
            bundle.putString("url", url);
            bundle.putString("QQId", QQId);
            bundle.putString("token", token);
            bundle.putString("expiresIn", expiresIn);
            msg.obj = bundle;
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            showToast("登录取消");
        }
    }

    private void thirdLogin(final Bundle bundle) {
        String QQId = bundle.getString("QQId");
        String token = bundle.getString("token");
        String expiresIn = bundle.getString("expiresIn");

        BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_QQ, token, expiresIn, QQId);
        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {

            @Override
            public void done(JSONObject userAuth, BmobException e) {
                if (e == null) {
                    //登录成功
                    Message msg = Message.obtain();
                    msg.what = UPDATE_INFO;
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    private void updateInfo(Bundle bundle) {
        final String userName = bundle.getString("userName");
        final String url = bundle.getString("url");
        String QQId = bundle.getString("QQId");
        User currentUser = BmobUser.getCurrentUser(User.class);
        final Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
        if (TextUtils.isEmpty(currentUser.getQQId())) {
            //第一次用qq登录，用qq昵称和头像
            User user = new User();
            user.setUsername(userName);
            user.setHeadPortraitUrl(url);
            user.setQQId(QQId);
            user.update(currentUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    LogUtil.d("111111111111111");
                    if (e == null) {
                        intent.putExtra(Constants.USER_NAME2, userName);
                        intent.putExtra(Constants.HEAD_PORTRAIT_URL, url);
                        showToast("登录成功");
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.showLongToast("更新信息失败");
                    }
                }
            });

        } else {
            //不是第一次登陆，用已有账户中的信息(传空串，显示已有账户中的信息)
            intent.putExtra(Constants.USER_NAME2, "");
            showToast("登录成功");
            startActivity(intent);
            finish();
        }

    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortToast(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
