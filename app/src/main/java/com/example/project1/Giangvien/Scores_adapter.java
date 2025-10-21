package com.example.project1.Giangvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.List;

public class Scores_adapter extends RecyclerView.Adapter<Scores_adapter.ViewHolder> {
    private Context context;
    private List<Scores> list;

    public Scores_adapter(Context context, List<Scores> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scores d = list.get(position);
        holder.tvMonHoc.setText("Môn: " + d.tenMH);
        holder.tvDiemQT.setText("QT: " + d.diemQT);
        holder.tvDiemGK.setText("GK: " + d.diemGK);
        holder.tvDiemCK.setText("CK: " + d.diemCK);
        holder.tvDiemTK.setText("TK: " + d.diemTK);
        holder.tvTrangThai.setText("Trạng thái: " + d.trangThai);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonHoc, tvDiemQT, tvDiemGK, tvDiemCK, tvDiemTK, tvTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvDiemQT = itemView.findViewById(R.id.tvDiemQT);
            tvDiemGK = itemView.findViewById(R.id.tvDiemGK);
            tvDiemCK = itemView.findViewById(R.id.tvDiemCK);
            tvDiemTK = itemView.findViewById(R.id.tvDiemTK);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}

