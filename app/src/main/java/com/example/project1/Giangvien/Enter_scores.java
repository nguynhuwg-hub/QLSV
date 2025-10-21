package com.example.project1.Giangvien;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;


public class Enter_scores extends AppCompatActivity {

    private Spinner spinnerHocKy, spinnerMonHoc;
    private Button buttonHienThiDanhSach, buttonQuayLai, buttonLuu, buttonChinhSua,buttonCheck_MSV;
    private LinearLayout layoutNhapDiem;
    private EditText editMaSinhVien, editDiemQT, editDiemGK, editDiemCK;
    private TextView textDiemTK, textTrangThai,textHoTen,textLop;
    CreateDatabase dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_and_edit);

        // Mở kết nối database
        dbHelper = new  CreateDatabase(this);
        database = dbHelper.open();

        // Ánh xạ View
        spinnerMonHoc = findViewById(R.id.spinnerMonHoc);
        buttonHienThiDanhSach = findViewById(R.id.buttonHienThiDanhSach);
        buttonQuayLai = findViewById(R.id.buttonQuayLai);
        buttonLuu = findViewById(R.id.buttonLuu);
        buttonChinhSua = findViewById(R.id.buttonChinhSua);
        layoutNhapDiem = findViewById(R.id.layoutNhapDiem);
        editMaSinhVien = findViewById(R.id.editMaSinhVien);
        editDiemQT = findViewById(R.id.editDiemQT);
        editDiemGK = findViewById(R.id.editDiemGK);
        editDiemCK = findViewById(R.id.editDiemCK);
        textDiemTK = findViewById(R.id.textDiemTK);
        textTrangThai = findViewById(R.id.textTrangThai);
        textHoTen = findViewById(R.id.textHoTen);
        textLop = findViewById(R.id.textLop);

        // 🟩 Nhận dữ liệu được gửi từ màn danh sách sinh viên
        String maSV = getIntent().getStringExtra("MaSV");
        String hoTen = getIntent().getStringExtra("HoTen");
        String maLop = getIntent().getStringExtra("MaLop");

        if (maSV != null) {
            editMaSinhVien.setText(maSV);
            editMaSinhVien.setEnabled(false); // Khóa không cho sửa
        }
        if (hoTen != null) {
            textHoTen.setText("Họ tên: " + hoTen);
        }
        if (maLop != null) {
            textLop.setText("Lớp: " + maLop);
        }

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
            spinnerMonHoc.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách môn học!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // Nút hiển thị/ẩn form
        buttonHienThiDanhSach.setOnClickListener(v -> layoutNhapDiem.setVisibility(View.VISIBLE));
        buttonQuayLai.setOnClickListener(v -> layoutNhapDiem.setVisibility(View.GONE));
        // 🟩 Nút Lưu điểm
        buttonLuu.setOnClickListener(v -> {
            try {
                String diemQTStr = editDiemQT.getText().toString().trim();
                String diemGKStr = editDiemGK.getText().toString().trim();
                String diemCKStr = editDiemCK.getText().toString().trim();

                if (maSV.isEmpty() || diemQTStr.isEmpty() || diemGKStr.isEmpty() || diemCKStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double diemQT = Double.parseDouble(diemQTStr);
                double diemGK = Double.parseDouble(diemGKStr);
                double diemCK = Double.parseDouble(diemCKStr);
                double diemTK = diemQT * 0.1 + diemGK * 0.3 + diemCK * 0.6;
                String trangThai = (diemTK >= 4.0) ? "Đạt" : "Không đạt";

                textDiemTK.setText("Điểm tổng kết: " + diemTK);
                textTrangThai.setText("Trạng thái: " + trangThai);

                ContentValues values = new ContentValues();
                values.put("MaSV", maSV);
                values.put("DiemQT", diemQT);
                values.put("DiemGK", diemGK);
                values.put("DiemCK", diemCK);
                values.put("DiemTK", diemTK);
                values.put("TrangThai", trangThai);

                long result = database.insert("Diem", null, values);
                if (result == -1) {
                    Toast.makeText(this, "Lưu thất bại! Mã SV có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lưu điểm thành công!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Chỉnh sửa điểm
        buttonChinhSua.setOnClickListener(v -> {
            try {
                String diemQTStr = editDiemQT.getText().toString().trim();
                String diemGKStr = editDiemGK.getText().toString().trim();
                String diemCKStr = editDiemCK.getText().toString().trim();

                if (maSV.isEmpty() || diemQTStr.isEmpty() || diemGKStr.isEmpty() || diemCKStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double diemQT = Double.parseDouble(diemQTStr);
                double diemGK = Double.parseDouble(diemGKStr);
                double diemCK = Double.parseDouble(diemCKStr);
                double diemTK = diemQT * 0.1 + diemGK * 0.3 + diemCK * 0.6;
                String trangThai = (diemTK >= 4.0) ? "Đạt" : "Không đạt";

                ContentValues values = new ContentValues();
                values.put("DiemQT", diemQT);
                values.put("MalopMH", "74DCTT23");
                values.put("DiemGK", diemGK);
                values.put("DiemCK", diemCK);
                values.put("DiemTK", diemTK);
                values.put("TrangThai", trangThai);

                int rows = database.update("Diem", values, "MaSV=?", new String[]{maSV});
                if (rows > 0) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    textDiemTK.setText("Điểm tổng kết: " + diemTK);
                    textTrangThai.setText("Trạng thái: " + trangThai);
                } else {
                    Toast.makeText(this, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập điểm hợp lệ!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}





