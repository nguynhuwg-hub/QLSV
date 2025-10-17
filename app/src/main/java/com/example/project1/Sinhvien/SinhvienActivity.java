package com.example.project1.Sinhvien;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

public class SinhvienActivity extends AppCompatActivity {

    TextView tvWelcome, tvThongTin;
    ListView lvDiem;
    CreateDatabase dbHelper;
    SQLiteDatabase database;
    String maSV, tenSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinhvien_layout);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvThongTin = findViewById(R.id.tvThongTin);
        lvDiem = findViewById(R.id.lvDiem);

        dbHelper = new CreateDatabase(this);
        database = dbHelper.getReadableDatabase();

        // Nhận thông tin từ màn đăng nhập
        Intent intent = getIntent();
        maSV = intent.getStringExtra("MaSV");
        tenSV = intent.getStringExtra("TenSV");

        tvWelcome.setText("Xin chào, " + tenSV + " (" + maSV + ")");

        hienThiDiemSinhVien(maSV);
    }

    private void hienThiDiemSinhVien(String maSV) {
        ArrayList<String> danhSachDiem = new ArrayList<>();

        /*
         * Bảng liên quan:
         * Diem.MaSV → SinhVien.MaSV
         * Diem.MaLopMH → LopMonHoc.MaLopMH
         * LopMonHoc.MaMH → MonHoc.MaMH
         */

        String query = "SELECT MonHoc.TenMH, Diem.DiemTP, Diem.DiemThi, Diem.DiemTongKet " +
                "FROM Diem " +
                "INNER JOIN LopMonHoc ON Diem.MaLopMH = LopMonHoc.MaLopMH " +
                "INNER JOIN MonHoc ON LopMonHoc.MaMH = MonHoc.MaMH " +
                "WHERE Diem.MaSV = ?";

        Cursor cursor = database.rawQuery(query, new String[]{maSV});

        if (cursor.moveToFirst()) {
            do {
                String tenMH = cursor.getString(0);
                float diemTP = cursor.getFloat(1);
                float diemThi = cursor.getFloat(2);
                float diemTongKet = cursor.getFloat(3);

                danhSachDiem.add(
                        tenMH + "\n - Điểm thành phần: " + diemTP +
                                "\n - Điểm thi: " + diemThi +
                                "\n - Tổng kết: " + diemTongKet
                );
            } while (cursor.moveToNext());
        } else {
            danhSachDiem.add("Bạn chưa có điểm môn học nào.");
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                danhSachDiem
        );
        lvDiem.setAdapter(adapter);
    }
}