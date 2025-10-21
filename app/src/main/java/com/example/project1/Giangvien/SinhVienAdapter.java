package com.example.project1.Giangvien;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.ViewHolder> {

    private Context context;
    private List<SinhVien> list;
    private String maLop;

    public SinhVienAdapter(Context context, List<SinhVien> list, String maLop) {
        this.context = context;
        this.list = list;
        this.maLop = maLop;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sv = list.get(position);
        holder.tvHoTen.setText(sv.hoTen);
        holder.tvMaSV.setText("Mã SV: " + sv.maSV);
        holder.tvMonHoc.setText("Môn học: " + (sv.tenMH != null ? sv.tenMH : "Chưa chọn"));

        holder.buttonNhapDiem.setOnClickListener(v -> {
            Intent intent = new Intent(context, Enter_scores.class);
            intent.putExtra("MaSV", sv.maSV);
            intent.putExtra("HoTen", sv.hoTen);
            intent.putExtra("MaLop", sv.maLop);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.buttonXemDiem.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_scores.class);
            intent.putExtra("MaSV", sv.maSV);
            intent.putExtra("HoTen", sv.hoTen);
            intent.putExtra("MaLop", sv.maLop);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvMaSV, tvMonHoc;
        Button buttonNhapDiem,buttonXemDiem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvMaSV = itemView.findViewById(R.id.tvMaSV);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            buttonNhapDiem = itemView.findViewById(R.id.buttonNhapDiem);
            buttonXemDiem = itemView.findViewById(R.id.buttonXemDiem);
        }
    }
}



