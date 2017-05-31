package com.curry.note.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.curry.note.R;
import com.curry.note.base.BaseActivity;
import com.curry.note.module.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 关于用户功能
 * 1.看了几个有自己的帐号系统app，注册都是用手机号，暂时不想这么做
 * 2.还有一个是只能用第三方帐号登录的，也有用户相关信息，只是改不了。
 *
 * 希望这个app简单一点，同时练到常用的功能
 * 可以提供两条选择，一种正常的，一种简单的
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tvExit)
    TextView tvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

    }

    // 每次都要授权，并且不是以qq信息作为死的登录名，如果修改了，下次qq登录依然是修改的。看莱聚+的，以及其他的
    @OnClick(R.id.tvExit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvExit:
                //清除帐号信息，跳到登录界面
                BmobUser.logOut();
                ShareSDK.getPlatform(QQ.NAME).removeAccount(true);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
