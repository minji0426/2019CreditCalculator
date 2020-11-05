package com.example.creditcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InfoInputActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private ArrayAdapter sa;
    private ArrayAdapter sa1;
    private ArrayAdapter na;

    private StudentOpenHelper shelper;
    private String selected_major;
    private int adv_major;
    private int teaching;
    private String selected_dmajor;
    private String selected_minor;
    private String studnumber;

    private DBHelper helper;
    private List<String> subjectlists; //과목 리스트
    private List<String> subjectlists1; //과목 리스트
    private List<String> numbers;
    private Spinner select_major;
    private Spinner select_dmajor;
    private Spinner select_minor;
    private Spinner stud_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_input);

        helper = new DBHelper(this);
        subjectlists = new ArrayList<>();
        subjectlists1 = new ArrayList<>();
        numbers = new ArrayList<>();

        for (int i = 2014; i < 2020; i++) {
            numbers.add(i + "");
        }

        getAllDatas(); //데이터 베이스 열어서 subjectlists에 학과명 넣어주는 함수

        // setting views
        sa1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectlists1);
        sa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectlists);
        na = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);

        select_major = findViewById(R.id.select_major); //주전공_Spinner
        select_major.setAdapter(sa1);
        select_major.setSelection(0, false);

        subjectlists.add(0,"해당사항 없음");
        sa.notifyDataSetChanged();

        select_dmajor = findViewById(R.id.select_double_major); //복수전공_Spinner
        select_dmajor.setAdapter(sa);
        select_dmajor.setSelection(0, false);

        select_minor = findViewById(R.id.select_minor); //부전공_Spinner
        select_minor.setAdapter(sa);
        select_minor.setSelection(0, false);

        stud_no = findViewById(R.id.select_studno); // 학번_Spinner
        stud_no.setAdapter(na);
        stud_no.setSelection(0, false);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.set_settings) { //설정 버튼을 누르면

            // database create & set student data
            shelper = new StudentOpenHelper(this);
            SQLiteDatabase sdb = shelper.getWritableDatabase();

            selected_major = select_major.getSelectedItem().toString();
            selected_dmajor = select_dmajor.getSelectedItem().toString();
            selected_minor = select_minor.getSelectedItem().toString();
            adv_major = ((CheckBox)findViewById(R.id.advanced_major)).isChecked() ? 1 : 0;
            teaching = ((CheckBox)findViewById(R.id.teaching)).isChecked() ? 1 : 0;
            studnumber = stud_no.getSelectedItem().toString();

            ContentValues values = new ContentValues();
            values.put("major", selected_major);
            values.put("adv_major", adv_major);
            values.put("teaching", teaching);
            if (!selected_dmajor.equals("해당사항 없음")){
                values.put("dmajor", selected_dmajor);
            }
            else values.put("dmajor", "none");
            if (!selected_minor.equals("해당사항 없음")) {
                values.put("minor", selected_minor);
            }
            else values.put("minor", "none");
            values.put("std_no",studnumber);
            sdb.insert("student", null, values);

            Cursor tmpc = sdb.query("student", new String[]{
                            "major", "adv_major", "teaching", "dmajor", "minor","std_no"},
                    null, null, null, null, null);

            while (tmpc.moveToNext()) {
                Toast.makeText(this, "" + tmpc.getString(0) + tmpc.getInt(1) +
                        tmpc.getInt(2) + tmpc.getString(3) + tmpc.getString(4), Toast.LENGTH_LONG).show();
            }
            sa.notifyDataSetChanged();
            sdb.close();

            Intent intent = new Intent(this, CompleteSubActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void getAllDatas() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select distinct MAJORNAME from subjects;",
                null);

        while (c.moveToNext()) {
            String s = c.getString(0);
            if (s.equals("교양학부") || s.equals("학생군사교육단") || s.equals("경력개발팀") || s.equals("국제학생지원팀") || s.equals("창업교육센터") ||
                    s.equals("학생창업교육팀") || s.equals("인재개발팀") || s.equals("미술대학") || s.equals("인문과학대학") || s.equals("법과대학") ||
                    s.equals("뷰티 생활산업국제대학") || s.equals("사범대학")|| s.equals("생명과학·화학부")|| s.equals("IT학부")|| s.equals("학사지원팀") ||
                    s.equals("융합문화예술대학") || s.equals("생활과학대학")) {
                continue;
            }
            subjectlists.add(s);
            subjectlists1.add(s);
            Log.e("qweqweqwe", s);
        }

        c.close();
        helper.close();
    }
}
