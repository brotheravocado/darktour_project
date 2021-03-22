package com.example.darktour_project;

// 상세페이지 - 윤지
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class DetailPage extends AppCompatActivity {
    int i = 0;
    ImageView weatherview = null;
    TextView weatherstate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        weatherview = (ImageView)findViewById(R.id.weather); // weather 사진
        weatherstate = (TextView)findViewById(R.id.weather_state); // weather 상태
        
        // 상태에 맞는 weather 이미지 출력
        if (i == 0){
            weatherview.setImageResource(R.drawable.sunny);
            //상태

        }
        else if(i == 1){
            
        }
        else if(i == 2){
            
        }
        else if(i == 3){
            weatherview.setImageResource(R.drawable.rainy);
        }
        else{
            //error image
        }
        
    }
 // weather0 - 맑음
 // weather1 - 흐림
 // weather2 - 눈
 // weather3 - 비

}