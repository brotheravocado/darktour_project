package com.example.darktour_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// 좋아하는 유적지
public class FavoriteSite extends AppCompatActivity  {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_site);
        Intent intent = getIntent(); // 데이터 수신

        //ArrayList<SiteData> list = (ArrayList<SiteData>) intent.getSerializableExtra("select_data");


    }

}
