package com.curry.note.module.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.listener.IBmobRegisterListener;
import com.curry.note.util.BmobUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        tvTitle.setText("注册");
    }

    @OnClick({R.id.btnRegister, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private void register() {
        String name = etUserName.getText().toString().trim();
        String pwd1 = etPassword.getText().toString().trim();
        String pwd2 = etRepeatPassword.getText().toString().trim();
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("mylog", "myRegister: " + name + "," + pwd1);
        BmobUtils.getInstance(null).bmobRegister(name, pwd1, new IBmobRegisterListener() {
            @Override
            public void registerSuccess(BmobUser bmobUser) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void registerFailure(Exception e) {
                Toast.makeText(RegisterActivity.this, "错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
