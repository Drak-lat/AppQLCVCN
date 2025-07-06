package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.quanlycongviecapp.Model.Account;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private ImageView imgAvatar;
    private TextView tvFullName, tvEmail, tvPhone, tvBirthday, tvAddress;
    private Button btnEditProfile;
    private int userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account, container, false);

        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvBirthday = view.findViewById(R.id.tvBirthday);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Lấy userId từ Activity cha
        if (getActivity() != null) {
            userId = getActivity().getIntent().getIntExtra("userId", -1);
        }
        Log.d("AccountFragment", "userId nhận được: " + userId);

        if (userId != -1) {
            loadUserInfo(userId);
        } else {
            Toast.makeText(requireContext(), "Không có ID người dùng", Toast.LENGTH_SHORT).show();
        }

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            // Nếu cần hiệu ứng chuyển cảnh, gọi từ Activity:
            // getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        });

        return view;
    }

    private void loadUserInfo(int id) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Log.d("AccountFragment", "API status code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Account user = response.body();
                    tvFullName.setText(user.getFullName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                    tvBirthday.setText(user.getBirthdate());
                    tvAddress.setText(user.getAddress());

                    if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                        Glide.with(AccountFragment.this)
                                .load(user.getAvatarUrl())
                                .placeholder(R.drawable.avatar_default)
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.avatar_default);
                    }
                } else {
                    Toast.makeText(requireContext(), "Không thể tải thông tin người dùng. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AccountFragment", "Retrofit error", t);
            }
        });
    }
}
