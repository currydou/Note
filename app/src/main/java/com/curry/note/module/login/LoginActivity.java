package com.curry.note.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.bean.bmob.User;
import com.curry.note.constant.SharedTag;
import com.curry.note.util.SPUtils;
import com.curry.note.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * qq登录的问题
 * http://bbs.mob.com/thread-118-1-1.html
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.qq_account)
    TextView qqAccount;
    @BindView(R.id.sina_weibo)
    TextView sinaWeibo;
    @BindView(R.id.qq_weixin)
    TextView qqWeixin;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initData();

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
//        Toast.makeText(LoginActivity.this, username + "-登录-" + password, Toast.LENGTH_LONG).show();
        User.loginByAccount(username, password, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功1", Toast.LENGTH_SHORT).show();
//                    //登录过后保存用户名
//                    CacheUtils.setString(LoginActivity.this, SharedTag.USERNAME, username);
//                    noteApplication.spUtils.putString(SharedTag.USER_NAME, user.getUsername());

                } else {
                    Toast.makeText(LoginActivity.this, "登录失败1", Toast.LENGTH_SHORT).show();
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
                saveUserInfo(platform);
                return;
            }
        }
        platform.setPlatformActionListener(new MyPlatformListener());
        platform.authorize();
        platform.showUser(null);
    }

    /**
     * 回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
     */
    class MyPlatformListener implements PlatformActionListener {

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            showToast("登录失败" + arg2.getMessage());
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            //保存用户信息
            saveUserInfo(arg0);
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            showToast("登录取消");
        }
    }

    private void saveUserInfo(Platform platform) {
        String userName = platform.getDb().getUserName();
        String url = platform.getDb().getUserIcon();
        // TODO: 5/26/2017  当前页面闪退，到前一个页面，再点进来，qq登录，这里空指针。实例被销毁？
        //在application中实例化过一次。其他项目用application中的对象，怎么用？
        SPUtils spUtils = new SPUtils(SharedTag.SP_USER);
        spUtils.putString(SharedTag.USER_NAME, userName);
        spUtils.putString(SharedTag.USER_ICON_URL, url);
        showToast("登录成功");
        setResult(0);
        onBackPressed();
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShortToast(text);
            }
        });
    }

}
