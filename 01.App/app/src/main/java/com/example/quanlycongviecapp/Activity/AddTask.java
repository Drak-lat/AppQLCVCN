package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTask extends DialogFragment {

    private EditText edtTaskTitle, edtTaskDesc, edtDueDate;
    private Button btnSaveTask;

    private TaskModel taskToEdit;
    private OnTaskSavedListener listener;

    public interface OnTaskSavedListener {
        void onTaskSaved();
    }

    public void setTaskToEdit(TaskModel task) {
        this.taskToEdit = task;
    }

    public void setOnTaskSavedListener(OnTaskSavedListener listener) {
        this.listener = listener;
    }

    public static AddTask newInstance(TaskModel task) {
        AddTask dialog = new AddTask();
        dialog.setTaskToEdit(task);
        return dialog;
    }

    public static void show(androidx.fragment.app.FragmentActivity activity, OnTaskSavedListener listener, TaskModel task) {
        AddTask dialog = newInstance(task);
        dialog.setOnTaskSavedListener(listener);
        dialog.show(activity.getSupportFragmentManager(), "AddTask");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_task, container, false);

        edtTaskTitle = view.findViewById(R.id.edtTaskTitle);
        edtTaskDesc = view.findViewById(R.id.edtTaskDesc);
        edtDueDate = view.findViewById(R.id.edtDueDate);
        btnSaveTask = view.findViewById(R.id.btnSaveTask);

        if (taskToEdit != null) {
            edtTaskTitle.setText(taskToEdit.getTitle());
            edtTaskDesc.setText(taskToEdit.getDescription());
            edtDueDate.setText(taskToEdit.getDueDate());
        }

        btnSaveTask.setOnClickListener(v -> {
            String title = edtTaskTitle.getText().toString().trim();
            String desc = edtTaskDesc.getText().toString().trim();
            String dueDate = edtDueDate.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), "Vui lòng nhập tiêu đề công việc", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(dueDate)) {
                Toast.makeText(getContext(), "Vui lòng nhập ngày hết hạn", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            if (taskToEdit == null) {
                TaskModel newTask = new TaskModel();
                newTask.setTitle(title);
                newTask.setDescription(desc);
                newTask.setDueDate(dueDate);
                // TODO: set PlanId hoặc UserId nếu cần

                Call<TaskModel> call = apiService.createTask(newTask);
                call.enqueue(new Callback<TaskModel>() {
                    @Override
                    public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm công việc thành công", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onTaskSaved();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Thêm công việc thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                taskToEdit.setTitle(title);
                taskToEdit.setDescription(desc);
                taskToEdit.setDueDate(dueDate);

                Call<TaskModel> call = apiService.updateTask(taskToEdit.getId(), taskToEdit);
                call.enqueue(new Callback<TaskModel>() {
                    @Override
                    public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật công việc thành công", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onTaskSaved();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật công việc thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
