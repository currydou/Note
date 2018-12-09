package com.yodoo.android.baseutil.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yodoo.android.baseutil.base.lifecycle.ActivityLifecycleable;
import com.yodoo.android.baseutil.mvp.IPresenter;
import com.yodoo.android.baseutil.utils.ArmsUtils;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lib on 2017/7/12.
 * 所有activity基类
 * <p>
 * 2018-8-29 将RxAppCompatActivity内的功能全部移到这里，并改为继承FragmentActivity
 * 因为要使用support 27的新组件和功能，如果继承AppcompatActivity，会有findViewById的冲突
 * 而新组件需要使用27，所以这两玩意就矛盾了
 * <p>
 * 2018-9-6 重写findviewById重写可以解决矛盾
 */

public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity implements IActivity, ActivityLifecycleable, LifecycleProvider<ActivityEvent> {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    protected P mPresenter;
    private Unbinder mBinder;
    @Inject
    protected ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler();

    @Override
    public View findViewById(int id) {
        return getDelegate().findViewById(id);
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= 26) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        try {
            int layoutResID = initView(savedInstanceState);
            if (layoutResID != 0) {//如果initView返回0,框架则不会调用setContentView()
                setContentView(layoutResID);
                mBinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onPause() {
        if (mProgressDialog != null)
            mProgressDialog.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) mProgressDialog.onDestroy();
        mProgressDialog = null;
        if (mBinder != null && mBinder != Unbinder.EMPTY)
            mBinder.unbind();
        this.mBinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    public void afterFragmentViewInit(BaseFragment fragment) {

    }

    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    public void launchActivity(Intent intent, Class target) {
        intent.setClass(this, target);
        launchActivity(intent);
    }

    public void launchActivityForResult(Intent intent, Class target, int requestCode) {
        intent.setClass(this, target);
        startActivityForResult(intent, requestCode);
    }

    public void showLoading() {
        if (mProgressDialog != null)
            mProgressDialog.showProgressDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismissProgressDialog();
    }


    public void showMessage(String s) {
        ArmsUtils.snackbarText(s);
    }

    public void killMyself() {
        onBackPressed();
    }

    //优化 增加延迟退出 2018-7-27 LiBin
    public void killMyselfDelay(int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, time);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //获取焦点控件
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        //必不可少，否则所有组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                //点击的输入框区域，保留点击Edittext事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());
    }

}
