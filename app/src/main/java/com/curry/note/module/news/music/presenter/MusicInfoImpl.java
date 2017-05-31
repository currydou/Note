package com.curry.note.module.news.music.presenter;


import com.curry.note.bean.music.MusicRoot;
import com.curry.note.constant.URLConfig;
import com.curry.note.module.news.music.view.IMusicView;
import com.curry.note.module.news.net.DouBanService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by curry.zhang on 5/16/2017.
 */

public class MusicInfoImpl implements MusicInfo {

    private IMusicView iMusicView;

    public MusicInfoImpl(IMusicView iMusicView) {
        this.iMusicView = iMusicView;
    }

    @Override
    public void getList(String tag, int start, int count, final boolean isLoadMore) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLConfig.DOUBAN_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DouBanService douBanService = retrofit.create(DouBanService.class);
        //  rx有个类好像一般实际用的比较多，androidmvpsample里用到的
        Observable<MusicRoot> musicRootObservable = douBanService.searchMusicByTag(tag, start, count);
        musicRootObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        iMusicView.onRequestStart();
                    }
                }).subscribe(new Subscriber<MusicRoot>() {
            @Override
            public void onCompleted() {
                iMusicView.onFinish();
            }

            @Override
            public void onError(Throwable e) {
                iMusicView.onFailure(e);
                iMusicView.onFinish();
            }

            @Override
            public void onNext(MusicRoot musicRoot) {
                if (musicRoot != null) {
                    iMusicView.onSuccess(musicRoot, isLoadMore);
                } else {
                    iMusicView.onFailure(new Exception());//new 一个exception？
                }
            }
        });
    }


}
