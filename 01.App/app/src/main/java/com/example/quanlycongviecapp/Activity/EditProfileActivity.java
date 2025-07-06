package com.example.quanlycongviecapp.Activity;

import android.content.Intent;

import android.net.Uri;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private EditText edtFullName, edtEmail, edtPhone, edtBirthdate, edtAddress;
    private Button btnSaveProfile, btnChangeAvatar;
    private int userId;
    private Account currentUser;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private Uri selectedImageUri;

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

        btnChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgAvatar.setImageURI(selectedImageUri); // Xem trước ảnh mới
            uploadAvatarToServer();
        }
    }

    private void loadUserInfo(int id) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUserById(id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
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
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateProfile() {
        currentUser.setFullName(edtFullName.getText().toString());
        currentUser.setEmail(edtEmail.getText().toString());
        currentUser.setPhone(edtPhone.getText().toString());
        currentUser.setBirthdate(edtBirthdate.getText().toString());
        currentUser.setAddress(edtAddress.getText().toString());

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Account> call = apiService.updateProfile(userId, currentUser);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAvatarToServer() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Bạn chưa chọn ảnh mới!", Toast.LENGTH_SHORT).show();
            return;
        }

        String realPath = RealPathUtil.getRealPath(this, selectedImageUri);
        if (realPath == null) {
            Toast.makeText(this, "Không lấy được đường dẫn ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(realPath);

        MediaType mediaType = MediaType.parse(getContentResolver().getType(selectedImageUri));
        RequestBody requestFile = RequestBody.create(mediaType, file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.uploadAvatar(userId, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Đổi ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                    // (Có thể load lại avatar từ server nếu muốn)
                } else {
                    Toast.makeText(EditProfileActivity.this, "Đổi ảnh đại diện thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi upload ảnh: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
