package com.example.quanlycongviecapp.Remote;

import com.example.quanlycongviecapp.Model.LoginResponse;
import com.example.quanlycongviecapp.Model.Plan;
import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.Model.User;
import com.example.quanlycongviecapp.Model.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import java.util.List;


public interface ApiService {
    @POST("/api/Auth/Register")  // chỉnh đúng path nếu backend khác
    Call<User> register(@Body User user);

    @POST("/api/Auth/Login")
    Call<LoginResponse> login(@Body User user);

    @GET("api/Tasks/plan/{planId}")
    Call<List<TaskModel>> getTasksByPlan(@Path("planId") int planId);
    @GET("api/Tasks/user/{userId}")
    Call<List<TaskModel>> getTasks(@Path("userId") int userId);

    @GET("api/Tasks/user/{userId}/priority")
    Call<List<TaskModel>> getTasksByPriority(@Path("userId") int userId);

    @GET("/api/Users/{id}")
    Call<Account> getUserById(@Path("id") int id);

    @PUT("/api/Users/{id}")
    Call<Account> updateProfile(@Path("id") int id, @Body Account account);

    @POST("api/Tasks")
    Call<TaskModel> createTask(@Body TaskModel taskModel);

    @PUT("api/Tasks/{taskId}")
    Call<TaskModel> updateTask(@Path("taskId") int taskId, @Body TaskModel taskModel);
    @DELETE("api/Tasks/{id}")
    Call<Void> deleteTask(@Path("id") int taskId);
    @GET("api/Plans/user/{userId}")
    Call<List<Plan>> getPlansByUser(@Path("userId") int userId);

    @POST("api/Plans")
    Call<Plan> createPlan(@Body Plan plan);

    @PUT("api/Plans/{id}")
    Call<Plan> updatePlan(@Path("id") int id, @Body Plan plan);

    @DELETE("api/Plans/{id}")
    Call<Void> deletePlan(@Path("id") int id);

    @Multipart
    @POST("Users/upload-avatar/{id}")
    Call<ResponseBody> uploadAvatar(
            @Path("id") int userId,
            @Part MultipartBody.Part file
    );


}

