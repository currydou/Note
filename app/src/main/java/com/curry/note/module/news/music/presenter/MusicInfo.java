package com.curry.note.module.news.music.presenter;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface MusicInfo {
    void getList(String tag, int start, int count,boolean isLoadMore);
}
