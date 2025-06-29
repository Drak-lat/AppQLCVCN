package com.example.quanlycongviecapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

public interface ApiService {
    @POST("/api/Auth/Register")  // chỉnh đúng path nếu backend khác
    Call<User> register(@Body User user);
    @POST("/api/Auth/Login")
    Call<User> login(@Body User user);
    @POST("/api/Tasks")
    Call<Void> addTask(@Body Task task);
    @PUT("/api/tasks/{id}")
    Call<Void> updateTask(@Path("id") int id, @Body Task task);
    @DELETE("/api/tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);
}

