package com.example.creditcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CompletedSubListActivity extends AppCompatActivity {
    private PrivSubOpenHelper phelper;
    private StudentOpenHelper shelper;
    private SQLiteDatabase priv_subdb;
    private SQLiteDatabase stud_infodb;
    private Cursor tc;


    ListView base_major;
    ListView deepen_major;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_sub_list);
        phelper = new PrivSubOpenHelper(this);
        shelper = new StudentOpenHelper(this);

        priv_subdb = phelper.getReadableDatabase();
        stud_infodb = shelper.getReadableDatabase();

        List<String> list_base_major = new ArrayList<>();
        List<String> list_deepen_major = new ArrayList<>();

        Cursor tmp = stud_infodb.rawQuery("select major, dmajor, minor from student;", null);
        tmp.moveToNext();
        String major = tmp.getString(tmp.getColumnIndex("major"));
        String dmajor = tmp.getString(tmp.getColumnIndex("dmajor"));
        tmp.close();
//        String dmajor = tmp.getString(tmp.getColumnIndex("dmajor"));
//        String minor = tmp.getString(tmp.getColumnIndex("minor"));

        tmp = priv_subdb.rawQuery("select major, field, subname, finished from priv_subject;", null);
        while (tmp.moveToNext()) {
            if (tmp.getString(tmp.getColumnIndex("major")).equals(major)) {
                if (tmp.getString(tmp.getColumnIndex("field")).equals("핵심전공")) {
                    if (tmp.getInt(tmp.getColumnIndex("finished")) == 1) {
                        String s = tmp.getString(tmp.getColumnIndex("subname"));
                        list_base_major.add(s);
                        Log.e("aaaa", s);
                    }
                } else if (tmp.getString(tmp.getColumnIndex("field")).equals("심화전공")) {
                    if (tmp.getInt(tmp.getColumnIndex("finished")) == 1) {
                        String s = tmp.getString(tmp.getColumnIndex("subname"));
                        list_deepen_major.add(s);
                        Log.e("bbbb", s);
                    }
                }
            }
        }


        base_major = findViewById(R.id.list_base_major);
        deepen_major = findViewById(R.id.list_deepen_major);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list_base_major);
        base_major.setAdapter(adapter);
        getTotalHeightofListView(base_major);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list_deepen_major);
        deepen_major.setAdapter(adapter1);
        getTotalHeightofListView(deepen_major);
    }

    public static void getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void onClick(View view) {
        if (view.getId() == R.id.insert) {
            Intent intent = new Intent(this, AdditionalInfoActivity.class);
            startActivity(intent);
        }

    }
}

