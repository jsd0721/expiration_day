package com.example.expiraton_date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MysqliteOpenHelper extends SQLiteOpenHelper {
    public MysqliteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table product" + "(_id integer primary key autoincrement," +
                "product_name TEXT," +
                "expiration_date date," +
                "/* usable_date date,*/ category TEXT, " +
                "save_place TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists student;";
        db.execSQL(sql);
        onCreate(db);
    }
}
