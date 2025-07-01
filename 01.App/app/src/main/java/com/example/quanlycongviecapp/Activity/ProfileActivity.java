package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView tvFullName, tvEmail, tvPhone, tvBirthday, tvAddress;
    private Button btnEditProfile;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        imgAvatar = findViewById(R.id.imgAvatar);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvAddress = findViewById(R.id.tvAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Log userId để kiểm tra đúng chưa
        id = getIntent().getIntExtra("userId", -1);
        Log.d("ProfileActivity", "userId nhận được: " + id); // kiểm tra logcat
        if (id != -1){
            loadUserInfo(id);
        } else {
            Toast.makeText(this, "Không có ID người dùng", Toast.LENGTH_SHORT).show();
        }

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("userId", id);
            startActivity(intent);
        });
    }

    private void loadUserInfo(int id) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Log.d("ProfileActivity", "API status code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body();
                    tvFullName.setText(user.getFullName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                    tvBirthday.setText(user.getBirthdate());
                    tvAddress.setText(user.getAddress());

                    // Load avatar nếu có, nếu chưa thì để trống hoặc dùng ảnh mặc định
                    if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(user.getAvatarUrl())
                                .placeholder(R.drawable.avatar_default)
                                .into(imgAvatar);
                    } else {
                        imgAvatar.setImageDrawable(null);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải thông tin người dùng. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("ProfileActivity", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {}
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Retrofit error", t);
            }
        });
    }
}
