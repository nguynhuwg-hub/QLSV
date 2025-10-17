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

        // Sự kiện click

    }
}
