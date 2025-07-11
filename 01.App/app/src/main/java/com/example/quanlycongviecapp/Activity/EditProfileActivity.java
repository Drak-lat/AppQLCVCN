package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quanlycongviecapp.Model.Account;
import com.example.quanlycongviecapp.R;
import com.example.quanlycongviecapp.Remote.ApiService;
import com.example.quanlycongviecapp.Remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private EditText edtUsername, edtPassword, edtFullName, edtEmail, edtPhone, edtBirthdate, edtAddress;
    private Button btnSaveProfile;
    private int userId;
    private Account currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        imgAvatar = findViewById(R.id.imgAvatar);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthdate = findViewById(R.id.edtBirthdate);
        edtAddress = findViewById(R.id.edtAddress);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không có userId, quay lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserInfo(userId);

        btnSaveProfile.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "Chưa có dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                return;
            }
            updateProfile();
        });
    }

    private void loadUserInfo(int id) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();

                    edtUsername.setText(currentUser.getUsername());
                    edtPassword.setText(currentUser.getPassword()); // Có thể hiển thị ****** nếu muốn
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
                        imgAvatar.setImageResource(R.drawable.avatar_default);
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Không tìm thấy thông tin user!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateProfile() {
        // Lấy lại toàn bộ thông tin (username và password lấy lại từ EditText, readonly nhưng vẫn gửi)
        currentUser.setUsername(edtUsername.getText().toString());
        currentUser.setPassword(edtPassword.getText().toString());
        currentUser.setFullName(edtFullName.getText().toString().trim());
        currentUser.setEmail(edtEmail.getText().toString().trim());
        currentUser.setPhone(edtPhone.getText().toString().trim());

        String inputBirthdate = edtBirthdate.getText().toString().trim();
        currentUser.setBirthdate(toIsoDate(inputBirthdate));

        currentUser.setAddress(edtAddress.getText().toString().trim());

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Account> call = apiService.updateProfile(userId, currentUser);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại: " + error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm convert ngày dd/MM/yyyy -> yyyy-MM-dd
    private String toIsoDate(String inputDate) {
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fromFormat.parse(inputDate);
            return toFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return inputDate;
        }
    }
}
