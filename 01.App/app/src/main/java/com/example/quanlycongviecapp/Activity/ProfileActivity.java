package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlycongviecapp.R;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView tvFullName, tvEmail, tvPhone, tvBirthday, tvAddress;
    private Button btnEditProfile;

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

        // Hiển thị thông tin mẫu (hoặc lấy dữ liệu từ API truyền vào)
        tvFullName.setText("Lê Thế Anh");
        tvEmail.setText("letheanh@email.com");
        tvPhone.setText("0987 654 321");
        tvBirthday.setText("29/10/2005");
        tvAddress.setText("Quận 12,TP Hồ Chí Minh, Việt Nam");
        // Glide hoặc Picasso để load ảnh nếu cần

        btnEditProfile.setOnClickListener(v -> {
            // TODO: Chuyển sang màn EditProfileActivity hoặc mở dialog chỉnh sửa
        });
    }
}
