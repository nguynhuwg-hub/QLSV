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
    Button btnThemSV, btnSuaSV, btnXoaSV,btnThoatSV;
    ListView lvSinhVien;

    ArrayList<String> dsSinhVien;
    ArrayAdapter<String> adapterSV;

    ArrayList<String> dsLop;  // lưu mã lớp
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
        btnThoatSV = findViewById(R.id.btnThoatSV);
        btnXoaSV = findViewById(R.id.btnXoaSV);
        lvSinhVien = findViewById(R.id.lvSinhVien);

        // DB
        createDatabase = new CreateDatabase(this);
        db = createDatabase.getWritableDatabase();

        // Load danh sách lớp vào Spinner
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

        // Thêm sinh viên
        btnThemSV.setOnClickListener(v -> {
            String ma = edtMaSV.getText().toString().trim();
            String ten = edtHoTenSV.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String gt = rbNam.isChecked() ? "Nam" : "Nữ";

            if(ma.isEmpty() || ten.isEmpty() || selectedLop.isEmpty()){
                Toast.makeText(this, "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("MaSV", ma);
            values.put("HoTen", ten);
            values.put("NgaySinh", ngaySinh);
            values.put("GioiTinh", gt);
            values.put("DiaChi", diaChi);
            values.put("MaLop", selectedLop);

            long kq = db.insert("SinhVien", null, values);
            if(kq == -1){
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                loadSinhVien();
            }
        });

        // Sửa
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

            int kq = db.update("SinhVien", values, "MaSV=?", new String[]{ma});
            if(kq > 0){
                Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                loadSinhVien();
            }else{
                Toast.makeText(this, "Không tìm thấy SV!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xóa
        btnXoaSV.setOnClickListener(v -> {
            String ma = edtMaSV.getText().toString().trim();
            int kq = db.delete("SinhVien", "MaSV=?", new String[]{ma});
            if(kq > 0){
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                loadSinhVien();
            }else{
                Toast.makeText(this, "Không tìm thấy SV!", Toast.LENGTH_SHORT).show();
            }
        });

        // Click vào list hiển thị lên form
        lvSinhVien.setOnItemClickListener((parent, view, position, id) -> {
            String[] parts = dsSinhVien.get(position).split(" - ");
            edtMaSV.setText(parts[0]);
            edtHoTenSV.setText(parts[1]);
            edtNgaySinh.setText(parts[2]);
            edtDiaChi.setText(parts[3]);

            if(parts[4].equals("Nam")) rbNam.setChecked(true); else rbNu.setChecked(true);

            // Set spinner theo MaLop
            for(int i=0; i<dsLop.size(); i++){
                if(dsLop.get(i).equals(parts[5])){
                    spinnerLop.setSelection(i);
                    break;
                }
            }
        });
    }

    private void loadSinhVien() {
        dsSinhVien.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM SinhVien", null);
        while(cursor.moveToNext()){
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
        while(cursor.moveToNext()){
            dsLop.add(cursor.getString(0));
        }
        cursor.close();
        btnThoatSV.setOnClickListener(v -> {
            Intent intent = new Intent(QuanlySinhvienActivity.this, AdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng AdminActivity
        });
    }
}