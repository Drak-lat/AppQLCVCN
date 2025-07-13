package com.example.quanlycongviecapp.Activity;


import android.content.Intent;
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
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTask extends AppCompatActivity {
    private EditText edtTitle, edtDesc, edtDueDate;
    private Spinner spPriority;
    private CheckBox cbCompleted;
    private Button btnUpdate, btnCancel;
    private TaskModel editingTask;
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
        btnUpdate  = findViewById(R.id.Save);
        btnCancel  = findViewById(R.id.Cancel);

        // Chuẩn bị spinner priorities
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.priorities,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        // Đọc JSON từ Intent
        String json = getIntent().getStringExtra("taskJson");
        editingTask = new Gson().fromJson(json, TaskModel.class);

        // Prefill form
        edtTitle.setText(editingTask.getTitle());
        edtDesc.setText(editingTask.getDescription());
        edtDueDate.setText(editingTask.getDueDate().split("T")[0]);
        spPriority.setSelection(editingTask.getPriority() - 1);
        cbCompleted.setChecked(editingTask.isCompleted());
        btnUpdate.setText("Cập nhật");

        btnUpdate.setOnClickListener(v -> doUpdateTask());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void doUpdateTask() {
        String title = edtTitle.getText().toString().trim();
        String desc  = edtDesc.getText().toString().trim();
        String date  = edtDueDate.getText().toString().trim();
        int priority = spPriority.getSelectedItemPosition() + 1;
        boolean done = cbCompleted.isChecked();

        if (title.isEmpty()) {
            edtTitle.setError("Nhập tiêu đề");
            return;
        }
        if (!isValidSqlDate(date)) {
            edtDueDate.setError("Định dạng yyyy-MM-dd");
            return;
        }

        // Cập nhật model
        editingTask.setTitle(title);
        editingTask.setDescription(desc);
        editingTask.setDueDate(date + "T00:00:00.000Z");
        editingTask.setPriority(priority);
        editingTask.setCompleted(done);

        // Gọi API PUT
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.updateTask(editingTask.getId(), editingTask)
                .enqueue(new Callback<TaskModel>() {
                    @Override
                    public void onResponse(Call<TaskModel> c, Response<TaskModel> r) {
                        if (r.isSuccessful()) {
                            Toast.makeText(EditTask.this,
                                    "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditTask.this,
                                    "Lỗi server: " + r.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<TaskModel> c, Throwable t) {
                        Toast.makeText(EditTask.this,
                                "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isValidSqlDate(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_PATTERN, Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

