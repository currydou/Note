package com.curry.note.module.news.music.presenter;


import com.curry.note.bean.music.MusicRoot;
import com.curry.note.constant.Constants;
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

    // TODO: 5/16/2017  先用retrofit，再改成rx，再封装
    @Override
    public void getList(String tag, int start, int count) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.DOUBAN_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DouBanService douBanService = retrofit.create(DouBanService.class);
        // TODO: 5/17/2017  rx有个类好像一般实际用的比较多，androidmvpsample里用到的
        Observable<MusicRoot> musicRootObservable = douBanService.searchMusicByTag(tag, start, count);
        musicRootObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        iMusicView.onStart();
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
                    iMusicView.onSuccess(musicRoot);
                } else {
                    iMusicView.onFailure(new Exception());//new 一个exception？
                }
            }
        });
    }


}
