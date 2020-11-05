package com.example.creditcalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "student.db";
    public StudentOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //학생의 주전 부전 복전 교직 전공심화 여부 저장 db 만들기
        db.execSQL("create table student (_id integer primary key autoincrement," +
                "major text not null, adv_major integer default 0, teaching integer default 0, " +
                "dmajor text, minor text, std_no text);");

        //create table student (_id integer primary key autoincrement,major text not null, adv_major integer default 0, teaching integer default 0, dmajor text, minor text);
        // _id, major, adv_major, teaching, dmajor, minor, std_no 로 테이블 만들음
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE if exists student;");
        onCreate(db);
    }
}
