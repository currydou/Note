package com.curry.note.module.news.book.presenter;

import android.util.Log;

import com.curry.note.bean.BookBean;
import com.curry.note.constant.Constants;
import com.curry.note.module.news.book.view.IBookView;
import com.curry.note.module.news.net.BookService;

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

    // TODO: 5/16/2017  先用retrofit，再改成rx，再封装
    @Override
    public void getList(String tag,int start,int count) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.DOUBAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);
        Call<BookBean> call = bookService.getBook(tag, start, count);
        call.enqueue(new Callback<BookBean>() {
            @Override
            public void onResponse(Call<BookBean> call, Response<BookBean> response) {
                // TODO: 5/16/2017  这个地方不同情形的判断
                iBookView.showSuccessPage(response.body());
                Log.e("curry", "onResponse: ");
            }

            @Override
            public void onFailure(Call<BookBean> call, Throwable t) {
                iBookView.showFailPage();
                Log.e("curry", "onFailure: ");
            }
        });
    }


}
