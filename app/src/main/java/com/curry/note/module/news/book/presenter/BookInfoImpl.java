package com.curry.note.module.news.book.presenter;

import android.util.Log;

import com.curry.note.bean.book.BookRoot;
import com.curry.note.constant.URLConfig;
import com.curry.note.module.news.book.view.IBookView;
import com.curry.note.module.news.net.DouBanService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by curry.zhang on 5/16/2017.
 */

public class BookInfoImpl implements BookInfo {

    private IBookView iBookView;

    public BookInfoImpl(IBookView iBookView) {
        this.iBookView = iBookView;
    }

    @Override
    public void getList(String tag, int start, int count, final boolean isLoadMore) {
        iBookView.showLoadingPage();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLConfig.DOUBAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DouBanService douBanService = retrofit.create(DouBanService.class);
        Call<BookRoot> call = douBanService.getBook(tag, start, count);
        call.enqueue(new Callback<BookRoot>() {
            @Override
            public void onResponse(Call<BookRoot> call, Response<BookRoot> response) {
                //  这个地方不同情形的判断
                iBookView.showSuccessPage(response.body(), isLoadMore);
                Log.e("curry", "onResponse: ");
            }

            @Override
            public void onFailure(Call<BookRoot> call, Throwable t) {
                iBookView.showFailPage();
                Log.e("curry", "onFailure: ");
            }
        });
    }


}
