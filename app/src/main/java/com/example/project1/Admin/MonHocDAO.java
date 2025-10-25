package com.example.project1.Admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import utt.cntt.httt.project2.database.DatabaseHelper;


public class MonHocDAO {
    private DatabaseHelper dbHelper;

    public MonHocDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ==================== THÊM MÔN HỌC ====================
    public boolean insert(com.example.project1.Admin.MonHoc monHoc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaMH", monHoc.getMaMH());
        values.put("TenMH", monHoc.getTenMH());
        values.put("SoTinChi", monHoc.getSoTinChi());
        long result = db.insert("MonHoc", null, values);
        db.close();
        return result != -1;
    }

    // ==================== CẬP NHẬT MÔN HỌC ====================
    // Chỉ cập nhật tên và tín chỉ, giữ nguyên mã môn học
    public boolean update(com.example.project1.Admin.MonHoc monHoc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenMH", monHoc.getTenMH());
        values.put("SoTinChi", monHoc.getSoTinChi());
        int result = db.update("MonHoc", values, "MaMH = ?", new String[]{monHoc.getMaMH()});
        db.close();
        return result > 0;
    }

    // Cập nhật môn học khi đổi mã môn học (MaMH có thể thay đổi)
    public boolean updateWithNewId(String oldMaMH, com.example.project1.Admin.MonHoc newMonHoc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaMH", newMonHoc.getMaMH());
        values.put("TenMH", newMonHoc.getTenMH());
        values.put("SoTinChi", newMonHoc.getSoTinChi());
        int result = db.update("MonHoc", values, "MaMH = ?", new String[]{oldMaMH});
        db.close();
        return result > 0;
    }

    // ==================== XÓA MÔN HỌC ====================
    public boolean delete(String maMH) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("MonHoc", "MaMH = ?", new String[]{maMH});
        db.close();
        return result > 0;
    }

    // ==================== LẤY DANH SÁCH MÔN HỌC ====================
    public List<com.example.project1.Admin.MonHoc> getAll() {
        List<com.example.project1.Admin.MonHoc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MonHoc", null);
        if (cursor.moveToFirst()) {
            do {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int tinchi = cursor.getInt(2);
                list.add(new com.example.project1.Admin.MonHoc(ma, ten, tinchi));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // ==================== KIỂM TRA MÃ MÔN HỌC ====================
    // Kiểm tra xem mã môn học đã tồn tại chưa
    public boolean exists(String maMH) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaMH FROM MonHoc WHERE MaMH = ?", new String[]{maMH});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ==================== TÌM MÔN HỌC THEO MÃ ====================
    public com.example.project1.Admin.MonHoc getById(String maMH) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MonHoc WHERE MaMH = ?", new String[]{maMH});
        com.example.project1.Admin.MonHoc monHoc = null;
        if (cursor.moveToFirst()) {
            monHoc = new com.example.project1.Admin.MonHoc(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2)
            );
        }
        cursor.close();
        db.close();
        return monHoc;
    }
}
