package com.example.project1.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Giangvien.MainActivity;
import com.example.project1.R;
import com.example.project1.Sinhvien.SinhvienActivity;
import com.example.project1.database.CreateDatabase;

public class DangnhapActivity extends AppCompatActivity {
    CreateDatabase createDatabase;
    Button btnDN;
    EditText edtDN, edtMK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap_layout);

        edtDN = findViewById(R.id.edtDN);
        edtMK = findViewById(R.id.edtMK);
        btnDN = findViewById(R.id.btnDN);
        createDatabase = new CreateDatabase(this);

        btnDN.setOnClickListener(v -> {
            String username = edtDN.getText().toString().trim();
            String password = edtMK.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = createDatabase.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT Role, MaSV, MaGV FROM NguoiDung WHERE Username=? AND Password=?",
                    new String[]{username, password});

            if (cursor != null && cursor.moveToFirst()) {
                String role = cursor.getString(cursor.getColumnIndexOrThrow("Role"));
                String maSV = cursor.getString(cursor.getColumnIndexOrThrow("MaSV"));
                String maGV = cursor.getString(cursor.getColumnIndexOrThrow("MaGV"));

                Log.d("DEBUG_LOGIN", "Role=" + role + ", MaSV=" + maSV + ", MaGV=" + maGV);

                if ("Admin".equalsIgnoreCase(role)) {
                    startActivity(new Intent(this, AdminActivity.class));

                } else if ("SinhVien".equalsIgnoreCase(role)) {
                    // kiểm tra maSV có null không
                    if (maSV == null || maSV.isEmpty()) {
                        Toast.makeText(this, "Không tìm thấy mã sinh viên", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Lấy tên sinh viên
                    Cursor curSV = db.rawQuery(
                            "SELECT HoTen FROM SinhVien WHERE MaSV=?",
                            new String[]{maSV});
                    String tenSV = "";
                    if (curSV.moveToFirst()) {
                        tenSV = curSV.getString(0);
                    }
                    curSV.close();

                    Intent intent = new Intent(this, SinhvienActivity.class);
                    intent.putExtra("MaSV", maSV);
                    intent.putExtra("TenSV", tenSV);
                    startActivity(intent);

                } else if ("GiangVien".equalsIgnoreCase(role)) {
                    startActivity(new Intent(this, MainActivity.class));
                }

                cursor.close();
                db.close();
                finish();

            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        });
    }
}