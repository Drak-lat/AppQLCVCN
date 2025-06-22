package com.example.quanlycongviecapp.Remote;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.Model.User;
import com.example.quanlycongviecapp.Model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;

import java.util.List;


public interface ApiService {
    @POST("/api/Auth/Register")  // chỉnh đúng path nếu backend khác
    Call<User> register(@Body User user);
    @POST("/api/Auth/Login")
    Call<User> login(@Body User user);
    @GET("api/Tasks/user/{userId}")
    Call<List<TaskModel>> getTasks(@Path("userId") int userId);

    @GET("api/Tasks/user/{userId}/priority")
    Call<List<TaskModel>> getTasksByPriority(@Path("userId") int userId);

    @GET("/api/Users/{id}")
    Call<UserProfile> getProfile(@Path("id") int id);

    @PUT("/api/Users/{id}")
    Call<UserProfile> updateProfile(@Path("id") int id, @Body UserProfile userProfile);



}

