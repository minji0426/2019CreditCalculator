package com.example.creditcalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrivSubOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "priv_subject.db";
    public PrivSubOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //해당 주/복/부전에 따른 과목만 따로 저장. 이수여부 컬럼 따로 있음
        db.execSQL("create table if not exists priv_subject (_id integer primary key autoincrement," +
                "major text not null, field text not null, subname text not null, subcode text not null unique," +
                "credit integer not null, required integer default 0 not null check(required in (0, 1)), finished integer default 0 not null check(finished in (0, 1)), grade text default 0);");
        //create table if not exists priv_subject (_id integer primary key autoincrement, major text not null, field text not null, subname text not null, subcode text not null,
        //credit integer not null, required integer default 0 not null check(required in (0, 1)), finished integer default 0 not null check(finished in (0, 1)));

        ///priv_subject테이블을 없으면 만든다.  _id, major, field, subname, subcode, credit, required, finished
        ///required, finished ????
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE if exists priv_subject;");
        onCreate(db);
    }
}
