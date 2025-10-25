package com.example.project1.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.ArrayList;






public class MonHocActivity extends AppCompatActivity {

    private RecyclerView rcvMonHoc;
    private MonHocDAO dao;
    private ArrayList<MonHoc> list;
    private com.example.project1.Admin.MonHocAdapter adapter;
    private View btnThemMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monhoc);

        rcvMonHoc = findViewById(R.id.rcvMonHoc);
        btnThemMonHoc = findViewById(R.id.btnThemMonHoc);

        dao = new MonHocDAO(this);
        list = new ArrayList<>(dao.getAll());
        adapter = new com.example.project1.Admin.MonHocAdapter(this, list, dao);

        rcvMonHoc.setLayoutManager(new LinearLayoutManager(this));
        rcvMonHoc.setAdapter(adapter);

        btnThemMonHoc.setOnClickListener(v -> showDialogThemMonHoc());
    }

    private void showDialogThemMonHoc() {
        // Tạo view của dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_monhoc, null);
        EditText edtMaMH = view.findViewById(R.id.edtMaMH);
        EditText edtTenMH = view.findViewById(R.id.edtTenMH);
        EditText edtSoTinChi = view.findViewById(R.id.edtSoTinChi);
        Button btnLuu = view.findViewById(R.id.btnLuu);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        // Tạo AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        // Xử lý nút Lưu
        btnLuu.setOnClickListener(v -> {
            String ma = edtMaMH.getText().toString().trim();
            String ten = edtTenMH.getText().toString().trim();
            String tinChiStr = edtSoTinChi.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || tinChiStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soTinChi;
            try {
                soTinChi = Integer.parseInt(tinChiStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tín chỉ phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            MonHoc mh = new MonHoc(ma, ten, soTinChi);

            if (dao.insert(mh)) {
                list.clear();
                list.addAll(dao.getAll());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            } else {
                Toast.makeText(this, "Mã môn học đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút Hủy
        btnHuy.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
