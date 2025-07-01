package com.example.quanlycongviecapp.Remote;

import com.example.quanlycongviecapp.Model.LoginResponse;
import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.Model.User;
import com.example.quanlycongviecapp.Model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Part;

import java.util.List;


public interface ApiService {
    @POST("/api/Auth/Register")  // chỉnh đúng path nếu backend khác
    Call<User> register(@Body User user);

    @POST("/api/Auth/Login")
    Call<LoginResponse> login(@Body User user);

    @GET("api/Tasks/user/{userId}")
    Call<List<TaskModel>> getTasks(@Path("userId") int userId);

    @GET("api/Tasks/user/{userId}/priority")
    Call<List<TaskModel>> getTasksByPriority(@Path("userId") int userId);

    @GET("/api/Users/{id}")
    Call<UserProfile> getUserById(@Path("id") int id);

    @PUT("/api/Users/{id}")
    Call<UserProfile> updateProfile(@Path("id") int id, @Body UserProfile userProfile);

    @POST("api/Tasks")
    Call<TaskModel> createTask(@Body TaskModel taskModel);

    @PUT("api/Tasks/{taskId}")
    Call<TaskModel> updateTask(@Path("taskId") int taskId, @Body TaskModel taskModel);



    @Multipart
    @POST("Users/upload-avatar/{id}")
    Call<ResponseBody> uploadAvatar(
            @Path("id") int userId,
            @Part MultipartBody.Part file
    );


}

