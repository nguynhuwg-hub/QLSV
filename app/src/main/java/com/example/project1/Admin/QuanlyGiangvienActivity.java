package com.example.project1.Admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project1.R;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuanlyGiangvienActivity extends AppCompatActivity {

    EditText edtMaGV, edtTenGV;
    Spinner spinnerMonHoc, spinnerLopMonHoc;
    Button btnThem, btnSua, btnXoa, btnPhanCong, btnBoPhanCong;
    ListView listGiangVien, listPhanCong;

    CreateDatabase dbHelper;
    ArrayAdapter<String> adapterGV, adapterPhanCong, adapterMonHoc, adapterLopMonHoc;
    ArrayList<String> dsGV = new ArrayList<>();
    ArrayList<String> dsPhanCong = new ArrayList<>();
    ArrayList<String> dsMonHoc = new ArrayList<>();
    ArrayList<String> dsLopMonHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlygiangvien_layout);

        dbHelper = new CreateDatabase(this);

        // ánh xạ
        edtMaGV = findViewById(R.id.edtMaGV);
        edtTenGV = findViewById(R.id.edtTenGV);
        spinnerMonHoc = findViewById(R.id.spinnerMonHoc);
        spinnerLopMonHoc = findViewById(R.id.spinnerLopMonHoc);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnPhanCong = findViewById(R.id.btnPhanCong);
        btnBoPhanCong = findViewById(R.id.btnBoPhanCong);
        listGiangVien = findViewById(R.id.listGiangVien);
        listPhanCong = findViewById(R.id.listPhanCong);

        // adapter
        adapterGV = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsGV);
        listGiangVien.setAdapter(adapterGV);

        adapterPhanCong = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsPhanCong);
        listPhanCong.setAdapter(adapterPhanCong);

        adapterMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsMonHoc);
        adapterMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(adapterMonHoc);

        adapterLopMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsLopMonHoc);
        adapterLopMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLopMonHoc.setAdapter(adapterLopMonHoc);

        // load dữ liệu
        loadGiangVien();
        loadMonHoc();
        loadLopMonHoc();
        loadPhanCong();

        // khi chọn GV từ list
        listGiangVien.setOnItemClickListener((parent, view, position, id) -> {
            String line = dsGV.get(position);
            // Format: "GV001 - Nguyễn Văn A"
            String[] parts = line.split(" - ");
            if (parts.length >= 2) {
                edtMaGV.setText(parts[0].trim());
                edtTenGV.setText(parts[1].trim());
            }
        });

        // thêm giảng viên
        btnThem.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String tenGV = edtTenGV.getText().toString().trim();

            if (maGV.isEmpty() || tenGV.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM GiangVien WHERE MaGV=?", new String[]{maGV});
            if (c.moveToFirst()) {
                Toast.makeText(this, "Mã GV đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("MaGV", maGV);
                values.put("HoTen", tenGV);
                db.insert("GiangVien", null, values);
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                loadGiangVien();
            }
            c.close();
        });

        // sửa giảng viên
        btnSua.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String tenGV = edtTenGV.getText().toString().trim();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("HoTen", tenGV);
            int rows = db.update("GiangVien", values, "MaGV=?", new String[]{maGV});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                loadGiangVien();
            }
        });

        // xóa giảng viên
        btnXoa.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("GiangVien", "MaGV=?", new String[]{maGV});
            Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            loadGiangVien();
        });

        // phân công
        btnPhanCong.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String maLop = spinnerLopMonHoc.getSelectedItem().toString();
            String maMon = getMaMon(spinnerMonHoc.getSelectedItem().toString());

            if (maGV.isEmpty() || maLop.isEmpty() || maMon.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin phân công!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String maLopMH = maLop + "_" + maMon; // sinh khóa chính đơn giản
            values.put("MaLopMH", maLopMH);
            values.put("MaLop", maLop);
            values.put("MaMH", maMon);
            values.put("MaGV", maGV);
            db.insert("LopMonHoc", null, values);
            Toast.makeText(this, "Phân công thành công!", Toast.LENGTH_SHORT).show();
            loadPhanCong();
        });

        // bỏ phân công
        btnBoPhanCong.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String maLop = spinnerLopMonHoc.getSelectedItem().toString();
            String maMon = getMaMon(spinnerMonHoc.getSelectedItem().toString());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete("LopMonHoc", "MaGV=? AND MaMH=? AND MaLop=?",
                    new String[]{maGV, maMon, maLop});
            if (rows > 0) {
                Toast.makeText(this, "Đã bỏ phân công!", Toast.LENGTH_SHORT).show();
                loadPhanCong();
            } else {
                Toast.makeText(this, "Không có phân công để xóa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGiangVien() {
        dsGV.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaGV, HoTen FROM GiangVien", null);
        while (c.moveToNext()) {
            dsGV.add(c.getString(0) + " - " + c.getString(1));
        }
        c.close();
        adapterGV.notifyDataSetChanged();
    }

    private void loadMonHoc() {
        dsMonHoc.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT TenMH FROM MonHoc", null);
        while (c.moveToNext()) {
            dsMonHoc.add(c.getString(0));
        }
        c.close();
        adapterMonHoc.notifyDataSetChanged();
    }

    private void loadLopMonHoc() {
        dsLopMonHoc.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaLop FROM Lop", null);
        while (c.moveToNext()) {
            dsLopMonHoc.add(c.getString(0));
        }
        c.close();
        adapterLopMonHoc.notifyDataSetChanged();
    }

    private void loadPhanCong() {
        dsPhanCong.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT gv.HoTen, l.TenLop, mh.TenMH " +
                "FROM LopMonHoc lm " +
                "JOIN GiangVien gv ON lm.MaGV = gv.MaGV " +
                "JOIN Lop l ON lm.MaLop = l.MaLop " +
                "JOIN MonHoc mh ON lm.MaMH = mh.MaMH", null);
        while (c.moveToNext()) {
            dsPhanCong.add(c.getString(0) + " | Lớp: " + c.getString(1) + " | Môn: " + c.getString(2));
        }
        c.close();
        adapterPhanCong.notifyDataSetChanged();
    }

    private String getMaMon(String tenMon) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaMH FROM MonHoc WHERE TenMH=?", new String[]{tenMon});
        String maMon = "";
        if (c.moveToFirst()) maMon = c.getString(0);
        c.close();
        return maMon;
    }
}