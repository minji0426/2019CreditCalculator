package com.example.creditcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //처음 인트로 화면 보여줌. 끝나면 mag=1을 가져옴.
        IntroThread it = new IntroThread(handler); //다음 화면을 선택
        it.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    //어떤 화면을 선택할지 보여줌.
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            File folder = new File("/data/data/" + MainActivity.this.getPackageName() + "/databases/student.db"); //폴더에 디비파일 경로 저장
            Intent intent;

            //처음 열리는 화면 결정.. 처음열면 설정화면을 설정을 하면 메인 화면을 띄워줌 -> 위에 파일이 있고 없고로 결정
            if (msg.what == 1) {
                if (folder.exists()) {
                    intent = new Intent(MainActivity.this, CompleteSubActivity.class);
                }
                else {
                    intent = new Intent(MainActivity.this, InfoInputActivity.class);
                }
                startActivity(intent);
            }
        }
    };
}
