package com.example.quanlycongviecapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    private int currentPlanId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // layout menu.xml chứa BottomNavigation, FAB và FrameLayout

        // NHẬN userId từ LoginActivity: key phải đúng với bên LoginActivity
        userId = getIntent().getIntExtra("userId", -1);
        Log.d("MenuActivity", "userId nhận được: " + userId);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);

        // Lấy dữ liệu truyền vào để chọn fragment mặc định
        String openFragment = getIntent().getStringExtra("openFragment");
        if ("plan".equals(openFragment)) {
            bottomNavigationView.setSelectedItemId(R.id.plan);
        } else {
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selected = null;

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
                // Mở Activity thêm kế hoạch
                Intent intent = new Intent(Menu.this, AddPlanActivity.class);
                intent.putExtra("userId", userId); // truyền userId nếu cần
                startActivity(intent);
            } else if (selectedItem == R.id.task) {
                if (currentPlanId < 0) {
                    Toast.makeText(this, "Chưa chọn kế hoạch để thêm công việc", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Menu.this, CreateTask.class);
                intent.putExtra("planId", currentPlanId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // Getter cho userId
    public int getUserId() {
        return userId;
    }

    public void openTaskTab(int planId) {
        currentPlanId = planId;
        bottomNavigationView.setSelectedItemId(R.id.task);
        TaskFragment frag = TaskFragment.newInstance(planId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, frag)
                .addToBackStack(null)
                .commit();
    }
}
