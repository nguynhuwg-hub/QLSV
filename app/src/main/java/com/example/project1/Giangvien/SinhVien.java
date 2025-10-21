package com.example.project1.Giangvien;

public class SinhVien {
    public String maSV;
    public String hoTen;
    public String maLop;
    public String tenMH;
    public double diemQT, diemGK, diemCK, diemTK;

    // 🟩 Constructor ngắn dùng khi lấy danh sách sinh viên theo lớp
    public SinhVien(String maSV, String hoTen, String maLop) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.maLop = maLop;
        this.tenMH = "";
        this.diemQT = this.diemGK = this.diemCK = this.diemTK = 0.0;
    }

    // 🟦 Constructor đầy đủ (khi có điểm và môn học)
    public SinhVien(String maSV, String hoTen, String maLop, String tenMH,
                    double diemQT, double diemGK, double diemCK, double diemTK) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.maLop = maLop;
        this.tenMH = tenMH;
        this.diemQT = diemQT;
        this.diemGK = diemGK;
        this.diemCK = diemCK;
        this.diemTK = diemTK;
    }

    // 🟨 Getter (nếu bạn cần sau này)
    public String getMaSV() { return maSV; }
    public String getHoTen() { return hoTen; }
    public String getMaLop() { return maLop; }
    public String getTenMH() { return tenMH; }
}


