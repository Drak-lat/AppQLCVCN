package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {

    private TaskAdapter adapterTask;
    private PriorityAdapter adapterPriority;
    private AddAdapter adapterAdd;

    private Spinner spinnerStatus, spinnerPriority;
    private List<TaskModel> fullTaskList = new ArrayList<>();

    private ApiService apiService;
    private int userId = 1; // ID người dùng cố định (bạn có thể truyền từ Login nếu muốn)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Khởi tạo RecyclerView
        RecyclerView rvTask = findViewById(R.id.rvTask);
        rvTask.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rvPriority = findViewById(R.id.rvPriority);
        rvPriority.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rvAdd = findViewById(R.id.rvAdd);
        rvAdd.setLayoutManager(new LinearLayoutManager(this));

        // Adapter cho nút Add
        adapterAdd = new AddAdapter(Collections.singletonList("Add 1"));
        rvAdd.setAdapter(adapterAdd);

        // Khởi tạo Spinner
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerPriority = findViewById(R.id.spinnerPriority);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Tất cả", "Chưa làm", "Đang làm", "Hoàn thành"));
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Tất cả", "Cao", "Trung bình", "Thấp"));
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        spinnerStatus.setOnItemSelectedListener(filterListener);
        spinnerPriority.setOnItemSelectedListener(filterListener);

        // ====== GỌI API ======
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Lấy toàn bộ Task (dùng để lọc)
        apiService.getTasks(userId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullTaskList = response.body();
                    filterAndDisplayTasks();
                }
            }
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) { }
        });

        // Lấy danh sách ưu tiên
        apiService.getTasksByPriority(userId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskModel> priorityTasks = response.body();
                    if (priorityTasks.size() > 3)
                        priorityTasks = priorityTasks.subList(0, 3);
                    adapterPriority = new PriorityAdapter(priorityTasks);
                    rvPriority.setAdapter(adapterPriority);
                }
            }
            @Override public void onFailure(Call<List<TaskModel>> call, Throwable t) { }
        });

        // ====== NÚT PROFILE ======
        View btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ProfileActivity.class);
                // intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    // ====== BỘ LỌC SPINNER ======
    private final AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filterAndDisplayTasks();
        }

        @Override public void onNothingSelected(AdapterView<?> parent) { }
    };

    private void filterAndDisplayTasks() {
        String selectedStatus = spinnerStatus.getSelectedItem().toString();
        String selectedPriority = spinnerPriority.getSelectedItem().toString();

        List<TaskModel> filteredList = new ArrayList<>();

        for (TaskModel task : fullTaskList) {
            boolean matchStatus = selectedStatus.equals("Tất cả")
                    || (task.status != null && task.status.equalsIgnoreCase(selectedStatus));

            int priorityValue = 0;
            switch (selectedPriority) {
                case "Cao": priorityValue = 1; break;
                case "Trung bình": priorityValue = 2; break;
                case "Thấp": priorityValue = 3; break;
            }

            boolean matchPriority = selectedPriority.equals("Tất cả") || task.priority == priorityValue;

            if (matchStatus && matchPriority) {
                filteredList.add(task);
            }
        }

        // Chỉ lấy tối đa 3 task để hiển thị
        if (filteredList.size() > 3)
            filteredList = filteredList.subList(0, 3);

        adapterTask = new TaskAdapter(filteredList);
        RecyclerView rvTask = findViewById(R.id.rvTask);
        rvTask.setAdapter(adapterTask);
    }
}
