package com.example.project1.Admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project1.R;
import com.example.project1.database.CreateDatabase;
import java.util.ArrayList;

public class Quanlytaikhoan extends AppCompatActivity {

    EditText edtPassword, edtRole;
    Button  btnUpdate, btnDelete;
    ListView listTaiKhoan;
    Spinner spnUsername;
    SQLiteDatabase db;
    ArrayList<String> dsTaiKhoan = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> dsUser = new ArrayList<>();
    ArrayAdapter<String> adapterUser;
    CreateDatabase dbHelper;
    String selectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlytaikhoan);

        // Ánh xạ view
        spnUsername = findViewById(R.id.spnUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRole = findViewById(R.id.edtRole);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        listTaiKhoan = findViewById(R.id.listTaiKhoan);
        edtRole.setEnabled(false); // Không cho sửa Role

        // Mở database
        dbHelper = new CreateDatabase(this);
        db = dbHelper.open();

        // Load danh sách
        loadUsernameList();
        loadData();

        // Khi bấm vào 1 tài khoản trong danh sách
        listTaiKhoan.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = dsTaiKhoan.get(i);
            String[] parts = item.split(" - ");
            selectedUser = parts[0];
            edtRole.setText(parts[1]);

            Cursor c = db.rawQuery("SELECT Password FROM NguoiDung WHERE Username=?", new String[]{selectedUser});
            if (c.moveToFirst()) {
                edtPassword.setText(c.getString(0));
            }
            c.close();
        });



        // Nút sửa
        btnUpdate.setOnClickListener(v -> {
            if (selectedUser == null) {
                Toast.makeText(this, "Hãy chọn tài khoản để sửa!", Toast.LENGTH_SHORT).show();
                return;
            }

            String pass = edtPassword.getText().toString().trim();
            String role = edtRole.getText().toString().trim();

            db.execSQL("UPDATE NguoiDung SET Password=?, Role=? WHERE Username=?",
                    new Object[]{pass, role, selectedUser});

            Toast.makeText(this, "Đã cập nhật tài khoản", Toast.LENGTH_SHORT).show();
            loadData();
            loadUsernameList();
        });

        // Nút xóa
        btnDelete.setOnClickListener(v -> {
            if (selectedUser == null) {
                Toast.makeText(this, "Hãy chọn tài khoản để xóa!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.execSQL("DELETE FROM NguoiDung WHERE Username=?", new Object[]{selectedUser});
            Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();

            loadData();
            loadUsernameList();
        });
    }

    // Load danh sách tài khoản vào ListView
    private void loadData() {
        dsTaiKhoan.clear();
        Cursor c = db.rawQuery("SELECT Username, Role FROM NguoiDung", null);
        while (c.moveToNext()) {
            dsTaiKhoan.add(c.getString(0) + " - " + c.getString(1));
        }
        c.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsTaiKhoan);
        listTaiKhoan.setAdapter(adapter);

        edtPassword.setText("");
        edtRole.setText("");
        selectedUser = null;
    }

    // Load danh sách SV + GV chưa có tài khoản
    private void loadUsernameList() {
        dsUser.clear();
        dsUser.add("           ");

        // Lấy SV chưa có tài khoản
        Cursor cursorSV = db.rawQuery("SELECT MaSV FROM SinhVien WHERE MaSV NOT IN (SELECT Username FROM NguoiDung)", null);
        while (cursorSV.moveToNext()) {
            dsUser.add(cursorSV.getString(0));
        }
        cursorSV.close();

        // Lấy GV chưa có tài khoản
        Cursor cursorGV = db.rawQuery("SELECT MaGV FROM GiangVien WHERE MaGV NOT IN (SELECT Username FROM NguoiDung)", null);
        while (cursorGV.moveToNext()) {
            dsUser.add(cursorGV.getString(0));
        }
        cursorGV.close();

        // Thêm admin (nếu muốn hiển thị để chỉnh sửa)
        if (!dsUser.contains("admin")) {
            dsUser.add("admin");
        }

        adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsUser);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUsername.setAdapter(adapterUser);
    }
}
