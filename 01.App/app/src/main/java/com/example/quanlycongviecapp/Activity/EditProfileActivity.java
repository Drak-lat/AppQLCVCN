package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quanlycongviecapp.Model.UserProfile;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private EditText edtFullName, edtEmail, edtPhone, edtBirthdate, edtAddress;
    private Button btnSaveProfile, btnChangeAvatar;
    private int id;
    private UserProfile currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        imgAvatar = findViewById(R.id.imgAvatar);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthdate = findViewById(R.id.edtBirthdate);
        edtAddress = findViewById(R.id.edtAddress);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);

        id = getIntent().getIntExtra("userId", -1);
        if (id == -1) {
            Toast.makeText(this, "Không có userId, quay lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 1. Load user info trước khi cho phép sửa
        loadUserInfo(id);

        // 2. Chỉ khi load xong currentUser != null mới được update
        btnSaveProfile.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "Chưa có dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                return;
            }
            updateProfile();
        });

        // TODO: Xử lý thay đổi ảnh avatar tại đây nếu cần
        // btnChangeAvatar.setOnClickListener...
    }

    private void loadUserInfo(int id) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    // Hiển thị lên form
                    edtFullName.setText(currentUser.getFullName());
                    edtEmail.setText(currentUser.getEmail());
                    edtPhone.setText(currentUser.getPhone());
                    edtBirthdate.setText(currentUser.getBirthdate());
                    edtAddress.setText(currentUser.getAddress());

                    if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
                        Glide.with(EditProfileActivity.this)
                                .load(currentUser.getAvatarUrl())
                                .placeholder(R.drawable.avatar_default)
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageDrawable(null);
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Không tìm thấy thông tin user!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateProfile() {
        // Lấy dữ liệu từ form
        currentUser.setFullName(edtFullName.getText().toString());
        currentUser.setEmail(edtEmail.getText().toString());
        currentUser.setPhone(edtPhone.getText().toString());
        currentUser.setBirthdate(edtBirthdate.getText().toString());
        currentUser.setAddress(edtAddress.getText().toString());

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserProfile> call = apiService.updateProfile(id, currentUser);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
