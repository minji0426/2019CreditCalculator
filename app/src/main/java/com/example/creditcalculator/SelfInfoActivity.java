package com.example.creditcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelfInfoActivity extends AppCompatActivity { //전화연결하는곳!! + 내정보 확인
    private String major_name;
    private String double_major_name;
    private String minor_name;
    private StudentOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info);

        helper = new StudentOpenHelper(this);
        db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from student;", null);
        c.moveToFirst();
        TextView mtv = findViewById(R.id.info_major_name); //주전공 이름
        major_name = c.getString(c.getColumnIndex("major"));
        mtv.setText(major_name);

        TextView dmtv = findViewById(R.id.info_dmajor_name);
        double_major_name = c.getString(c.getColumnIndex("dmajor"));
        if (!double_major_name.equals("none")) dmtv.setText(double_major_name);
        else dmtv.setText("없음");

        TextView mintv = findViewById(R.id.info_minor_name);
        minor_name = c.getString(c.getColumnIndex("minor"));
        if (!minor_name.equals("none")) mintv.setText(minor_name);
        else mintv.setText("없음");
        //없으면 아예안보이게 만들자!

        findViewById(R.id.m_dial).setOnClickListener(dialListener);
        findViewById(R.id.dm_dial).setOnClickListener(dialListener);
        findViewById(R.id.min_dial).setOnClickListener(dialListener);
    }
    public void onClick(View v) {
        if (v.getId() == R.id.bnt_modify) {
            Intent intent = new Intent(this, SelfInfoModifyActivity.class);
            startActivity(intent);
        }
    }
    View.OnClickListener dialListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setDials();
            String number = "0";
            switch (view.getId()) {
                case R.id.m_dial:
                    number = dials.get(major_name);
                    break;
                case R.id.dm_dial:
                    number = dials.get(double_major_name);
                    break;
                case R.id.min_dial:
                    number = dials.get(minor_name);
            }

            Uri uri= Uri.parse("tel:"+number); //전화와 관련된 Data는 'Tel:'으로 시작. 이후는 전화번호
            Intent i= new Intent(Intent.ACTION_DIAL,uri); //시스템 액티비티인 Dial Activity의 action값
            startActivity(i);//액티비티 실행


            // 여기서 permission 받아서 전화걸어야 합니다 퍼미션 받는것도 없네요.. request이전에 manifest에서
            // uses-permission하는것 잊지마세요 아마 CALL_DIAL이었나 뭐였나 그럴텐데 모소 교재를 찾아보십시오
            // 사랑해 지연아♥
        }
    };

    private Map<String, String> dials = new HashMap<>();


    public void setDials() {
        dials.put("국어국문학과", "029207069");
        dials.put("영여영문학과", "029207077");
        dials.put("독어독문학과", "029207084");
        dials.put("불어불문학과", "029207090");
        dials.put("일어일문학과", "029207096");
        dials.put("중어중문학과", "029207101");
        dials.put("사학과", "029207106");
        dials.put("정치외교학과", "029207127");
        dials.put("심리학과", "029207132");
        dials.put("지리학과", "029207137");

        dials.put("경제학과", "029207143");
        dials.put("경영학과", "029207148");
        dials.put("미디어커뮤니케이션학과", "029207805");
        dials.put("융합보안학과", "029207938");
        dials.put("법학과", "029207122");
        dials.put("수학과", "029207159");
        dials.put("통계학과", "029207180");
        dials.put("생명과학·화학부", "029207169");
        dials.put("생명과학", "029207169");

        dials.put("IT학부", "029207151");
        dials.put("컴퓨터소프트웨어", "029207151");
        dials.put("정보미디어", "029207151");
        dials.put("청정융합과학과", "029207922");

        dials.put("간호학과", " 029207720");
        dials.put("글로벌의과학과", "029207705");
        dials.put("교육학과", "029207720");
        dials.put("사회교육과", "029207221");
        dials.put("윤리교육과", "029207226");
        dials.put("한문교육과", "029207230");
        dials.put("유아교육과", "029207234");

        dials.put("미술대학", "029207241");
        dials.put("동양화과", "029207242");
        dials.put("서양화과", "029207248");
        dials.put("조소과", "029207259");
        dials.put("공예과", "029207259");
        dials.put("산업디자인과", "029207269");
        dials.put("성악과", "029207277");
        dials.put("기악과", "029207277");
        dials.put("작곡과", "029207277");

        dials.put("융합문화예술대학", "029207780");
        dials.put("문화예술경영학과", "029207820");
        dials.put("미디어영상연기학과", "029207825");
        dials.put("현대실용음악학과", "029207825");
        dials.put("무용예술학과", "029207835");
        dials.put("메이크업디자인학과", "029207845");
        dials.put("생활과학대학", "029207194");
        dials.put("의류학과", "029207195");
        dials.put("식품영양학과", "029207200");
        dials.put("생활문화소비자학과", "029207194");

        dials.put("사회복지학과", " 029207134");
        dials.put("스포츠레저학과", "029207835");
        dials.put("운동재활복지학과", "029207835");
        dials.put("인문과학대학", "029207068");
        dials.put("정보시스템", "029207151");
        dials.put("독일어문·문화학과", "029207084");
        dials.put("프랑스어문·문화학과", "029207090");
        dials.put("일본어문·문화학과", "029207096");
        dials.put("중국어문·문화학과", "029207101");
        dials.put("법과대학", "029207102");

        dials.put("지식산업법학과", "029207880");
        dials.put("화학과", "029207164");
        dials.put("컴퓨터공학과", "029207151");
        dials.put("정보시스템공학과", "029207151");
        dials.put("융합보안공학과", "029207938");
        dials.put("서비스·디자인공학과", "029207166");
        dials.put("바이오식품공학과", "029202697");
        dials.put("바이오생명공학과", "029207169");
        dials.put("청정융합에너지공학과", "029207101");
        dials.put("의류산업학과", "029207195");

        dials.put("뷰티산업학과", "029207845");
        dials.put("소비자생활문화산업학과", "029207194");
        dials.put("글로벌비즈니스학과", " 029207195");
        dials.put("뷰티 생활산업국제대학", "029202720");
        dials.put("사범대학", "029207533");
    }
}
