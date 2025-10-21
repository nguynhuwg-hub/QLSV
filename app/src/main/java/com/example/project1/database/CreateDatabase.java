package com.example.project1.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;
import androidx.annotation.Nullable;

import com.example.project1.Giangvien.SinhVien;

import java.util.ArrayList;

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
        super(context,"QLSV", null,10);
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
        db.execSQL("INSERT INTO Lop (MaLop, TenLop, MaNganh) VALUES ('L01', 'Lớp CNTT1', 'CNTT')");
        db.execSQL("INSERT INTO Lop VALUES ('74DCTT23','CNTT','TT')");
        db.execSQL("INSERT INTO Lop VALUES ('74DCUT23','KTOT','OT')");
        db.execSQL("INSERT INTO Lop VALUES ('74DCDQ23','CNDQ','DQ')");

        db.execSQL("CREATE TABLE SinhVien (" +
                "MaSV TEXT PRIMARY KEY," +
                "HoTen TEXT NOT NULL," +
                "NgaySinh TEXT," +
                "GioiTinh TEXT," +
                "DiaChi TEXT," +
                "MaLop TEXT," +
                "FOREIGN KEY (MaLop) REFERENCES Lop(MaLop))");
        db.execSQL("INSERT INTO SinhVien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop) " +
                "VALUES ('SV001', 'Nguyen Van A', '2003-05-12', 'Nam', 'Hà Nội', 'L01')");

        db.execSQL("INSERT INTO SinhVien (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop) " +
                "VALUES ('SV002', 'Nguyen Van B', '2003-05-12', 'Nam', 'Hà Nội', 'L01')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22507','Nguyen Gia Huy','26-12-2005','Nam','Ha Noi','74DCTT23')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22506','Nguyen Quoc Hung','21-8-2005','Nam','Son La','74DCTT23')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22505','Pham Cong Minh','2-2-2005','Nam','Ninh Binh','74DCTT23')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22504','Pham Tung Duong','6-1-2005','Nam','Ninh Binh','74DCTT23')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22503','Do Quang Danh','6-2-2005','Nam','Thai Binh','74DCTT23')");
        db.execSQL("INSERT INTO SinhVien VALUES('74DCTT22501','Nguyen Van A','01-01-2005','Nam','Ha Noi','74DCTT23')");
        db.execSQL("CREATE TABLE MonHoc (" +
                "MaMH TEXT PRIMARY KEY," +
                "TenMH TEXT NOT NULL," +
                "SoTinChi INTEGER NOT NULL)");

        db.execSQL("INSERT INTO MonHoc VALUES ('MH002','Lập trình Java',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH003','Cấu trúc dữ liệu',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH004','Hệ điều hành',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH005','Mạng máy tính',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH006','An toàn thông tin',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH007','Trí tuệ nhân tạo',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH008','Học máy',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH009','Phân tích dữ liệu',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH010','Lập trình Web',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH011','Cơ sở lập trình',3)");
        db.execSQL("INSERT INTO MonHoc VALUES ('MH012','CNTT',3)");
        db.execSQL("INSERT INTO MonHoc (MaMH, TenMH, SoTinChi) " +
                "VALUES ('MH001', 'Cơ sở dữ liệu', 3)");

        db.execSQL("CREATE TABLE GiangVien (" +
                "MaGV TEXT PRIMARY KEY," +
                "HoTen TEXT NOT NULL," +
                "BoMon TEXT)");
        db.execSQL("INSERT INTO GiangVien (MaGV, HoTen, BoMon) " +
                "VALUES ('GV001', 'Tran Thi B', 'Cơ sở dữ liệu')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV01','Nguyen Van A','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV02','Tran Thi B','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV03','Le Van C','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV04','Pham Thi D','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV05','Hoang Van E','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV06','Nguyen Van F','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV07','Tran Thi G','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV08','Le Van H','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV09','Pham Thi I','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV10','Hoang Van J','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV11','Nguyen Van K','CNTT')");
        db.execSQL("INSERT INTO GiangVien VALUES ('GV12','Tran Thi L','CNTT')");


        db.execSQL("CREATE TABLE LopMonHoc (" +
                "MaLopMH TEXT PRIMARY KEY," +
                "MaLop TEXT," +
                "MaMH TEXT," +
                "MaGV TEXT," +
                "FOREIGN KEY (MaLop) REFERENCES Lop(MaLop)," +
                "FOREIGN KEY (MaMH) REFERENCES MonHoc(MaMH)," +
                "FOREIGN KEY (MaGV) REFERENCES GiangVien(MaGV))");
        db.execSQL("INSERT INTO LopMonHoc (MaLopMH, MaLop, MaMH, MaGV) " +
                "VALUES ('LMH001', 'L01', 'MH001', 'GV001')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH01','74DCTT23','MH001','GV01')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH02','74DCTT23','MH002','GV02')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH03','74DCTT23','MH003','GV03')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH04','74DCTT23','MH004','GV04')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH05','74DCTT23','MH005','GV05')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH06','74DCTT23','MH006','GV06')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH07','74DCTT23','MH007','GV07')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH08','74DCTT23','MH008','GV08')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH09','74DCTT23','MH009','GV09')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH10','74DCTT23','MH010','GV10')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH11','74DCTT23','MH011','GV11')");
        db.execSQL("INSERT INTO LopMonHoc VALUES ('LMH12','74DCTT23','MH012','GV12')");

        db.execSQL("CREATE TABLE Diem (" +
                "MaSV TEXT," +
                "MaLopMH TEXT,"+
                "DiemQT REAL," +
                "DiemGK REAL," +
                "DiemCK REAL," +
                "DiemTK REAL,"+
                "TrangThai TEXT,"+
                "PRIMARY KEY (MaSV,MaLopMH),"+
                "FOREIGN KEY (MaLopMH) REFERENCES LopMonHoc(MaLopMH),"+
                "FOREIGN KEY (MaSV) REFERENCES SinhVien(MaSV))");


        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22501','LMH01',8,7,9,8.2,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22502','LMH02',6,7,7,6.8,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22503','LMH03',9,8,9,8.7,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22504','LMH04',7,8,8,7.8,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22505','LMH05',5,6,6,5.8,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22506','LMH06',8,9,9,8.9,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22507','LMH07',7,7,8,7.4,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22508','LMH08',6,6,7,6.4,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22509','LMH09',9,9,10,9.4,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22510','LMH10',4,5,6,5.0,'Không đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22511','LMH11',7,8,9,8.0,'Đạt')");
        db.execSQL("INSERT INTO Diem VALUES ('74DCTT22512','LMH12',6,6,6,6.0,'Đạt')");
            db.execSQL("CREATE TABLE NguoiDung (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL UNIQUE," +
                "Password TEXT NOT NULL," +
                "Role TEXT CHECK(Role IN ('Admin','SinhVien','GiangVien')) NOT NULL," +
                "MaSV TEXT,"  +
                "MaGV TEXT," +
                "FOREIGN KEY (MaSV) REFERENCES SinhVien(MaSV)," +
                "FOREIGN KEY (MaGV) REFERENCES GiangVien(MaGV))");
        db.execSQL("INSERT INTO Nganh (MaNganh, TenNganh) VALUES ('CNTT', 'Công nghệ thông tin')");



        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role) " +
                "VALUES ('admin', '123456', 'Admin')");


        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role, MaSV) " +
                "VALUES ('sv001', '123456', 'SinhVien', 'SV001')");

        db.execSQL("INSERT INTO NguoiDung (Username, Password, Role, MaSV) " +
                "VALUES ('sv002', '123456', 'SinhVien', 'SV002')");


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
    public ArrayList<String> getAllLop() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaLop FROM Lop", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    // Lấy sinh viên theo mã lớp
    public ArrayList<SinhVien> getSinhVienByLop(String maLop) {
        ArrayList<SinhVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaSV, HoTen, MaLop FROM SinhVien WHERE MaLop = ?", new String[]{maLop});
        while (c.moveToNext()) {
            String maSV = c.getString(c.getColumnIndexOrThrow("MaSV"));
            String hoTen = c.getString(c.getColumnIndexOrThrow("HoTen"));
            String lop = c.getString(c.getColumnIndexOrThrow("MaLop"));
            list.add(new SinhVien(maSV, hoTen, lop));
        }
        c.close();
        return list;
    }
public SQLiteDatabase open(){
        return this.getWritableDatabase();
}


}
