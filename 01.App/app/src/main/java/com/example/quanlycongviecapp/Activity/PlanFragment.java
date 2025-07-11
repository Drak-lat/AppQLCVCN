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

    private RecyclerView recyclerView;
    private PlanAdapter planAdapter;
    private List<Plan> planList = new ArrayList<>();
    private int userId = -1; // Nhận từ Activity hoặc Bundle

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan, container, false);
        recyclerView = view.findViewById(R.id.rvPlans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        planAdapter = new PlanAdapter(planList, new PlanAdapter.OnPlanClickListener() {
            @Override
            public void onPlanClick(Plan plan) {
                // Nếu muốn: show detail plan
            }

            @Override
            public void onEdit(Plan plan) {
                // Sử dụng lại AddPlan dialog để sửa
                AddPlan.show(requireActivity(), () -> loadPlans(), plan, userId);
            }

            @Override
            public void onDelete(Plan plan) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xóa kế hoạch")
                        .setMessage("Bạn chắc chắn muốn xóa kế hoạch này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            ApiService api = RetrofitClient.getClient().create(ApiService.class);
                            api.deletePlan(plan.getId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                                    loadPlans();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getContext(), "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        recyclerView.setAdapter(planAdapter);

        // Lấy userId từ bundle hoặc activity nếu cần
        userId = ((Menu) requireActivity()).getUserId();

        loadPlans();
        return view;
    }

    private void loadPlans() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getPlansByUser(userId).enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                planList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    planList.addAll(response.body());
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi load danh sách!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
