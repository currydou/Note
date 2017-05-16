package com.curry.note.module.news.book.presenter;

import com.curry.note.bean.User;
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
    public void getList(/*具体的参数需要看豆瓣api的接口*/) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);
        Call<User> call = bookService.getString("", "15261595841");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // TODO: 5/16/2017  这个地方不同情形的判断
                iBookView.showSuccessPage(response.body());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                iBookView.showFailPage();
            }
        });
    }


}
