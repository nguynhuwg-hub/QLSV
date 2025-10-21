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



public class QuanlyGiangvienActivity extends AppCompatActivity {

    EditText edtMaGV, edtTenGV, edtBoMon;
    Button btnThem, btnSua, btnXoa;
    ListView listGiangVien;

    CreateDatabase dbHelper;
    ArrayList<String> dsGV = new ArrayList<>();
    ArrayAdapter<String> adapterGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlygiangvien_layout);

        dbHelper = new CreateDatabase(this);

        edtMaGV = findViewById(R.id.edtMaGV);
        edtTenGV = findViewById(R.id.edtTenGV);
        edtBoMon = findViewById(R.id.edtBoMon);
        btnThem = findViewById(R.id.btnThemGV);
        btnSua = findViewById(R.id.btnSuaGV);
        btnXoa = findViewById(R.id.btnXoaGV);
        listGiangVien = findViewById(R.id.listGiangVien);

        adapterGV = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsGV);
        listGiangVien.setAdapter(adapterGV);

        loadGiangVien();

        listGiangVien.setOnItemClickListener((parent, view, position, id) -> {
            String line = dsGV.get(position);
            String[] parts = line.split(" - ");
            if (parts.length >= 3) {
                edtMaGV.setText(parts[0]);
                edtTenGV.setText(parts[1]);
                edtBoMon.setText(parts[2]);
            }
        });

        btnThem.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String tenGV = edtTenGV.getText().toString().trim();
            String boMon = edtBoMon.getText().toString().trim();

            if (maGV.isEmpty() || tenGV.isEmpty() || boMon.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM GiangVien WHERE MaGV=?", new String[]{maGV});
            if (c.moveToFirst()) {
                Toast.makeText(this, "Mã giảng viên đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("MaGV", maGV);
                values.put("HoTen", tenGV);
                values.put("BoMon", boMon);
                db.insert("GiangVien", null, values);
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                loadGiangVien();
            }
            c.close();
        });

        btnSua.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            String tenGV = edtTenGV.getText().toString().trim();
            String boMon = edtBoMon.getText().toString().trim();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("HoTen", tenGV);
            values.put("BoMon", boMon);
            int rows = db.update("GiangVien", values, "MaGV=?", new String[]{maGV});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                loadGiangVien();
            }
        });

        btnXoa.setOnClickListener(v -> {
            String maGV = edtMaGV.getText().toString().trim();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("GiangVien", "MaGV=?", new String[]{maGV});
            Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            loadGiangVien();
        });
    }

    private void loadGiangVien() {
        dsGV.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaGV, HoTen, BoMon FROM GiangVien", null);
        while (c.moveToNext()) {
            dsGV.add(c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2));
        }
        c.close();
        adapterGV.notifyDataSetChanged();
    }
}