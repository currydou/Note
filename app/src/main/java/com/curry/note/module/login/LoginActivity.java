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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

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

    @OnClick({R.id.btnLogin,R.id.tvRegister, R.id.qq_account, R.id.sina_weibo, R.id.qq_weixin,R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                countLogin();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.qq_account:
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
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    //登录过后保存用户名
//                    CacheUtils.setString(LoginActivity.this, SharedTag.USERNAME, username);
                    noteApplication.spUtils.putString(SharedTag.USER_NAME, user.getName());

                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
