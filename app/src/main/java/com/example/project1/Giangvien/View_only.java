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
    private Spinner spinnerMonHoc_Xem;
    private SQLiteDatabase database;
    private CreateDatabase dbHelper;

    private ArrayList<String> maLopMHList = new ArrayList<>();
    private ArrayList<String> tenMonHocList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.project1.R.layout.view_scores);

        dbHelper = new CreateDatabase(this);
        database = dbHelper.open();

        // Ánh xạ view
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

        // 🟩 Load danh sách môn học từ LopMonHoc JOIN MonHoc
        loadMonHocSpinner();

        // 🟦 Khi bấm nút “Xem điểm”
        buttonXemDiem.setOnClickListener(v -> {
            String maSV = editMaSV.getText().toString().trim();

            if (maSV.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }

            int pos = spinnerMonHoc_Xem.getSelectedItemPosition();
            if (pos < 0 || pos >= maLopMHList.size()) {
                Toast.makeText(this, "Vui lòng chọn môn học!", Toast.LENGTH_SHORT).show();
                return;
            }

            String maLopMH = maLopMHList.get(pos);

            Cursor cursor = database.rawQuery(
                    "SELECT sv.HoTen, mh.TenMH, d.DiemQT, d.DiemGK, d.DiemCK, d.DiemTK, d.TrangThai " +
                            "FROM SinhVien sv " +
                            "JOIN Diem d ON sv.MaSV = d.MaSV " +
                            "JOIN LopMonHoc lm ON d.MaLopMH = lm.MaLopMH " +
                            "JOIN MonHoc mh ON lm.MaMH = mh.MaMH " +
                            "WHERE sv.MaSV = ? AND d.MaLopMH = ?",
                    new String[]{maSV, maLopMH});

            if (cursor.moveToFirst()) {
                textHoTen.setText("Họ tên: " + cursor.getString(cursor.getColumnIndexOrThrow("HoTen")));
                textMonHoc.setText("Môn học: " + cursor.getString(cursor.getColumnIndexOrThrow("TenMH")));
                textDiemQT.setText("Điểm QT: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemQT")));
                textDiemGK.setText("Điểm GK: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemGK")));
                textDiemCK.setText("Điểm CK: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemCK")));
                textDiemTK.setText("Tổng kết: " + cursor.getDouble(cursor.getColumnIndexOrThrow("DiemTK")));
                textTrangThai.setText("Trạng thái: " + cursor.getString(cursor.getColumnIndexOrThrow("TrangThai")));
            } else {
                Toast.makeText(this, "Không tìm thấy điểm cho sinh viên này ở môn học đã chọn!", Toast.LENGTH_SHORT).show();
                textHoTen.setText("");
                textMonHoc.setText("");
                textDiemQT.setText("");
                textDiemGK.setText("");
                textDiemCK.setText("");
                textDiemTK.setText("");
                textTrangThai.setText("");
            }

            cursor.close();
        });

        // 🟦 Quay lại
        buttonQuayLai.setOnClickListener(v -> {
            Intent intent = new Intent(View_only.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadMonHocSpinner() {
        maLopMHList.clear();
        tenMonHocList.clear();

        Cursor cursor = null;
        try {
            // Lấy danh sách môn học từ LopMonHoc JOIN MonHoc
            cursor = database.rawQuery(
                    "SELECT LMH.MaLopMH, MH.TenMH " +
                            "FROM LopMonHoc LMH JOIN MonHoc MH ON LMH.MaMH = MH.MaMH",
                    null);

            if (cursor.moveToFirst()) {
                do {
                    String maLopMH = cursor.getString(0);
                    String tenMH = cursor.getString(1);
                    maLopMHList.add(maLopMH);
                    tenMonHocList.add(tenMH);
                } while (cursor.moveToNext());
            }

            if (tenMonHocList.isEmpty()) {
                tenMonHocList.add("⚠️ Chưa có môn học trong CSDL");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tenMonHocList);
            spinnerMonHoc_Xem.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách môn học!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}


