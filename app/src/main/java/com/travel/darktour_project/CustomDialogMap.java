package com.travel.darktour_project;

import android.app.Activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class CustomDialogMap extends AppCompatActivity {
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] x; // 경도
    String[] y; // 위도
    int[] start_finish_arr = new int[2]; // 시작 도착지 좌표
    TextView title;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.map_dialog);
        Intent intent = getIntent();
        titleNumArr=intent.getStringArrayExtra("title");

        title = (TextView)findViewById(R.id.title);

        title.setText(String.join("-", titleNumArr));
        title.setSelected(true);
        title.setSingleLine();
        title.setMarqueeRepeatLimit(-1);
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        Bundle bundle = new Bundle();
        x= new String[]{"126.96752","126.97257","126.99018","127.03535","126.956513"};
        //경도
        y= new String[]{"37.5686","37.50082","37.55915","37.52465","37.574022"};
        start_finish_arr[0] = 0;
        start_finish_arr[1] = titleNumArr.length -1;
        bundle.putStringArray("title",titleNumArr); // 유적지 이름
        bundle.putStringArray("x", x); // x
        bundle.putStringArray("y", y); // y
        bundle.putIntArray("start_finish_arr", start_finish_arr); // 출발지 도착지 array
        PublicFrag publicfrag = new PublicFrag();

        FragmentTransaction transaction ;

        transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.intercourse_map,publicfrag);

        publicfrag.setArguments(bundle);
        transaction.commit();


        Button btn1;
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn1 = findViewById(R.id.cancelButton);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
