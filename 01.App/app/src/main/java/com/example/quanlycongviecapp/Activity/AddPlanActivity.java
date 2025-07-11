package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlycongviecapp.Model.Plan;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlanActivity extends AppCompatActivity {

    private EditText edtPlanName, edtPlanDesc, edtStartDate, edtEndDate;
    private Button btnSavePlan;
    private int userId; // Nhận từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_plan);

        edtPlanName = findViewById(R.id.edtPlanName);
        edtPlanDesc = findViewById(R.id.edtPlanDesc);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnSavePlan = findViewById(R.id.btnSavePlan);

        userId = getIntent().getIntExtra("userId", -1);

        btnSavePlan.setOnClickListener(v -> {
            String name = edtPlanName.getText().toString().trim();
            String desc = edtPlanDesc.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Plan plan = new Plan();
            plan.setName(name);
            plan.setDescription(desc);
            plan.setStartDate(startDate);
            plan.setEndDate(endDate);
            plan.setUserId(userId); // setUserId() cần có trong model Plan

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.createPlan(plan).enqueue(new Callback<Plan>() {
                @Override
                public void onResponse(Call<Plan> call, Response<Plan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddPlanActivity.this, "Thêm kế hoạch thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AddPlanActivity.this, "Thêm kế hoạch thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Plan> call, Throwable t) {
                    Toast.makeText(AddPlanActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
