package com.curry.note.module.news.net;

import com.curry.note.bean.BookBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface BookService {

    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     */

    @GET("v2/book/search")
    Call<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

//    @GET("v2/book/{id}")
//    Observable<BookDetailBean> getBookDetail(@Path("id") String id);
}
