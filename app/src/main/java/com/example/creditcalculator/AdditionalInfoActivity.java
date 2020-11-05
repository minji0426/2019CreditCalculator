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
import android.widget.EditText;
import android.widget.Spinner;

public class AdditionalInfoActivity extends AppCompatActivity { //추가정보 입력하는 액티비티

    Spinner select;
    EditText subject;
    EditText credit;

    String selected_select;
    String selected_subject;
    int selected_credit;

    SQLiteDatabase priv_subdb;
    PrivSubOpenHelper phelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        select = findViewById(R.id.select);
        subject = findViewById(R.id.subject);
        credit = findViewById(R.id.credit);

        String[] s = {"일반교양", "공통교양", "핵심교양 1영역", "핵심교양 2영역", "핵심교양 3영역", "핵심교양 4영역", "핵심전공", "심화전공"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, s);
        select.setAdapter(adapter);
        select.setSelection(0,false);

        phelper = new PrivSubOpenHelper(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.insert_bottom) {
            Log.e("qweqweqwe", "22222222222222222222");
            selected_select = select.getSelectedItem().toString();
            selected_subject = subject.getText().toString();
            selected_credit = Integer.parseInt(credit.getText().toString());
            Log.e("qweqweqwe", "333333333333333333333");
            priv_subdb = phelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("major", "추가입력");
            values.put("field", selected_select);
            values.put("subname", selected_subject);
            values.put("subcode", "0");
            values.put("credit", selected_credit);
            values.put("required", 0);
            values.put("finished", 1);
            priv_subdb.insert("priv_subject", null, values);
            Log.e("qweqweqwe", "1111111111111111111");

            subject.setText("");
            credit.setText("");

            //priv_subdb.close();
        }
    }
}
