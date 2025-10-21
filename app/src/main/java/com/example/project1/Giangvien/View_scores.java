package com.example.project1.Giangvien;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.database.CreateDatabase;

import java.util.ArrayList;

public class View_scores extends AppCompatActivity {

    private RecyclerView recyclerViewScores;
    private TextView tvTenSV, tvMaSV, tvLop;
    private CreateDatabase dbHelper;
    private SQLiteDatabase database;
    private ArrayList<Scores> listDiem;
    private Scores_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        recyclerViewScores = findViewById(R.id.recyclerViewScores);
        tvTenSV = findViewById(R.id.tvTenSV);
        tvMaSV = findViewById(R.id.tvMaSV);
        tvLop = findViewById(R.id.tvLop);

        dbHelper = new  CreateDatabase(this);
        database = dbHelper.open();

        // Nhận dữ liệu từ Intent
        String maSV = getIntent().getStringExtra("MaSV");
        String hoTen = getIntent().getStringExtra("HoTen");
        String maLop = getIntent().getStringExtra("MaLop");

        tvTenSV.setText("Họ tên: " + hoTen);
        tvMaSV.setText("Mã SV: " + maSV);
        tvLop.setText("Lớp: " + maLop);

        // Lấy danh sách điểm của sinh viên đó
        listDiem = new ArrayList<>();
        try {
            Cursor c = database.rawQuery(
                    "SELECT mh.TenMH, d.DiemQT, d.DiemGK, d.DiemCK, d.DiemTK, d.TrangThai " +
                            "FROM Diem d JOIN LopMonHoc lm ON d.MaLopMH = lm.MaLopMH " +
                            "JOIN MonHoc mh ON lm.MaMH = mh.MaMH " +
                            "WHERE d.MaSV = ?", new String[]{maSV});

            while (c.moveToNext()) {
                listDiem.add(new Scores(
                        c.getString(0),
                        c.getDouble(1),
                        c.getDouble(2),
                        c.getDouble(3),
                        c.getDouble(4),
                        c.getString(5)
                ));
            }
            c.close();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đọc dữ liệu điểm!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        adapter = new Scores_adapter(this, listDiem);
        recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewScores.setAdapter(adapter);
    }
}

