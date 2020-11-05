package com.example.creditcalculator;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class CompleteSubActivity extends AppCompatActivity {
    private PrivSubOpenHelper phelper;
    private StudentOpenHelper shelper;
    private SQLiteOpenHelper tshelper;

    private SQLiteDatabase priv_subdb;
    private SQLiteDatabase total_subdb;
    private SQLiteDatabase stud_infodb;
    private Cursor tc;
    private InfoMemory info;
    private File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/noname.txt");
    public TextFileReader tfr = new TextFileReader();

    //핵심교양 들었는지 체크
    private boolean hec1 = false;
    private boolean hec2 = false;
    private boolean hec3 = false;
    private boolean hec4 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sub);

        // 191212 수정했음 내 정보 확인하는 액티비티로 이동
//        findViewById(R.id.checkinfo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CompleteSubActivity.this, SelfInfoActivity.class);
//                startActivity(intent);
//            }
//        });
        // 여기까지~

        tshelper = new DBHelper(this);
        phelper = new PrivSubOpenHelper(this);
        shelper = new StudentOpenHelper(this);

        SQLiteDatabase database = phelper.getReadableDatabase();
        Cursor c = database.rawQuery("select * from priv_subject;", null, null);
        if (c.getCount() == 0) {
            setSubjectDatas();
        }
        info = new InfoMemory();
        setSubjectFinished(); // 내가 들은 과목을 텍스트파일에서 읽어와 이수한 과목은 이수현황에 True 체크
        setCredit();
    }



    public void onClick(View v) {
        if (v.getId() == R.id.comp_list_view) {
            Intent intent = new Intent(this, CompletedSubListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.reset_db) {
            setSubjectFinished();
        } else if (v.getId() == R.id.checkinfo) {
            Intent intent = new Intent(CompleteSubActivity.this, SelfInfoActivity.class);
            startActivity(intent);
        }
    }

    public boolean isMasterEmpty() {
        boolean flag;
        String quString = "select exists(select 1 from priv_subject);";

        SQLiteDatabase db = phelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count == 1) {
            flag = false;
        } else {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }

    public void setSubjectDatas() {
        total_subdb = tshelper.getReadableDatabase();
        priv_subdb = phelper.getWritableDatabase();
        stud_infodb = shelper.getReadableDatabase();
        // 자신이 속한 전공과 동일한 전공에 대해서만 과목 전부 가져오기 (이수현황은 아직 전부 false인 상태)
        Cursor tmp = stud_infodb.rawQuery("select major, dmajor, minor from student;", null);
        tmp.moveToNext();
        String major = tmp.getString(tmp.getColumnIndex("major"));
        String dmajor = tmp.getString(tmp.getColumnIndex("dmajor"));
        String minor = tmp.getString(tmp.getColumnIndex("minor"));
        int teaching = tmp.getColumnIndex("teaching");

        tc = total_subdb.rawQuery("select * from subjects where majorname in (?, ?, ?);", new String[]{major, dmajor, minor});
        while (tc.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("major", tc.getString(tc.getColumnIndex("majorname")));
            values.put("field", tc.getString(tc.getColumnIndex("field")));
            values.put("subname", tc.getString(tc.getColumnIndex("subname")));
            values.put("subcode", tc.getString(tc.getColumnIndex("subcode")));
            values.put("credit", tc.getLong(tc.getColumnIndex("credit")));
            values.put("required", tc.getInt(tc.getColumnIndex("required")));

            priv_subdb.insert("priv_subject", null, values);
        }


        tc = total_subdb.rawQuery("select * from subjects where field in (?, ?, ?, ?,?);", new String[]{"핵심교양 1영역", "핵심교양 2영역", "핵심교양 3영역", "핵심교양 4영역", "공통교양"});
        tc.moveToFirst();
        while (tc.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("major", tc.getString(tc.getColumnIndex("majorname")));
            values.put("field", tc.getString(tc.getColumnIndex("field")));
            values.put("subname", tc.getString(tc.getColumnIndex("subname")));
            values.put("subcode", tc.getString(tc.getColumnIndex("subcode")));
            values.put("credit", tc.getLong(tc.getColumnIndex("credit")));
            values.put("required", tc.getInt(tc.getColumnIndex("required")));

            priv_subdb.insert("priv_subject", null, values);
        }

        if (teaching == 1) {
            //교직 하는곳
        }



        tc.close();
        priv_subdb.close();
        total_subdb.close();
        stud_infodb.close();
    }

    public void setSubjectFinished() {
        priv_subdb = phelper.getReadableDatabase();

        Log.e("external path : ", path.toString());


        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                updateDB(path);
            } catch (Exception e) {
                Log.e("IOException Occur:", "cannot read file");
            }
        } else {
            Toast.makeText(this, "파일 읽기 권한 요청을 확인해야만 데이터를 읽어올 수 있습니다.", Toast.LENGTH_LONG).show();
        }
        priv_subdb.close();
    }

    private void updateDB(File path) throws IOException {
        total_subdb = tshelper.getReadableDatabase();
        priv_subdb = phelper.getWritableDatabase();

        info = tfr.readDataFromFile(path); //성적이랑 학점 코드 다 있음
        Cursor c = priv_subdb.rawQuery("select * from priv_subject;", null);
        if (path.exists()) {
            tc = total_subdb.rawQuery("select * from subjects where field='일반교양'",null);
            tc.moveToFirst();
            while (tc.moveToNext()) {
                int i=0;
                for (String code : info.getSubNo()) {
                    if (tc.getString(tc.getColumnIndex("subcode")).equals(code)) {

                        ContentValues values = new ContentValues();
                        values.put("major", tc.getString(tc.getColumnIndex("majorname")));
                        values.put("field", tc.getString(tc.getColumnIndex("field")));
                        values.put("subname", tc.getString(tc.getColumnIndex("subname")));
                        values.put("subcode", tc.getString(tc.getColumnIndex("subcode")));
                        values.put("credit", tc.getLong(tc.getColumnIndex("credit")));
                        values.put("required", tc.getInt(tc.getColumnIndex("required")));
                        values.put("finished",1);
                        values.put("grade", info.getGrade()[i]);
                        priv_subdb.insert("priv_subject", null, values);
                        Log.e("IOException Occur:", "can");
                    }
                    i++;
                }
            }

            while (c.moveToNext()) {
                for (String code : info.getSubNo()) {
                    if (c.getString(c.getColumnIndex("subcode")).equals(code)) {
                        String sql = "update priv_subject set finished = 1 where subcode = \"" + code + "\";";
                        priv_subdb.execSQL(sql);
                        Log.e("IOException Occur:", "can");
                    }
                }
            }
            Toast.makeText(this, "데이터베이스 설정 완료", Toast.LENGTH_LONG).show();
            c.close();


        } else
            Toast.makeText(this, "포탈의 \"개인성적조회\"란에서 텍스트파일을 다운로드 받은 뒤 다시 실행해 주세요.", Toast.LENGTH_LONG).show();
    }

    public void setCredit(){
        total_subdb = tshelper.getReadableDatabase();
        priv_subdb = phelper.getWritableDatabase();
        stud_infodb = shelper.getReadableDatabase();

        Cursor tmp = stud_infodb.rawQuery("select major, dmajor, minor from student;", null);
        tmp.moveToNext();
        String major = tmp.getString(tmp.getColumnIndex("major"));
        String dmajor = tmp.getString(tmp.getColumnIndex("dmajor"));
        String minor = tmp.getString(tmp.getColumnIndex("minor"));
        tmp.close();

        int base_major = 0;
        int deepen_major=0;
        tmp = priv_subdb.rawQuery("select major, field, credit, finished from priv_subject;", null);
        while (tmp.moveToNext()) {
            if (tmp.getString(tmp.getColumnIndex("major")).equals(major)) {
                if (tmp.getString(tmp.getColumnIndex("field")).equals("핵심전공")) {
                    if (tmp.getInt(tmp.getColumnIndex("finished")) == 1) {
                        base_major += tmp.getInt(tmp.getColumnIndex("credit"));
                    }
                }
                else if (tmp.getString(tmp.getColumnIndex("field")).equals("심화전공")) {
                    if (tmp.getInt(tmp.getColumnIndex("finished")) == 1) {
                        deepen_major += tmp.getInt(tmp.getColumnIndex("credit"));
                    }
                }
            }
        }

        //여기서부터 수정해야해야함( 진행상태를 알려주는 곳 )
        TextView base_major_credit = findViewById(R.id.base_major_credit);
        TextView deepen_major_credit = findViewById(R.id.deepen_major_credit);

        base_major_credit.setText(base_major+"/"+"39");
        deepen_major_credit.setText(deepen_major+"/"+"33");

        TextView table_major = findViewById(R.id.tabel_major);
        int total_major_credit = base_major + deepen_major;
        table_major.setText(total_major_credit+"");


        ProgressBar p1 = findViewById(R.id.p1);
        p1.setMax(39);
        p1.setProgress(base_major);

        ProgressBar p2 = findViewById(R.id.p2);
        p2.setMax(33);
        p2.setProgress(deepen_major);
    }

}
