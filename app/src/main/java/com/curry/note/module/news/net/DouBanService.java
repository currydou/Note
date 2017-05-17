package com.curry.note.module.news.net;


import com.curry.note.bean.BookRoot;
import com.curry.note.bean.MusicRoot;
import com.curry.note.bean.Musics;
import com.curry.note.bean.top250.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface DouBanService {

    class Buidler {
        public static DouBanService getDouBanService() {
            return HttpUtils.getInstance().getDouBanRetrofit(DouBanService.class);
        }
    }

    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param count 一次请求的数目 最多100
     */

    @GET("v2/book/search")
    Call<BookRoot> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

//    @GET("v2/book/{id}")
//    Observable<BookDetailBean> getBookDetail(@Path("id") String id);


    /**
     * 根据tag获取music｀
     *
     * @param tag
     * @return
     */

    @GET("v2/music/search")
    Observable<MusicRoot> searchMusicByTag(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("v2/music/{id}")
    Observable<Musics> getMusicDetail(@Path("id") String id);


    /**
     * 获取top250
     * @param start
     * @param count
     * @return
     */

    @GET("v2/movie/top250")
    Observable<Root> getTop250(@Query("start")int start, @Query("count")int count);
}
