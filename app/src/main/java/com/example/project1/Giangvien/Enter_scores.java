package com.example.project1.Giangvien;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

public class Enter_scores extends AppCompatActivity {

    private Spinner spinnerMonHoc;
    private Button buttonHienThiDanhSach, buttonQuayLai, buttonLuu, buttonChinhSua;
    private LinearLayout layoutNhapDiem;
    private EditText editMaSinhVien, editDiemQT, editDiemGK, editDiemCK;
    private TextView textDiemTK, textTrangThai, textHoTen, textLop;
    CreateDatabase dbHelper;
    private SQLiteDatabase database;

    // Danh sách song song: index tương ứng giữa maLopMHList và tenMonHocList
    private ArrayList<String> maLopMHList = new ArrayList<>();
    private ArrayList<String> tenMonHocList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.project1.R.layout.enter_and_edit);

        // Mở kết nối database
        dbHelper = new CreateDatabase(this);
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

        // Nhận dữ liệu được gửi từ màn danh sách sinh viên
        final String maSV = getIntent().getStringExtra("MaSV");
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

        // Load dữ liệu Spinner môn học từ bảng LopMonHoc JOIN MonHoc
        loadMonHocSpinner();

        // Khi chọn môn học, load điểm nếu đã có
        spinnerMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position >= maLopMHList.size()) return;
                String maLopMH = maLopMHList.get(position);
                loadDiemIfExists(maSV, maLopMH);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Nút hiển thị/ẩn form
        buttonHienThiDanhSach.setOnClickListener(v -> layoutNhapDiem.setVisibility(View.VISIBLE));
        buttonQuayLai.setOnClickListener(v -> layoutNhapDiem.setVisibility(View.GONE));

        // Nút Lưu điểm
        buttonLuu.setOnClickListener(v -> {
            try {
                String diemQTStr = editDiemQT.getText().toString().trim();
                String diemGKStr = editDiemGK.getText().toString().trim();
                String diemCKStr = editDiemCK.getText().toString().trim();

                if (maSV == null || maSV.isEmpty()) {
                    Toast.makeText(this, "Không có mã sinh viên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diemQTStr.isEmpty() || diemGKStr.isEmpty() || diemCKStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int pos = spinnerMonHoc.getSelectedItemPosition();
                if (pos < 0 || pos >= maLopMHList.size()) {
                    Toast.makeText(this, "Vui lòng chọn môn học!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String maLopMH = maLopMHList.get(pos);

                double diemQT = Double.parseDouble(diemQTStr);
                double diemGK = Double.parseDouble(diemGKStr);
                double diemCK = Double.parseDouble(diemCKStr);
                double diemTK = diemQT * 0.1 + diemGK * 0.3 + diemCK * 0.6;
                String trangThai = (diemTK >= 4.0) ? "Đạt" : "Không đạt";

                textDiemTK.setText("Điểm tổng kết: " + diemTK);
                textTrangThai.setText("Trạng thái: " + trangThai);

                ContentValues values = new ContentValues();
                values.put("MaSV", maSV);
                values.put("MaLopMH", maLopMH); // quan trọng: lưu MaLopMH
                values.put("DiemQT", diemQT);
                values.put("DiemGK", diemGK);
                values.put("DiemCK", diemCK);
                values.put("DiemTK", diemTK);
                values.put("TrangThai", trangThai);

                long result = database.insert("Diem", null, values);
                if (result == -1) {
                    // Có thể đã tồn tại -> thông báo rõ hơn
                    Toast.makeText(this, "Lưu thất bại! Có thể điểm của sinh viên cho môn này đã tồn tại.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lưu điểm thành công!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu điểm!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // Nút Chỉnh sửa điểm
        buttonChinhSua.setOnClickListener(v -> {
            try {
                String diemQTStr = editDiemQT.getText().toString().trim();
                String diemGKStr = editDiemGK.getText().toString().trim();
                String diemCKStr = editDiemCK.getText().toString().trim();

                if (maSV == null || maSV.isEmpty()) {
                    Toast.makeText(this, "Không có mã sinh viên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (diemQTStr.isEmpty() || diemGKStr.isEmpty() || diemCKStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int pos = spinnerMonHoc.getSelectedItemPosition();
                if (pos < 0 || pos >= maLopMHList.size()) {
                    Toast.makeText(this, "Vui lòng chọn môn học!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String maLopMH = maLopMHList.get(pos);

                double diemQT = Double.parseDouble(diemQTStr);
                double diemGK = Double.parseDouble(diemGKStr);
                double diemCK = Double.parseDouble(diemCKStr);
                double diemTK = diemQT * 0.1 + diemGK * 0.3 + diemCK * 0.6;
                String trangThai = (diemTK >= 4.0) ? "Đạt" : "Không đạt";

                ContentValues values = new ContentValues();
                values.put("DiemQT", diemQT);
                values.put("DiemGK", diemGK);
                values.put("DiemCK", diemCK);
                values.put("DiemTK", diemTK);
                values.put("TrangThai", trangThai);

                int rows = database.update("Diem", values, "MaSV=? AND MaLopMH=?", new String[]{maSV, maLopMH});
                if (rows > 0) {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    textDiemTK.setText("Điểm tổng kết: " + diemTK);
                    textTrangThai.setText("Trạng thái: " + trangThai);
                } else {
                    Toast.makeText(this, "Không tìm thấy sinh viên hoặc điểm cho môn này!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập điểm hợp lệ!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void loadMonHocSpinner() {
        maLopMHList.clear();
        tenMonHocList.clear();

        Cursor cursor = null;
        try {
            // Lấy MaLopMH và TenMH (join LopMonHoc -> MonHoc)
            String sql = "SELECT LMH.MaLopMH, MH.TenMH " +
                    "FROM LopMonHoc LMH JOIN MonHoc MH ON LMH.MaMH = MH.MaMH";
            cursor = database.rawQuery(sql, null);

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
            spinnerMonHoc.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải danh sách môn học!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    private void loadDiemIfExists(String maSV, String maLopMH) {
        Cursor c = null;
        try {
            c = database.rawQuery("SELECT DiemQT, DiemGK, DiemCK, DiemTK, TrangThai FROM Diem WHERE MaSV=? AND MaLopMH=?",
                    new String[]{maSV, maLopMH});
            if (c.moveToFirst()) {
                editDiemQT.setText(String.valueOf(c.getDouble(0)));
                editDiemGK.setText(String.valueOf(c.getDouble(1)));
                editDiemCK.setText(String.valueOf(c.getDouble(2)));
                textDiemTK.setText("Điểm tổng kết: " + c.getDouble(3));
                textTrangThai.setText("Trạng thái: " + c.getString(4));
            } else {
                editDiemQT.setText("");
                editDiemGK.setText("");
                editDiemCK.setText("");
                textDiemTK.setText("");
                textTrangThai.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) c.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) database.close();
    }
}












