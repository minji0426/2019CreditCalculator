package com.example.creditcalculator;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class DBHelper extends SQLiteOpenHelper {  //새로 생성한 adapter 속성은 SQLiteOpenHelper이다.
    public static final String DB_NAME = "subject_total.db"; //기본, 전체 과목 데이터 저장 db
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);    // db명과 버전만 정의 한다.
        DataBaseSetter.setDB(context, DB_NAME);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
