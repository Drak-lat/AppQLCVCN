package com.example.quanlycongviecapp.Activity;

import android.os.Bundle;
import android.util.Log;

import com.example.quanlycongviecapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fabAdd;

    // Khai báo biến userId để dùng toàn class
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // layout menu.xml chứa BottomNavigation, FAB và FrameLayout

        // NHẬN userId từ LoginActivity: key phải đúng với bên LoginActivity
        userId = getIntent().getIntExtra("userId", -1);
        Log.d("MenuActivity", "userId nhận được: " + userId);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);

        // Lấy dữ liệu truyền vào
        String openFragment = getIntent().getStringExtra("openFragment");

        if ("plan".equals(openFragment)) {
            bottomNavigationView.setSelectedItemId(R.id.plan);
        } else {
            // Mặc định load Home
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selected = null;

            // Chú ý: id phải trùng với menu_bottom_nav.xml
            if (id == R.id.home) {
                selected = new HomeFragment();
                fabAdd.show();
            } else if (id == R.id.plan) {
                selected = new PlanFragment();
                fabAdd.show();
            } else if (id == R.id.task) {
                selected = new TaskFragment();
                fabAdd.show();
            } else if (id == R.id.account) {
                selected = new AccountFragment();
                fabAdd.hide(); // Không cho thêm từ tab account
            }

            if (selected != null) {
                loadFragment(selected);
            }
            return true;
        });

        fabAdd.setOnClickListener(v -> {
            int selectedItem = bottomNavigationView.getSelectedItemId();
            if (selectedItem == R.id.plan) {
                // Mở dialog thêm kế hoạch
                AddPlan.show(this, () -> {
                    // Reload danh sách plan sau khi thêm
                    // Ví dụ gọi lại PlanFragment load lại data
                }, null);
            } else if (selectedItem == R.id.task) {
                // Mở dialog thêm công việc
                AddTask.show(this, () -> {
                    // Reload danh sách task sau khi thêm
                }, null);
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // Có thể tạo getter cho userId nếu các fragment cần dùng:
    public int getUserId() {
        return userId;
    }
}
