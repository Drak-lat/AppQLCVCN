package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.Adapter.AddAdapter;
import com.example.quanlycongviecapp.Adapter.PriorityAdapter;
import com.example.quanlycongviecapp.Adapter.TaskAdapter;
import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {

    private TaskAdapter adapterTask;
    private PriorityAdapter adapterPriority;
    private AddAdapter adapterAdd;
    // KHÔNG cần adapterProfile nữa nếu chỉ dùng nút Profile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Khởi tạo RecyclerView cho Task, Priority, Add (nếu có dùng)
        RecyclerView rvTask = findViewById(R.id.rvTask);
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView rvPriority = findViewById(R.id.rvPriority);
        rvPriority.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView rvAdd = findViewById(R.id.rvAdd);
        rvAdd.setLayoutManager(new LinearLayoutManager(this));
        adapterAdd = new AddAdapter(Collections.singletonList("Add 1"));
        rvAdd.setAdapter(adapterAdd);

        // --- Gọi API cho Task và Priority ---
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        int userId = 1; // Thay đổi ID thực tế nếu cần

        // Lấy danh sách Task
        apiService.getTasks(userId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskModel> tasks = response.body();
                    if (tasks.size() > 3) tasks = tasks.subList(0, 3);
                    adapterTask = new TaskAdapter(tasks);
                    rvTask.setAdapter(adapterTask);
                }
            }
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) { }
        });

        // Lấy danh sách Priority Task
        apiService.getTasksByPriority(userId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskModel> priorityTasks = response.body();
                    if (priorityTasks.size() > 3) priorityTasks = priorityTasks.subList(0, 3);
                    adapterPriority = new PriorityAdapter(priorityTasks);
                    rvPriority.setAdapter(adapterPriority);
                }
            }
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) { }
        });

        // ====== XỬ LÝ SỰ KIỆN CHUYỂN SANG TRANG PROFILE ======

        // 1. Ánh xạ ID nút Profile
        View btnProfile = findViewById(R.id.btnProfile);

        // 2. Gán sự kiện click chuyển sang ProfileActivity
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Profile
                Intent intent = new Intent(MainMenu.this, ProfileActivity.class);
                // Nếu muốn truyền userId sang Profile, dùng intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
