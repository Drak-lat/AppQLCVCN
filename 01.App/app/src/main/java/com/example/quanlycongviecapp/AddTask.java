package com.example.quanlycongviecapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTask extends AppCompatActivity {

    private EditText Title, Description, DueDate;
    private Spinner Priority;
    private CheckBox Completed;
    private Button Save, Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Title = findViewById(R.id.Title);
        Description = findViewById(R.id.Description);
        DueDate = findViewById(R.id.DueDate);
        Priority = findViewById(R.id.Priority);
        Completed = findViewById(R.id.Completed);
        Save = findViewById(R.id.Save);
        Cancel = findViewById(R.id.Cancel);

        // Cài đặt Spinner (mức độ ưu tiên)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Priority.setAdapter(adapter);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addTask() {
        String title = Title.getText().toString().trim();
        String desc = Description.getText().toString().trim();
        String dueDate = DueDate.getText().toString().trim();
        int priority = Priority.getSelectedItemPosition() + 1; // 1: Cao, 2: Trung bình, 3: Thấp
        boolean completed = Completed.isChecked();

        // Kiểm tra dữ liệu nhập
        if (title.isEmpty()) {
            Title.setError("Không được để trống tiêu đề!");
            return;
        }

        Task task = new Task(title, desc, dueDate, priority, completed);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.addTask(task);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddTask.this, "Đã lưu công việc!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddTask.this, "Lỗi lưu: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddTask.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
