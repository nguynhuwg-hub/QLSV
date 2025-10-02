package com.example.project1.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Giangvien.GiangvienActivity;
import com.example.project1.R;
import com.example.project1.Sinhvien.SinhvienActivity;
import com.example.project1.database.CreateDatabase;

public class DangnhapActivity extends AppCompatActivity {
    CreateDatabase createDatabase;
    Button btnDN;
    EditText edtDN , edtMK;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap_layout);
        edtDN = (EditText) findViewById(R.id.edtDN);
        edtMK = (EditText) findViewById(R.id.edtMK);
        btnDN = (Button) findViewById(R.id.btnDN);
        createDatabase = new CreateDatabase(this);
        btnDN.setOnClickListener(v -> {                                  // (7)
            String username = edtDN.getText().toString().trim();     // (8)
            String password = edtMK.getText().toString().trim();     // (9)

            if (username.isEmpty() || password.isEmpty()) {                // (10)
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show(); // (11)
                return;                                                    // (12)
            }

            SQLiteDatabase db = createDatabase.getReadableDatabase();
            Log.d("DB_TEST", "Username nhập: " + username + " - Password nhập: " + password);
            // (13)
            Cursor cursor = db.rawQuery(
                    "SELECT Role FROM NguoiDung WHERE Username=? AND Password=?",
                    new String[]{username, password});                         // (14)

            if (cursor.moveToFirst()) {                                   // (15)
                String role = cursor.getString(0);                        // (16)
                Toast.makeText(this, "Đăng nhập thành công: " + role, Toast.LENGTH_SHORT).show(); // (17)

                if (role.equalsIgnoreCase("Admin"))  {                               // (18)
                    startActivity(new Intent(this, AdminActivity.class));  // (19)
                } else if (role.equalsIgnoreCase("SinhVien")) {
                    startActivity(new Intent(this, SinhvienActivity.class));
                } else if (role.equalsIgnoreCase("GiangVien")) {
                    startActivity(new Intent(this, GiangvienActivity.class));
                }
                finish();                                                  // (20)
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show(); // (21)
            }

            cursor.close();                                               // (22)
            db.close();
        });
        }
}
