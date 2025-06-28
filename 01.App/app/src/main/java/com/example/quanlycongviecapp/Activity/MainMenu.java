package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlycongviecapp.Activity.TaskActivity;

import com.example.quanlycongviecapp.Adapter.AddAdapter;
import com.example.quanlycongviecapp.ApiService;
import com.example.quanlycongviecapp.Adapter.PriorityAdapter;
import com.example.quanlycongviecapp.Adapter.ProfileAdapter;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.RetrofitClient;
import com.example.quanlycongviecapp.Adapter.TaskAdapter;
import com.example.quanlycongviecapp.Model.TaskModel;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Button;
import android.widget.ListView;

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

        //  ListView
        ListView lvTask = findViewById(R.id.lvTask);
        ListView lvPriority = findViewById(R.id.lvPriority);
        ListView lvAdd = findViewById(R.id.lvAdd);
        ListView lvProfile = findViewById(R.id.lvProfile);

        // Button
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnTask = findViewById(R.id.btnTask);
        Button btnPriority = findViewById(R.id.btnPriority);
        Button btnProfile = findViewById(R.id.btnProfile);

        // Xử lý sự kiện các button
        btnTask.setOnClickListener(v ->{
            Intent intent = new Intent(MainMenu.this, TaskActivity.class);
            startActivity(intent);

        });


        // Adapter cho Add và Profile (dùng dữ liệu tĩnh)
        adapterAdd = new AddAdapter(this, Collections.singletonList("Add 1"));
        lvAdd.setAdapter(adapterAdd);

        adapterProfile = new ProfileAdapter(this, Collections.singletonList("User"));
        lvProfile.setAdapter(adapterProfile);

        // Gọi API cho Task và Priority
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        int userId = 1; // Đổi lại id thực tế nếu có

        // Task
        apiService.getTasks(userId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskModel> tasks = response.body();
                    if (tasks.size() > 3) tasks = tasks.subList(0, 3);
                    adapterTask = new TaskAdapter(MainMenu.this, tasks);
                    lvTask.setAdapter(adapterTask);
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
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
                    adapterPriority = new PriorityAdapter(MainMenu.this, priorityTasks);
                    lvPriority.setAdapter(adapterPriority);
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                // Xử lý lỗi, ví dụ: Toast.makeText(MainMenu.this, "Lỗi load Priority", Toast.LENGTH_SHORT).show();
            }




        });
    }
}