package com.example.demo_listview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class listview extends AppCompatActivity {
    Button btnThem, btnXoa;
    EditText edtTen;
    ListView lvMonHoc;
    ArrayList <String> dsMonHoc;
    ArrayAdapter aaMonHoc;
    int vitridangchon = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_demo);

        edtTen = findViewById(R.id.edtTen);
        btnThem = findViewById(R.id.btnthem);
        btnXoa = findViewById(R.id.btnXoa);
        lvMonHoc = findViewById(R.id.lvMonHoc);

        dsMonHoc = new ArrayList<>();
        aaMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsMonHoc);
        lvMonHoc.setAdapter(aaMonHoc);
    }
}
