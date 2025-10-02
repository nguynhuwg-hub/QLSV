package com.example.project1.Giangvien;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project1.Admin.AdminActivity;
import com.example.project1.Admin.DangnhapActivity;
import com.example.project1.Admin.QuanlyGiangvienActivity;
import com.example.project1.R;

public class GiangvienActivity extends AppCompatActivity {
    Button btnThoat;
    CardView cardSinhVien, cardGiangVien, cardLop, cardMonHoc, cardTaiKhoan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giangvien_layout);
        cardSinhVien = findViewById(R.id.cardSinhVien);
        cardGiangVien = findViewById(R.id.cardGiangVien);
        cardLop = findViewById(R.id.cardLop);
        cardMonHoc = findViewById(R.id.cardMonHoc);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);
        btnThoat = findViewById(R.id.btnThoat);

        // Sự kiện click
        cardSinhVien.setOnClickListener(v -> {
            // TODO: Mở activity quản lý sinh viên
            Toast.makeText(this, "Quản lý sinh viên", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, QuanLySinhVienActivity.class));
        });

        cardGiangVien.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý giảng viên", Toast.LENGTH_SHORT).show();
        });

        cardLop.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý lớp", Toast.LENGTH_SHORT).show();
        });

        cardMonHoc.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý môn học", Toast.LENGTH_SHORT).show();
        });

        cardTaiKhoan.setOnClickListener(v -> {
            Toast.makeText(this, "Quản lý tài khoản", Toast.LENGTH_SHORT).show();
        });

        btnThoat.setOnClickListener(v -> {
            Intent intent = new Intent(GiangvienActivity.this, DangnhapActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
