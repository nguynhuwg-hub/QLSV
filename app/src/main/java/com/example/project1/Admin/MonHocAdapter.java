package com.example.project1.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.ArrayList;




public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.ViewHolder> {

    private Context context;
    private ArrayList<com.example.project1.Admin.MonHoc> list;
    private com.example.project1.Admin.MonHocDAO dao;

    public MonHocAdapter(Context context, ArrayList<com.example.project1.Admin.MonHoc> list, com.example.project1.Admin.MonHocDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_monhoc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonHoc monHoc = list.get(position);
        holder.tvMaMH.setText("Mã MH: " + monHoc.getMaMH());
        holder.tvTenMH.setText(monHoc.getTenMH());
        holder.tvSoTinChi.setText("Số tín chỉ: " + monHoc.getSoTinChi());

        // ✅ Nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_monhoc, null);
            builder.setView(view);

            EditText edtMaMH = view.findViewById(R.id.edtMaMH);
            EditText edtTenMH = view.findViewById(R.id.edtTenMH);
            EditText edtSoTinChi = view.findViewById(R.id.edtSoTinChi);
            Button btnLuu = view.findViewById(R.id.btnLuu);
            Button btnHuy = view.findViewById(R.id.btnHuy);

            // Gán dữ liệu cũ vào form
            edtMaMH.setText(monHoc.getMaMH());
            edtTenMH.setText(monHoc.getTenMH());
            edtSoTinChi.setText(String.valueOf(monHoc.getSoTinChi()));

            // ✅ Cho phép sửa mã môn học (primary key)
            edtMaMH.setEnabled(true);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Xử lý nút Lưu
            btnLuu.setOnClickListener(x -> {
                String maMoi = edtMaMH.getText().toString().trim();
                String tenMoi = edtTenMH.getText().toString().trim();
                String tinChiStr = edtSoTinChi.getText().toString().trim();

                if (maMoi.isEmpty() || tenMoi.isEmpty() || tinChiStr.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int tinChiMoi;
                try {
                    tinChiMoi = Integer.parseInt(tinChiStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Số tín chỉ phải là số!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu mã cũ
                String maCu = monHoc.getMaMH();

                // Cập nhật đối tượng
                monHoc.setMaMH(maMoi);
                monHoc.setTenMH(tenMoi);
                monHoc.setSoTinChi(tinChiMoi);

                // ✅ Gọi hàm cập nhật có xử lý đổi khóa chính
                if (dao.updateWithNewId(maCu, monHoc)) {
                    list.set(position, monHoc);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại! (Có thể trùng mã)", Toast.LENGTH_SHORT).show();
                }
            });

            // Nút Hủy
            btnHuy.setOnClickListener(x -> dialog.dismiss());
        });

        // ❌ Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa môn học");
            builder.setMessage("Bạn có chắc chắn muốn xóa " + monHoc.getTenMH() + " không?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                dao.delete(monHoc.getMaMH());
                list.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaMH, tvTenMH, tvSoTinChi;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaMH = itemView.findViewById(R.id.tvMaMH);
            tvTenMH = itemView.findViewById(R.id.tvTenMH);
            tvSoTinChi = itemView.findViewById(R.id.tvSoTinChi);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
