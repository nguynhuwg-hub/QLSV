package com.example.project1.Admin;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuanlySinhvienActivity extends AppCompatActivity {

    EditText edtMaSV, edtHoTenSV, edtNgaySinh, edtDiaChi;
    RadioGroup rgGioiTinh;
    RadioButton rbNam, rbNu;
    Spinner spinnerLop;
    Button btnThemSV, btnSuaSV, btnXoaSV, btnThoatSV;
    ListView lvSinhVien;

    ArrayList<String> dsSinhVien;
    ArrayAdapter<String> adapterSV;

    ArrayList<String> dsLop;
    ArrayAdapter<String> adapterLop;

    CreateDatabase createDatabase;
    SQLiteDatabase db;

    String selectedLop = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlysinhvien_layout);

        // Ánh xạ
        edtMaSV = findViewById(R.id.edtMaSV);
        edtHoTenSV = findViewById(R.id.edtHoTenSV);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        spinnerLop = findViewById(R.id.spinnerLop);
        btnThemSV = findViewById(R.id.btnThemSV);
        btnSuaSV = findViewById(R.id.btnSuaSV);

        btnXoaSV = findViewById(R.id.btnXoaSV);
        lvSinhVien = findViewById(R.id.lvSinhVien);

        // DB
        createDatabase = new CreateDatabase(this);
        db = createDatabase.getWritableDatabase();

        // Load danh sách lớp
        dsLop = new ArrayList<>();
        loadLop();
        adapterLop = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsLop);
        adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapterLop);

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedLop = dsLop.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ListView sinh viên
        dsSinhVien = new ArrayList<>();
        adapterSV = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsSinhVien);
        lvSinhVien.setAdapter(adapterSV);
        loadSinhVien();

        // === THÊM SINH VIÊN ===
        btnThemSV.setOnClickListener(v -> {
            String maSV = edtMaSV.getText().toString().trim();
            String ten = edtHoTenSV.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String gt = rbNam.isChecked() ? "Nam" : "Nữ";

            if (maSV.isEmpty() || ten.isEmpty() || selectedLop.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor c = db.rawQuery("SELECT * FROM SinhVien WHERE MaSV=?", new String[]{maSV});
            if (c.moveToFirst()) {
                Toast.makeText(this, "Mã sinh viên đã tồn tại!", Toast.LENGTH_SHORT).show();
                c.close();
                return;
            }

            // Thêm vào bảng SinhVien
            ContentValues values = new ContentValues();
            values.put("MaSV", maSV);
            values.put("HoTen", ten);
            values.put("NgaySinh", ngaySinh);
            values.put("GioiTinh", gt);
            values.put("DiaChi", diaChi);
            values.put("MaLop", selectedLop);
            db.insert("SinhVien", null, values);
            c.close();

            // === Tạo tài khoản tự động ===
            Cursor checkUser = db.rawQuery("SELECT * FROM NguoiDung WHERE Username=?", new String[]{maSV});
            if (!checkUser.moveToFirst()) {
                ContentValues userValues = new ContentValues();
                userValues.put("Username", maSV);
                userValues.put("Password", "123456"); // Mật khẩu mặc định
                userValues.put("Role", "SinhVien");
                userValues.put("MaSV", maSV);
                db.insert("NguoiDung", null, userValues);
                Toast.makeText(this, "✅ Đã tạo tài khoản cho sinh viên!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "⚠️ Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
            checkUser.close();

            Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
            loadSinhVien();
        });

        // === SỬA ===
        btnSuaSV.setOnClickListener(v -> {
            String ma = edtMaSV.getText().toString().trim();
            String ten = edtHoTenSV.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String gt = rbNam.isChecked() ? "Nam" : "Nữ";

            ContentValues values = new ContentValues();
            values.put("HoTen", ten);
            values.put("NgaySinh", ngaySinh);
            values.put("GioiTinh", gt);
            values.put("DiaChi", diaChi);
            values.put("MaLop", selectedLop);

            int rows = db.update("SinhVien", values, "MaSV=?", new String[]{ma});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                loadSinhVien();
            } else {
                Toast.makeText(this, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
            }
        });

        // === XÓA ===
        btnXoaSV.setOnClickListener(v -> {
            String ma = edtMaSV.getText().toString().trim();
            db.delete("SinhVien", "MaSV=?", new String[]{ma});
            db.delete("NguoiDung", "MaSV=?", new String[]{ma});
            Toast.makeText(this, "Đã xóa sinh viên và tài khoản liên quan!", Toast.LENGTH_SHORT).show();
            loadSinhVien();
        });

        // === HIỂN THỊ DỮ LIỆU LÊN FORM ===
        lvSinhVien.setOnItemClickListener((parent, view, position, id) -> {
            String[] parts = dsSinhVien.get(position).split(" - ");
            edtMaSV.setText(parts[0]);
            edtHoTenSV.setText(parts[1]);
            edtNgaySinh.setText(parts[2]);
            edtDiaChi.setText(parts[3]);
            if (parts[4].equals("Nam")) rbNam.setChecked(true); else rbNu.setChecked(true);

            for (int i = 0; i < dsLop.size(); i++) {
                if (dsLop.get(i).equals(parts[5])) {
                    spinnerLop.setSelection(i);
                    break;
                }
            }
        });

        btnThoatSV.setOnClickListener(v -> {
            Intent intent = new Intent(QuanlySinhvienActivity.this, AdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadSinhVien() {
        dsSinhVien.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM SinhVien", null);
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            String ngaySinh = cursor.getString(2);
            String gt = cursor.getString(3);
            String diaChi = cursor.getString(4);
            String lop = cursor.getString(5);
            dsSinhVien.add(ma + " - " + ten + " - " + ngaySinh + " - " + diaChi + " - " + gt + " - " + lop);
        }
        cursor.close();
        adapterSV.notifyDataSetChanged();
    }

    private void loadLop() {
        dsLop.clear();
        Cursor cursor = db.rawQuery("SELECT MaLop FROM Lop", null);
        while (cursor.moveToNext()) {
            dsLop.add(cursor.getString(0));
        }
        cursor.close();
    }
}