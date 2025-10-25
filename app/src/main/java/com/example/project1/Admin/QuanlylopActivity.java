package com.example.project1.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class QuanlylopActivity extends AppCompatActivity {

    EditText edtMaLop, edtTenLop;
    Spinner spinnerNganh;
    Button btnThem, btnSua, btnXoa ;
    ListView lvLop;

    ArrayList<String> listLop = new ArrayList<>();
    ArrayAdapter<String> adapterLop;

    ArrayList<String> listNganh = new ArrayList<>();
    ArrayAdapter<String> adapterNganh;

    String maNganhChon = "";
    String maLopChon = "";

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlylop_layout);

        anhXa();
        CreateDatabase createDatabase = new CreateDatabase(this);
        database = createDatabase.getWritableDatabase();

        loadNganh();
        loadLop();

        spinnerNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maNganhChon = listNganh.get(i).split(" - ")[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        lvLop.setOnItemClickListener((parent, view, position, id) -> {
            String item = listLop.get(position);
            // Dạng chuỗi: "74DCUT24 - HTTT (CNTT)"
            String maLop = item.substring(0, item.indexOf(" - "));
            String tenLop = item.substring(item.indexOf(" - ") + 3, item.lastIndexOf(" ("));
            edtMaLop.setText(maLop);
            edtTenLop.setText(tenLop);
            maLopChon = maLop;
        });

        btnThem.setOnClickListener(v -> themLop());
        btnSua.setOnClickListener(v -> suaLop());
        btnXoa.setOnClickListener(v -> xoaLop());

    }

    private void anhXa() {
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);
        spinnerNganh = findViewById(R.id.spinnerNganh);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        lvLop = findViewById(R.id.lvLop);
    }

    private void loadNganh() {
        listNganh.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM Nganh", null);
        while (cursor.moveToNext()) {
            String ma = cursor.getString(0);
            String ten = cursor.getString(1);
            listNganh.add(ma + " - " + ten);
        }
        cursor.close();
        adapterNganh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listNganh);
        adapterNganh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNganh.setAdapter(adapterNganh);
    }

    private void loadLop() {
        listLop.clear();
        Cursor cursor = database.rawQuery(
                "SELECT Lop.MaLop, Lop.TenLop, Nganh.TenNganh, Nganh.MaNganh " +
                        "FROM Lop INNER JOIN Nganh ON Lop.MaNganh = Nganh.MaNganh", null);

        while (cursor.moveToNext()) {
            String maLop = cursor.getString(0);
            String tenLop = cursor.getString(1);
            String tenNganh = cursor.getString(2);
            String maNganh = cursor.getString(3);
            // chỉ hiển thị, không thêm vào dữ liệu gốc
            listLop.add(maLop + " - " + tenLop + " (" + maNganh + ")");
        }
        cursor.close();
        adapterLop = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listLop);
        lvLop.setAdapter(adapterLop);
    }

    private void themLop() {
        String maLop = edtMaLop.getText().toString().trim();
        String tenLop = edtTenLop.getText().toString().trim();

        if (maLop.isEmpty() || tenLop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("MaLop", maLop);
        values.put("TenLop", tenLop);
        values.put("MaNganh", maNganhChon);

        long kq = database.insert("Lop", null, values);
        if (kq != -1) {
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            loadLop();
        } else {
            Toast.makeText(this, "Thêm thất bại (trùng mã lớp?)", Toast.LENGTH_SHORT).show();
        }
    }

    private void suaLop() {
        if (maLopChon.isEmpty()) {
            Toast.makeText(this, "Hãy chọn lớp cần sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("TenLop", edtTenLop.getText().toString().trim());
        values.put("MaNganh", maNganhChon);

        int kq = database.update("Lop", values, "MaLop = ?", new String[]{maLopChon});
        if (kq > 0) {
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
            loadLop();
        } else {
            Toast.makeText(this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaLop() {
        if (maLopChon.isEmpty()) {
            Toast.makeText(this, "Hãy chọn lớp cần xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        int kq = database.delete("Lop", "MaLop = ?", new String[]{maLopChon});
        if (kq > 0) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            loadLop();
        } else {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
