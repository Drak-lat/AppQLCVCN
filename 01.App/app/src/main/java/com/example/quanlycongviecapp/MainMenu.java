package com.example.quanlycongviecapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {

    private TaskAdapter adapterTask;
    private PriorityAdapter adapterPriority;
    private AddAdapter adapterAdd;
    private ProfileAdapter adapterProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        // Khởi tạo RecyclerView
        RecyclerView rvTask = findViewById(R.id.rvTask);
        rvTask.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rvPriority = findViewById(R.id.rvPriority);
        rvPriority.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rvAdd = findViewById(R.id.rvAdd);
        rvAdd.setLayoutManager(new LinearLayoutManager(this));
        adapterAdd = new AddAdapter(Collections.singletonList("Add 1"));
        rvAdd.setAdapter(adapterAdd);

        RecyclerView rvProfile = findViewById(R.id.rvProfile);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));
        adapterProfile = new ProfileAdapter(Collections.singletonList("User"));
        rvProfile.setAdapter(adapterProfile);

        // --- Gọi API cho Task và Priority ---
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        int userId = 1; // Đổi lại id thực tế nếu có

        // Task
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
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                // Xử lý lỗi, ví dụ: Toast.makeText(MainMenu.this, "Lỗi load Task", Toast.LENGTH_SHORT).show();
            }
        });

        // Priority
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
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                // Xử lý lỗi, ví dụ: Toast.makeText(MainMenu.this, "Lỗi load Priority", Toast.LENGTH_SHORT).show();
            }
        });
    }
}