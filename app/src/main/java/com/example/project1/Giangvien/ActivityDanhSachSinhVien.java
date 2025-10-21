package com.example.project1.Giangvien;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

public class ActivityDanhSachSinhVien extends AppCompatActivity {

    private Spinner spinnerLop;
    private RecyclerView recyclerViewSV;
    private CreateDatabase db; // ⚡ Dùng Database chính, không dùng DatabaseHelper
    private SinhVienAdapter adapter;
    private ArrayList<SinhVien> listSV;
    private ArrayList<String> listLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.admin_choose_and_enter);
        spinnerLop = findViewById(R.id.spinnerLop);
        recyclerViewSV = findViewById(R.id.recyclerViewSV);
        recyclerViewSV.setLayoutManager(new LinearLayoutManager(this));

        try {
            db = new CreateDatabase(this); // Kết nối đến DB thật
            listLop = db.getAllLop(); // Lấy danh sách lớp

            if (listLop.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu lớp trong cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayAdapter<String> lopAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_dropdown_item, listLop);
            spinnerLop.setAdapter(lopAdapter);

            spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String maLop = listLop.get(position);
                    try {
                        listSV = db.getSinhVienByLop(maLop);
                        if (listSV.isEmpty()) {
                            Toast.makeText(ActivityDanhSachSinhVien.this, "Không có sinh viên trong lớp " + maLop, Toast.LENGTH_SHORT).show();
                        }
                        adapter = new SinhVienAdapter(ActivityDanhSachSinhVien.this, listSV, maLop);
                        recyclerViewSV.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(ActivityDanhSachSinhVien.this, "Lỗi khi lấy sinh viên: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không chọn gì
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

