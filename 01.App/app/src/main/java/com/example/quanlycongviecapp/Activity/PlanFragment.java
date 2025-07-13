package com.example.quanlycongviecapp.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlycongviecapp.Adapter.PlanAdapter;
import com.example.quanlycongviecapp.Activity.TaskFragment;
import com.example.quanlycongviecapp.Activity.AddPlan;
import com.example.quanlycongviecapp.Model.Plan;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanFragment extends Fragment {
    private RecyclerView rvPlans;
    private PlanAdapter planAdapter;
    private List<Plan> planList = new ArrayList<>();
    private int userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan, container, false);
        rvPlans = view.findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy userId từ Activity chứa Fragment
        if (getActivity() instanceof Menu) {
            userId = ((Menu) getActivity()).getUserId();
        }

        // Khởi tạo adapter với callback
        planAdapter = new PlanAdapter(planList, new PlanAdapter.OnPlanClickListener() {
            @Override
            public void onPlanClick(Plan plan) {
                // Chuyển sang tab Công việc với planId
                ((Menu) requireActivity()).openTaskTab(plan.getId());
            }

            @Override
            public void onEdit(Plan plan) {
                // Sử dụng lại AddPlan dialog để sửa
                AddPlan.show(requireActivity(), () -> loadPlans(), plan, userId);
            }

            @Override
            public void onDelete(Plan plan) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xóa kế hoạch")
                        .setMessage("Bạn chắc chắn muốn xóa kế hoạch này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            ApiService api = RetrofitClient.getClient().create(ApiService.class);
                            api.deletePlan(plan.getId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(requireContext(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                                    loadPlans();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(requireContext(), "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        rvPlans.setAdapter(planAdapter);

        // Tải danh sách kế hoạch
        loadPlans();
        return view;
    }

    private void loadPlans() {
        if (userId < 0) {
            Toast.makeText(requireContext(), "User chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getPlansByUser(userId).enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                planList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    planList.addAll(response.body());
                } else {
                    Toast.makeText(requireContext(), "Lấy danh sách kế hoạch thất bại", Toast.LENGTH_SHORT).show();
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
