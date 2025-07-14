package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.Adapter.TaskAdapter;
import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskFragment extends Fragment {

    private static final String ARG_PLAN_ID = "planId";
    private int planId;

    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private EditText edtSearchTask;

    private List<TaskModel> tasks = new ArrayList<>();     // Danh sách hiển thị
    private List<TaskModel> allTasks = new ArrayList<>();  // Danh sách gốc để search

    // Factory method để tạo fragment với planId
    public static TaskFragment newInstance(int planId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLAN_ID, planId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lấy planId từ arguments
        if (getArguments() != null) {
            planId = getArguments().getInt(ARG_PLAN_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout chứa EditText tìm kiếm + RecyclerView
        return inflater.inflate(R.layout.task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTasks = view.findViewById(R.id.rvTasks);
        edtSearchTask = view.findViewById(R.id.edtSearchTask);

        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));

        TaskAdapter.OnTaskActionListener listener =
                new TaskAdapter.OnTaskActionListener() {
                    @Override public void onEdit(TaskModel task) {
                        Log.d("TaskFragment", "onEdit called for " + task.getId());
                        Intent i = new Intent(getContext(), EditTask.class);
                        i.putExtra("taskJson", new Gson().toJson(task));
                        startActivity(i);
                    }

                    @Override
                    public void onDelete(TaskModel task) {
                        RetrofitClient.getClient()
                                .create(ApiService.class)
                                .deleteTask(task.getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getContext(),
                                                    "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            tasks.remove(task);
                                            allTasks.remove(task);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getContext(),
                                                    "Xóa thất bại: " + response.code(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(),
                                                "Lỗi kết nối: " + t.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                };

        adapter = new TaskAdapter(requireContext(), tasks, listener);
        rvTasks.setAdapter(adapter);

        // Xử lý search realtime
        edtSearchTask.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }
        });
        loadTasksByPlan();
    }
    private void loadTasksByPlan() {
        if (planId < 0) {
            Toast.makeText(getContext(), "Plan ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getTasksByPlan(planId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call,
                                   Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTasks.clear();
                    allTasks.addAll(response.body());
                    filterTasks(edtSearchTask.getText().toString()); // Lọc theo text search hiện tại
                } else {
                    Toast.makeText(getContext(),
                            "Lỗi server: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Không thể kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm filter task theo tên
    private void filterTasks(String keyword) {
        tasks.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            tasks.addAll(allTasks);
        } else {
            String kw = keyword.trim().toLowerCase();
            for (TaskModel t : allTasks) {
                if (t.getTitle() != null && t.getTitle().toLowerCase().contains(kw)) {
                    tasks.add(t);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
