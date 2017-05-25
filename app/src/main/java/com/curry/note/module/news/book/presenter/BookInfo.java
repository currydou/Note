package com.curry.note.module.news.book.presenter;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface BookInfo {
    void getList(String tag,int start,int count,boolean isLoadMore);
}
