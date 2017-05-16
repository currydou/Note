package com.curry.note.module.news.net;

import com.curry.note.bean.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by curry.zhang on 5/16/2017.
 */

public interface BookService {


    @GET("/apistore/mobilenumber/mobilenumber")
    Call<User> getString(@Header("apikey") String apikey,
                         @Query("phone") String phone);


    @POST("Test")
    void getString2(@Query("username") String aa,
                    @Query("pwd") String dd);
}
