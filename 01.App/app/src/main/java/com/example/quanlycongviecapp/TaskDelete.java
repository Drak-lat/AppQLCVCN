package com.example.quanlycongviecapp;

import retrofit2.Call;
import retrofit2.Callback;

public class TaskDelete {
    private ApiService apiService;

    public TaskDelete() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    // Hàm xóa task
    public void deleteTask(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteTask(id);
        call.enqueue(callback);
    }

}
