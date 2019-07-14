package com.xxm.minidouyin.api;

import com.xxm.minidouyin.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface IMyUserService {


    String BASE_URL = "http://47.102.132.131/api/";

    @POST("authenticate")
    Call<User.LoginResponse> authenticate(
            @Body User user
    );

    @POST("register")
    Call<User> register(
            @Body String username,
            @Body String password
    );
}
