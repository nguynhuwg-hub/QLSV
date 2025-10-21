package com.example.project1.Giangvien;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;

import com.example.project1.R;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Màn hình chọn vai trò
        setContentView(R.layout.role);

        Button btnAdmin = findViewById(R.id.btnAdmin);
        Button btnSinhVien = findViewById(R.id.btnSinhVien);

        // Admin → layout nhập và chỉnh sửa điểm
        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityDanhSachSinhVien.class);
            startActivity(intent);
        });

        // Sinh viên → layout xem điểm
        btnSinhVien.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, View_only.class);
            intent.putExtra("role", "SinhVien");
            startActivity(intent);
        });
    }
}




