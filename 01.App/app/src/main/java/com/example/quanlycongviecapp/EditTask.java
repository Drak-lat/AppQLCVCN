package com.example.quanlycongviecapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTask extends AppCompatActivity {

    private EditText Title, Description, DueDate;
    private Spinner Priority;
    private CheckBox Completed;
    private Button Save, Cancel;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Title = findViewById(R.id.Title);
        Description = findViewById(R.id.Description);
        DueDate = findViewById(R.id.DueDate);
        Priority = findViewById(R.id.Priority);
        Completed = findViewById(R.id.Completed);
        Save = findViewById(R.id.Save);
        Cancel = findViewById(R.id.Cancel);

        taskId = getIntent().getIntExtra("task_id", -1);
        Title.setText(getIntent().getStringExtra("title"));
        Description.setText(getIntent().getStringExtra("description"));
        DueDate.setText(getIntent().getStringExtra("dueDate"));
        int priority = getIntent().getIntExtra("priority", 1);
        Priority.setSelection(priority - 1);
        Completed.setChecked(getIntent().getBooleanExtra("completed", false));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Priority.setAdapter(adapter);

        Save.setOnClickListener(v -> updateTask());

        Cancel.setOnClickListener(v -> finish());
    }

    private void updateTask() {
        String title = Title.getText().toString().trim();
        String desc = Description.getText().toString().trim();
        String dueDate = DueDate.getText().toString().trim();
        int priority = Priority.getSelectedItemPosition() + 1;
        boolean completed = Completed.isChecked();

        if (title.isEmpty()) {
            Title.setError("Không được để trống tiêu đề!");
            Toast.makeText(this, "Bạn chưa nhập tiêu đề!", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(title, desc, dueDate, priority, completed);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.updateTask(taskId, task);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditTask.this, "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditTask.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditTask.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
