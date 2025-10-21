package com.example.project1.Giangvien;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

public class View_only extends AppCompatActivity {

    private EditText editMaSV;
    private Button buttonXemDiem, buttonQuayLai;
    private TextView textHoTen, textMonHoc, textDiemQT, textDiemGK, textDiemCK, textDiemTK, textTrangThai;
    private Spinner spinnerHocKy_Xem, spinnerMonHoc_Xem;
    private SQLiteDatabase database;
    private CreateDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_scores);

        dbHelper = new  CreateDatabase(this);
        database = dbHelper.open();

        editMaSV = findViewById(R.id.editMaSV_Xem);
        buttonXemDiem = findViewById(R.id.buttonXemDiem);
        buttonQuayLai = findViewById(R.id.buttonQuayLai);
        textHoTen = findViewById(R.id.textHoTen_Xem);
        textMonHoc = findViewById(R.id.textMonHoc_Xem);
        textDiemQT = findViewById(R.id.textDiemQT_Xem);
        textDiemGK = findViewById(R.id.textDiemGK_Xem);
        textDiemCK = findViewById(R.id.textDiemCK_Xem);
        textDiemTK = findViewById(R.id.textDiemTK_Xem);
        textTrangThai = findViewById(R.id.textTrangThai_Xem);
        spinnerMonHoc_Xem = findViewById(R.id.spinnerMonHoc_Xem);

        // 🟩 Load dữ liệu Spinner môn học từ bảng MonHoc
        try {
            Cursor cursor = database.rawQuery("SELECT TenMH FROM MonHoc", null);
            ArrayList<String> monHocList = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    String tenMH = cursor.getString(cursor.getColumnIndexOrThrow("TenMH"));
                    monHocList.add(tenMH);
                } while (cursor.moveToNext());
            }

            cursor.close();

            if (monHocList.isEmpty()) {
                monHocList.add("⚠️ Chưa có môn học trong CSDL");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monHocList);
            spinnerMonHoc_Xem.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách môn học!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        buttonXemDiem.setOnClickListener(v -> {
            String maSV = editMaSV.getText().toString().trim();

            if (maSV.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = database.rawQuery(
                    "SELECT sv.HoTen, mh.TenMH, d.DiemQT, d.DiemGK, d.DiemCK, d.DiemTK, d.TrangThai " +
                            "FROM SinhVien sv " +
                            "JOIN Diem d ON sv.MaSV = d.MaSV " +
                            "JOIN LopMonHoc lm ON d.MaLopMH = lm.MaLopMH " +
                            "JOIN MonHoc mh ON lm.MaMH = mh.MaMH " +
                            "WHERE sv.MaSV = ?",
                    new String[]{maSV});

            if (cursor.moveToFirst()) {
                textHoTen.setText("Họ tên: " + cursor.getString(cursor.getColumnIndexOrThrow("HoTen")));
                textMonHoc.setText("Môn học: " + cursor.getString(cursor.getColumnIndexOrThrow("TenMH")));
                textDiemQT.setText("Điểm QT: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemQT")));
                textDiemGK.setText("Điểm GK: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemGK")));
                textDiemCK.setText("Điểm CK: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemCK")));
                textDiemTK.setText("Tổng kết: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemTK")));
                textTrangThai.setText("Trạng thái: " + cursor.getString(cursor.getColumnIndexOrThrow("TrangThai")));
            } else {
                Toast.makeText(this, "Không tìm thấy mã sinh viên!", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });

        buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình chọn vai trò hoặc main
                Intent intent = new Intent(View_only.this, MainActivity.class);
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại
            }
        });
}
}
