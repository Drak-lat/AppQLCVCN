package com.example.quanlycongviecapp.Activity;

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

    RecyclerView rvPlans;
    PlanAdapter adapter;
    List<Plan> planList = new ArrayList<>();

    // Giả sử userId được truyền từ Activity hoặc lấy từ SharedPreferences
    private int userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan, container, false);

        rvPlans = view.findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlanAdapter(planList, plan -> {
            Toast.makeText(getContext(), "Click kế hoạch: " + plan.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Chuyển sang TaskFragment hoặc Activity chi tiết plan
        });
        rvPlans.setAdapter(adapter);

        // Lấy userId từ activity cha (nếu MainActivity chứa fragment)
        if (getActivity() instanceof Menu) {
            userId = ((Menu) getActivity()).getUserId();
        }

        loadPlans();

        return view;
    }

    private void loadPlans() {
        if (userId == -1) {
            Toast.makeText(getContext(), "User chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Plan>> call = apiService.getPlansByUser(userId);
        call.enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    planList.clear();
                    planList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lấy danh sách kế hoạch thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
