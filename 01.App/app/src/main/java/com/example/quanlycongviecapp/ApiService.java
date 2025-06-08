package com.example.quanlycongviecapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/Auth/Register")  // chỉnh đúng path nếu backend khác
    Call<User> register(@Body User user);
    @POST("/api/Auth/Login")
    Call<User> login(@Body User user);

}

