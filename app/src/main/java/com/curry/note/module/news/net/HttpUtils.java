package com.curry.note.module.news.net;

import com.curry.note.constant.URLConfig;

import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by curry.zhang on 5/17/2017.
 */

public class HttpUtils {

    private static HttpUtils instance;
    private Object retrofit;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }


    public <T> T getDouBanRetrofit(Class<T> a) {
        if (retrofit == null) {
            synchronized (HttpUtils.class) {
                if (retrofit == null) {
                    retrofit = getBuilder(URLConfig.DOUBAN_BASE_URL).build().create(a);
                }
            }
        }
        return (T) retrofit;
    }

    private Retrofit.Builder getBuilder(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
//        builder.client(getOkClient());
        builder.baseUrl(baseUrl);//设置远程地址
//        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create());
//        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

}
