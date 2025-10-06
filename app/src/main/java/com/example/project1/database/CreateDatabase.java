package com.example.project1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;
import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {
public static String TB_Nganh ="Nganh";
public static String TB_Lop ="Lop";
public static String TB_Sinhvien ="SinhVien";
public static String TB_Monhoc ="MonHoc";
public static String TB_GiangVien ="GiangVien";
public static String TB_Lopmonhoc ="LopMonHoc";
public static String TB_Diem ="Diem";
public static String TB_Nguoidung ="NguoiDung";


public static String TB_Nganh_Manganh ="MaNganh";
public static String TB_Nganh_Tennganh ="TenNganh";

public static String TB_Lop_Malop ="MaLop";
public static String TB_Lop_Tenlop ="TenLop";
public static String TB_Lop_Manganh ="MaNganh";
public static String TB_Sinhvien_MaSv ="MaSV";
public static String TB_Sinhvien_Hoten ="HoTen";

public static String TB_Sinhvien_Ngaysinh ="NgaySinh";
public static String TB_Sinhvien_Gioitinh ="GioiTinh";
public static String TB_Sinhvien_Diachi ="DiaChi";
    public static String TB_Sinhvien_Malop ="MaLop";

    public static String TB_Monhoc_Mamh ="MaMH";
    public static String TB_Monhoc_TenMh ="TenMH";
    public static String TB_Monhoc_Sotinchi ="SoTinChi";

    public static String TB_Giangvien_MaGv ="MaGV";
    public static String TB_Giangvien_Hoten ="HoTen";
    public static String TB_Giangvien_Bomon ="BoMon";

    public static String TB_Lopmonhoc_MalopMh ="MaLopMH";
    public static String TB_Lopmonhoc_Malop ="MaLop";
    public static String TB_Lopmonhoc_MaMh ="MaMH";
    public static String TB_Lopmonhoc_MaGv ="MaGV";

    public static String TB_Diem_MaSv  ="MaSV";
    public static String TB_Diem_MaLopMh ="MaLopMH";
    public static String TB_Diem_DiemTp ="DiemTP";
    public static String TB_Diem_Diemthi ="DiemThi";
    public static String TB_Diem_Diemtongket ="DiemTongKet";

    public static String TB_NguoiDung_Userid ="UserID";
    public static String TB_Nguoidung_Username ="Username";
    public static String TB_Nguoidung_Password ="Password";
    public static String TB_Nguoidung_Role ="Role";
    public static String TB_Nguoidung_MsSV ="MaSV";
    public static String TB_Nguoidung_MaGV ="MaGV";







    public CreateDatabase(@Nullable Context context) {
        super(context,"QLSV", null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Nganh (" +
                "MaNganh TEXT PRIMARY KEY," +
                "TenNganh TEXT NOT NULL)");

       
        db.execSQL("CREATE TABLE Lop (" +
                "MaLop TEXT PRIMARY KEY," +
                "TenLop TEXT NOT NULL," +
                "MaNganh TEXT," +
                "FOREIGN KEY (MaNganh) REFERENCES Nganh(MaNganh))");


        db.execSQL("CREATE TABLE SinhVien (" +
                "MaSV TEXT PRIMARY KEY," +
                "HoTen TEXT NOT NULL," +
                "NgaySinh TEXT," +
                "GioiTinh TEXT," +
                "DiaChi TEXT," +
                "MaLop TEXT," +
                "FOREIGN KEY (MaLop) REFERENCES Lop(MaLop))");


        db.execSQL("CREATE TABLE MonHoc (" +
                "MaMH TEXT PRIMARY KEY," +
                "TenMH TEXT NOT NULL," +
                "SoTinChi INTEGER NOT NULL)");


        db.execSQL("CREATE TABLE GiangVien (" +
                "MaGV TEXT PRIMARY KEY," +
                "HoTen TEXT NOT NULL," +
                "BoMon TEXT)");


        db.execSQL("CREATE TABLE LopMonHoc (" +
                "MaLopMH TEXT PRIMARY KEY," +
                "MaLop TEXT," +
                "MaMH TEXT," +
                "MaGV TEXT," +
                "FOREIGN KEY (MaLop) REFERENCES Lop(MaLop)," +
                "FOREIGN KEY (MaMH) REFERENCES MonHoc(MaMH)," +
                "FOREIGN KEY (MaGV) REFERENCES GiangVien(MaGV))");


        db.execSQL("CREATE TABLE Diem (" +
                "MaSV TEXT," +
                "MaLopMH TEXT," +
                "DiemTP REAL," +
                "DiemThi REAL," +
                "DiemTongKet REAL," +
                "PRIMARY KEY (MaSV, MaLopMH)," +
                "FOREIGN KEY (MaSV) REFERENCES SinhVien(MaSV)," +
                "FOREIGN KEY (MaLopMH) REFERENCES LopMonHoc(MaLopMH))");


            db.execSQL("CREATE TABLE NguoiDung (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL UNIQUE," +
                "Password TEXT NOT NULL," +
                "Role TEXT CHECK(Role IN ('Admin','SinhVien','GiangVien')) NOT NULL," +
                "MaSV TEXT," +
                "MaGV TEXT," +
                "FOREIGN KEY (MaSV) REFERENCES SinhVien(MaSV)," +
                "FOREIGN KEY (MaGV) REFERENCES GiangVien(MaGV))");
        db.execSQL("INSERT INTO Nganh (MaNganh, TenNganh) VALUES ('CNTT', 'Công nghệ thông tin')");


        db.execSQL("INSERT INTO Lop (MaLop, TenLop, MaNganh) VALUES ('L01', 'Lớp CNTT1', 'CNTT')");


        db.execSQL("INSERT INTO SinhVien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop) " +
                "VALUES ('SV001', 'Nguyen Van A', '2003-05-12', 'Nam', 'Hà Nội', 'L01')");


        db.execSQL("INSERT INTO GiangVien (MaGV, HoTen, BoMon) " +
                "VALUES ('GV001', 'Tran Thi B', 'Cơ sở dữ liệu')");


        db.execSQL("INSERT INTO MonHoc (MaMH, TenMH, SoTinChi) " +
                "VALUES ('MH001', 'Cơ sở dữ liệu', 3)");


        db.execSQL("INSERT INTO LopMonHoc (MaLopMH, MaLop, MaMH, MaGV) " +
                "VALUES ('LMH001', 'L01', 'MH001', 'GV001')");

        db.execSQL("INSERT INTO Diem (MaSV, MaLopMH, DiemTP, DiemThi, DiemTongKet) " +
                "VALUES ('SV001', 'LMH001', 8.0, 7.5, 7.7)");




        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role) " +
                "VALUES ('admin', '123456', 'Admin')");


        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role, MaSV) " +
                "VALUES ('sv001', '123456', 'SinhVien', 'SV001')");


        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role, MaGV) " +
                "VALUES ('gv001', '123456', 'GiangVien', 'GV001')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ (nếu cần nâng cấp DB)
        db.execSQL("DROP TABLE IF EXISTS Diem");
        db.execSQL("DROP TABLE IF EXISTS LopMonHoc");
        db.execSQL("DROP TABLE IF EXISTS SinhVien");
        db.execSQL("DROP TABLE IF EXISTS Lop");
        db.execSQL("DROP TABLE IF EXISTS Nganh");
        db.execSQL("DROP TABLE IF EXISTS MonHoc");
        db.execSQL("DROP TABLE IF EXISTS GiangVien");
        db.execSQL("DROP TABLE IF EXISTS NguoiDung");
        onCreate(db);
    }
public SQLiteDatabase open(){
        return this.getWritableDatabase();
}


}
