package com.example.darktour_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

// 윤지 코스 만든 화면
public class MakeCourse extends AppCompatActivity {
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] contentNumArr; // 설명 저장 arr
    double[] x; // 경도
    double[] y; // 위도
    String location; // 지역
    int[] start_finish_arr; // 시작 도착지 좌표
    String transportation; // 대중교통
    CarFrag carfrag;
    PublicFrag publicfrag;
    RoadFrag roadfrag;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makecourse);
        Intent intent = getIntent(); // 데이터 수신
        titleNumArr = intent.getStringArrayExtra("title"); // title
        contentNumArr = intent.getStringArrayExtra("content"); // 설명
        x = intent.getDoubleArrayExtra("x"); // 경도
        y = intent.getDoubleArrayExtra("y"); // 위도
        location = intent.getStringExtra("location"); // 지역
        transportation = intent.getStringExtra("transportation"); // 대중교통
        start_finish_arr = intent.getIntArrayExtra("start_finish_arr"); // 시작 도착
        TextView location_name = findViewById(R.id.location);
        location_name.setText(location);
        carfrag = new CarFrag(); //프래그먼트 객채셍성
        publicfrag = new PublicFrag(); //프래그먼트 객채셍성
        roadfrag = new RoadFrag(); //프래그먼트 객채셍성

        
        switch (transportation){
            case "자동차":
                setFrag(0);
                break;
            case "대중교통":
                setFrag(1);
                break;
            case "도보":
                setFrag(2);
                break;
        }
        
    }
    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (n){
            case 0:
                transaction.replace(R.id.main_frame, carfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.main_frame, publicfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.main_frame, roadfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                transaction.commit();
                break;
        }
    }

    public void back_button_click(View v){
        super.onBackPressed();
    } // 뒤로가기
}
