package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviecapp.Model.TaskModel;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTask extends AppCompatActivity {
    private EditText edtTitle, edtDesc, edtDueDate;
    private Spinner spPriority;
    private CheckBox cbCompleted;
    private Button btnSave, btnCancel;
    private int planId;

    // Định dạng SQL Server: yyyy-MM-dd
    private static final String SQL_DATE_PATTERN = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        // Ánh xạ
        edtTitle   = findViewById(R.id.Title);
        edtDesc    = findViewById(R.id.Description);
        edtDueDate = findViewById(R.id.DueDate);
        spPriority = findViewById(R.id.Priority);
        cbCompleted= findViewById(R.id.Completed);
        btnSave    = findViewById(R.id.Save);
        btnCancel  = findViewById(R.id.Cancel);

        // Lấy planId từ Intent
        planId = getIntent().getIntExtra("planId", -1);

        // Thiết lập Spinner ưu tiên
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.priorities,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        // Cho hint định dạng
        edtDueDate.setHint(SQL_DATE_PATTERN);

        // Nút Lưu và Hủy
        btnSave.setOnClickListener(v -> doAddTask());
        btnCancel.setOnClickListener(v -> finish());
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            String json = getIntent().getStringExtra("taskJson");
            TaskModel editing = new Gson().fromJson(json, TaskModel.class);
            // Prefill form:
            edtTitle.setText(editing.getTitle());
            edtDesc.setText(editing.getDescription());
            edtDueDate.setText(editing.getDueDate().split("T")[0]);
            spPriority.setSelection(editing.getPriority() - 1);
            cbCompleted.setChecked(editing.isCompleted());
            planId = editing.getPlanId();
            // Đổi nút Save thành Cập nhật
            btnSave.setText("Cập nhật");
        }

    }

    private void doAddTask() {
        String title = edtTitle.getText().toString().trim();
        String desc  = edtDesc.getText().toString().trim();
        String dueDateInput = edtDueDate.getText().toString().trim();
        int priority    = spPriority.getSelectedItemPosition() + 1;
        boolean completed = cbCompleted.isChecked();




        // Validate title
        if (title.isEmpty()) {
            edtTitle.setError("Nhập tiêu đề");
            return;
        }
        // Validate date format
        if (!isValidSqlDate(dueDateInput)) {
            edtDueDate.setError("Nhập ngày theo định dạng yyyy-MM-dd");
            Toast.makeText(this, "Ngày không hợp lệ, vui lòng nhập lại theo định dạng yyyy-MM-dd", Toast.LENGTH_LONG).show();
            return;
        }

        // Chuyển sang định dạng ISO cho backend
        String isoDate = dueDateInput + "T00:00:00.000Z";

        // Tạo TaskModel bằng setter
        TaskModel taskModel = new TaskModel();
        taskModel.setTitle(title);
        taskModel.setDescription(desc);
        taskModel.setDueDate(isoDate);
        taskModel.setPriority(priority);
        taskModel.setCompleted(completed);
        taskModel.setPlanId(planId);

        // Debug payload
        Log.d("CreateTask", "Payload: " + new Gson().toJson(taskModel));

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.createTask(taskModel).enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateTask.this, "Đã thêm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateTask.this, "Lỗi server: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                Toast.makeText(CreateTask.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidSqlDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_PATTERN, Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
