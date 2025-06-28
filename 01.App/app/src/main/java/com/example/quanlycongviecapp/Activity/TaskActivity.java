package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviecapp.Adapter.TaskAdapter;
import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private ListView lvTask;
    private TaskAdapter taskAdapter;
    private List<TaskModel> taskList;
    private Button btnAdd, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initViews();
        setupListView();
        setupEvents();
    }

    private void initViews() {
        lvTask = findViewById(R.id.lvTask);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupListView() {
        taskList = getMockData();
        taskAdapter = new TaskAdapter(this, taskList);
        lvTask.setAdapter(taskAdapter);
    }

    private void setupEvents() {
        btnAdd.setOnClickListener(v -> addNewTask());
        btnDelete.setOnClickListener(v -> deleteLastTask());
    }

    private void addNewTask() {
        TaskModel newTask = new TaskModel();
        newTask.id = taskList.size() + 1;
        newTask.title = "Việc mới " + newTask.id;
        newTask.description = "Chưa có mô tả";
        newTask.dueDate = "Chưa có hạn";
        newTask.priority = 0;
        taskList.add(newTask);
        taskAdapter.updateItems(taskList); // Dùng hàm updateItems từ adapter
    }

    private void deleteLastTask() {
        if (taskList.isEmpty()) return;
        taskList.remove(taskList.size() - 1);
        taskAdapter.updateItems(taskList); // Cập nhật danh sách
    }

    private List<TaskModel> getMockData() {
        List<TaskModel> list = new ArrayList<>();
        TaskModel task1 = new TaskModel();
        task1.id = 1;
        task1.title = "Học Android";
        task1.description = "Học RecyclerView";
        task1.dueDate = "28/06/2025";
        task1.priority = 1;
        list.add(task1);

        TaskModel task2 = new TaskModel();
        task2.id = 2;
        task2.title = "Giao bài tập nhóm";
        task2.description = "Phân chia công việc";
        task2.dueDate = "30/06/2025";
        task2.priority = 2;
        list.add(task2);

        return list;
    }
}