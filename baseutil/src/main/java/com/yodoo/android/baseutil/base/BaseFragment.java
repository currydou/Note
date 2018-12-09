package com.yodoo.android.baseutil.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yodoo.android.baseutil.base.lifecycle.FragmentLifecycleable;
import com.yodoo.android.baseutil.mvp.IPresenter;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lib on 2017/7/12.
 */

public abstract class BaseFragment<P extends IPresenter> extends RxFragment implements IFragment, FragmentLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    protected View mMainView;
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    protected P mPresenter;
    @Inject
    protected ProgressDialog mProgressDialog;

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments(new Bundle());
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = initView(inflater, container, savedInstanceState);
        initChildView();
        if (getActivity() instanceof IActivity) {
            ((IActivity) getActivity()).afterFragmentViewInit(this);
        }
        return mMainView;
    }

    public void showLoading() {
        if (mProgressDialog != null)
            mProgressDialog.showProgressDialog(getActivity());
    }

    public void hideLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismissProgressDialog();
    }

    @Override
    public void onPause() {
        if (mProgressDialog != null)
            mProgressDialog.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) mProgressDialog.onDestroy();
        mProgressDialog = null;
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    public void launchActivity(Intent intent, Class target) {
        intent.setClass(getActivity(), target);
        launchActivity(intent);
    }

    public void launchActivityForResult(Intent intent, Class target, int requestCode) {
        intent.setClass(getActivity(), target);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 加载布局中的其他view
     */
    protected abstract void initChildView();

    public boolean onBackPress() {
        return false;
    }

    public void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getName());
    }

}
