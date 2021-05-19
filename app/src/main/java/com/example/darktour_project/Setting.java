package com.example.darktour_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ListView listview;
        SettingAdapter adapter;

        adapter = new SettingAdapter();

        listview = (ListView) findViewById(R.id.settinglist);
        listview.setAdapter(adapter);

        adapter.addItem("로그아웃") ;
        adapter.addItem("비밀번호 변경") ;
        adapter.addItem("공지사항") ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                SettingItem item = (SettingItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle();
            }
        }) ;
    }
}
