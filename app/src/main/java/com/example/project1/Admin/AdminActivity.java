package com.example.project1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project1.R;

public class AdminActivity extends AppCompatActivity {

    CardView cardSinhVien, cardGiangVien, cardLop, cardMonHoc, cardTaiKhoan;
    Button btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        cardSinhVien = findViewById(R.id.cardSinhVien);
        cardGiangVien = findViewById(R.id.cardGiangVien);
        cardLop = findViewById(R.id.cardLop);
        cardMonHoc = findViewById(R.id.cardMonHoc);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);
        btnThoat = findViewById(R.id.btnThoat);

        // Sự kiện click Thoát
        btnThoat.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, DangnhapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng AdminActivity
        });

        // Các sự kiện khác giữ nguyên
        cardSinhVien.setOnClickListener(v -> {
                Toast.makeText(this, "Quản lý sinh viên", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this, QuanlySinhvienActivity.class));
    } );

        cardGiangVien.setOnClickListener(v -> {
                Toast.makeText(this, "Quản lý giảng viên", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminActivity.this, QuanlyGiangvienActivity.class));
    }  );

        cardLop.setOnClickListener(v ->
                Toast.makeText(this, "Quản lý lớp", Toast.LENGTH_SHORT).show()

        );

        cardMonHoc.setOnClickListener(v ->{
                Toast.makeText(this, "Quản lý môn học", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this, MonHocActivity.class));
    } );

        cardTaiKhoan.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý tài khoản", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminActivity.this, Quanlytaikhoan.class));
        });
    }
}
