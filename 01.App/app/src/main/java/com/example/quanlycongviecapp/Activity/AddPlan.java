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
import androidx.fragment.app.FragmentActivity;

import com.example.quanlycongviecapp.Model.Plan;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlan extends DialogFragment {

    private EditText edtPlanName, edtPlanDesc, edtStartDate, edtEndDate;
    private Button btnSavePlan;

    private Plan planToEdit;
    private OnPlanSavedListener listener;
    private int userId = -1; // Dùng để truyền vào khi tạo Plan mới

    // Interface callback
    public interface OnPlanSavedListener {
        void onPlanSaved();
    }

    // Setter cho plan đang chỉnh sửa (nếu có)
    public void setPlanToEdit(Plan plan) {
        this.planToEdit = plan;
    }

    // Setter cho listener callback
    public void setOnPlanSavedListener(OnPlanSavedListener listener) {
        this.listener = listener;
    }

    // Setter cho userId
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Factory method để tạo DialogFragment
    public static AddPlan newInstance(Plan plan) {
        AddPlan dialog = new AddPlan();
        dialog.setPlanToEdit(plan);
        return dialog;
    }

    // Static method để show dialog
    public static void show(FragmentActivity activity, OnPlanSavedListener listener, Plan plan, int userId) {
        AddPlan dialog = newInstance(plan);
        dialog.setOnPlanSavedListener(listener);
        dialog.setUserId(userId);
        dialog.show(activity.getSupportFragmentManager(), "AddPlan");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_plan, container, false);

        edtPlanName = view.findViewById(R.id.edtPlanName);
        edtPlanDesc = view.findViewById(R.id.edtPlanDesc);
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        btnSavePlan = view.findViewById(R.id.btnSavePlan);

        // Nếu là edit
        if (planToEdit != null) {
            edtPlanName.setText(planToEdit.getName());
            edtPlanDesc.setText(planToEdit.getDescription());
            edtStartDate.setText(planToEdit.getStartDate());
            edtEndDate.setText(planToEdit.getEndDate());
        }

        btnSavePlan.setOnClickListener(v -> {
            String name = edtPlanName.getText().toString().trim();
            String desc = edtPlanDesc.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Vui lòng nhập tên kế hoạch", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(startDate)) {
                Toast.makeText(getContext(), "Vui lòng nhập ngày bắt đầu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(endDate)) {
                Toast.makeText(getContext(), "Vui lòng nhập ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            if (planToEdit == null) {
                // Thêm mới
                Plan newPlan = new Plan();
                newPlan.setName(name);
                newPlan.setDescription(desc);
                newPlan.setStartDate(startDate);
                newPlan.setEndDate(endDate);
                newPlan.setUserId(userId); // Truyền userId

                Call<Plan> call = apiService.createPlan(newPlan);
                call.enqueue(new Callback<Plan>() {
                    @Override
                    public void onResponse(Call<Plan> call, Response<Plan> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm kế hoạch thành công", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onPlanSaved();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Thêm kế hoạch thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Plan> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Sửa
                planToEdit.setName(name);
                planToEdit.setDescription(desc);
                planToEdit.setStartDate(startDate);
                planToEdit.setEndDate(endDate);

                Call<Plan> call = apiService.updatePlan(planToEdit.getId(), planToEdit);
                call.enqueue(new Callback<Plan>() {
                    @Override
                    public void onResponse(Call<Plan> call, Response<Plan> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật kế hoạch thành công", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onPlanSaved();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật kế hoạch thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Plan> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
