package com.curry.note.module.news.movie.presenter;

import com.curry.note.bean.top250.Root;
import com.curry.note.module.news.movie.view.IMovieView;
import com.curry.note.module.news.net.DouBanService;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by curry.zhang on 5/17/2017.
 */

public class MoviePresenterImpl implements IMoviePresenter {

    private IMovieView iMovieView;

    public MoviePresenterImpl(IMovieView iMovieView) {
        this.iMovieView = iMovieView;
    }

    @Override
    public void getList(String tag, int start, int count, final boolean isLoadMore) {
        DouBanService douBanService = DouBanService.Buidler.getDouBanService();
        Observable<Root> observable = douBanService.getTop250(start, count);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        iMovieView.onRequestStart();
                    }
                }).subscribe(new Subscriber<Root>() {
            @Override
            public void onCompleted() {
                iMovieView.onFinish();
            }

            @Override
            public void onError(Throwable e) {
                iMovieView.onFailure(e);
                iMovieView.onFinish();
            }

            @Override
            public void onNext(Root root) {
                if (root != null) {
                    iMovieView.onSuccess(root, isLoadMore);
                } else {
                    iMovieView.onFailure(new Exception());//new 一个exception？
                }
            }
        });
    }
}
