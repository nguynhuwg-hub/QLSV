package com.example.project1.Admin;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class Quanlytaikhoan extends AppCompatActivity {

    EditText edtNhapTK, edtNhapMK, edtSearch;
    Spinner spinnerMS;
    RadioGroup radioGroupRole;
    RadioButton rdoSinhVien, rdoGiangVien;
    Button btnThem, btnSua, btnXoa, btnThoat;
    ListView lvTaiKhoan;
    CreateDatabase dbHelper;
    ArrayAdapter<String> taiKhoanAdapter;
    List<String> listTaiKhoan, listTaiKhoanGoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlytaikhoan);

        // Ánh xạ View
        edtNhapTK = findViewById(R.id.edtNhapTK);
        edtNhapMK = findViewById(R.id.edtNhapMK);
        edtSearch = findViewById(R.id.edtSearch);
        spinnerMS = findViewById(R.id.SpinnerMS);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        rdoSinhVien = findViewById(R.id.rdoSinhVien);
        rdoGiangVien = findViewById(R.id.rdoGiangVien);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnThoat = findViewById(R.id.btnThoat);
        lvTaiKhoan = findViewById(R.id.lvTaiKhoan);

        dbHelper = new CreateDatabase(this);

        // Mặc định load mã Sinh viên
        loadSpinnerData("SinhVien");

        // Khi chọn loại role thì load lại danh sách mã tương ứng
        radioGroupRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdoSinhVien)
                loadSpinnerData("SinhVien");
            else if (checkedId == R.id.rdoGiangVien)
                loadSpinnerData("GiangVien");
        });

        // Load danh sách tài khoản
        loadTaiKhoan();

        // ====== Click ListView: Hiển thị thông tin để sửa ======
        lvTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            String selected = listTaiKhoan.get(position);
            String[] parts = selected.split(" - ");
            if (parts.length >= 3) {
                String user = parts[0];
                String role = parts[1];
                String ma = parts[2];

                edtNhapTK.setText(user);

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT Password FROM NguoiDung WHERE Username=?", new String[]{user});
                if (cursor.moveToFirst()) edtNhapMK.setText(cursor.getString(0));
                cursor.close();

                if (role.equals("SinhVien")) rdoSinhVien.setChecked(true);
                else if (role.equals("GiangVien")) rdoGiangVien.setChecked(true);

                loadSpinnerData(role);

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerMS.getAdapter();
                int index = adapter.getPosition(ma);
                if (index >= 0) spinnerMS.setSelection(index);
            }
        });

        // ====== Nút Thêm ======
        btnThem.setOnClickListener(v -> {
            String user = edtNhapTK.getText().toString().trim();
            String pass = edtNhapMK.getText().toString().trim();
            String role = getSelectedRole();
            String ma = spinnerMS.getSelectedItem() != null ? spinnerMS.getSelectedItem().toString() : "";

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor check = db.rawQuery("SELECT * FROM NguoiDung WHERE Username=?", new String[]{user});
            if (check.moveToFirst()) {
                Toast.makeText(this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                check.close();
                return;
            }
            check.close();

            ContentValues values = new ContentValues();
            values.put("Username", user);
            values.put("Password", pass);
            values.put("Role", role);

            if (role.equals("SinhVien")) {
                values.put("MaSV", ma);
                values.putNull("MaGV");
            } else {
                values.put("MaGV", ma);
                values.putNull("MaSV");
            }

            long result = db.insert("NguoiDung", null, values);
            if (result != -1) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                loadTaiKhoan();
            } else {
                Toast.makeText(this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show();
            }
        });

        // ====== Nút Sửa ======
        btnSua.setOnClickListener(v -> {
            String user = edtNhapTK.getText().toString().trim();
            String pass = edtNhapMK.getText().toString().trim();
            String role = getSelectedRole();
            String ma = spinnerMS.getSelectedItem() != null ? spinnerMS.getSelectedItem().toString() : "";

            if (user.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn tài khoản cần sửa!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            if (!pass.isEmpty()) values.put("Password", pass);
            values.put("Role", role);

            if (role.equals("SinhVien")) {
                values.put("MaSV", ma);
                values.putNull("MaGV");
            } else {
                values.put("MaGV", ma);
                values.putNull("MaSV");
            }

            int rows = db.update("NguoiDung", values, "Username=?", new String[]{user});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                loadTaiKhoan();
            } else {
                Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });

        // ====== Nút Xóa ======
        btnXoa.setOnClickListener(v -> {
            String user = edtNhapTK.getText().toString().trim();
            if (user.isEmpty()) {
                Toast.makeText(this, "Chọn tài khoản cần xóa!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("NguoiDung", "Username=?", new String[]{user});
            Toast.makeText(this, "Đã xóa tài khoản!", Toast.LENGTH_SHORT).show();
            loadTaiKhoan();
        });

        // ====== Nút Thoát ======
        btnThoat.setOnClickListener(v -> finish());

        // ====== Tìm kiếm realtime ======
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTaiKhoan(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // ================== LOAD SPINNER ==================
    private void loadSpinnerData(String role) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor;

        if (role.equals("GiangVien"))
            cursor = db.rawQuery("SELECT MaGV FROM GiangVien", null);
        else
            cursor = db.rawQuery("SELECT MaSV FROM SinhVien", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMS.setAdapter(adapter);
    }

    // ================== LOAD LISTVIEW ==================
    private void loadTaiKhoan() {
        if (listTaiKhoan == null) listTaiKhoan = new ArrayList<>();
        listTaiKhoan.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Username, Role, MaSV, MaGV FROM NguoiDung", null);

        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(0);
                String role = cursor.getString(1);
                String maSV = cursor.getString(2);
                String maGV = cursor.getString(3);

                String ma = "-";
                if ("SinhVien".equals(role) && maSV != null) ma = maSV;
                else if ("GiangVien".equals(role) && maGV != null) ma = maGV;

                listTaiKhoan.add(user + " - " + role + " - " + ma);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (taiKhoanAdapter == null) {
            taiKhoanAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listTaiKhoan);
            lvTaiKhoan.setAdapter(taiKhoanAdapter);
        } else {
            taiKhoanAdapter.notifyDataSetChanged();
        }

        if (listTaiKhoanGoc == null) listTaiKhoanGoc = new ArrayList<>();
        listTaiKhoanGoc.clear();
        listTaiKhoanGoc.addAll(listTaiKhoan);
    }

    // ================== LỌC DANH SÁCH ==================
    private void filterTaiKhoan(String keyword) {
        listTaiKhoan.clear();
        if (keyword.isEmpty()) listTaiKhoan.addAll(listTaiKhoanGoc);
        else {
            for (String tk : listTaiKhoanGoc) {
                if (tk.toLowerCase().contains(keyword.toLowerCase())) {
                    listTaiKhoan.add(tk);
                }
            }
        }
        taiKhoanAdapter.notifyDataSetChanged();
    }

    private String getSelectedRole() {
        int id = radioGroupRole.getCheckedRadioButtonId();
        if (id == R.id.rdoSinhVien) return "SinhVien";
        else return "GiangVien";
    }
}