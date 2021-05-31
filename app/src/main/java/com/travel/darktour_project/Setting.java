package com.travel.darktour_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class Setting extends AppCompatActivity {

    Intent intent;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ListView listview;
        SettingAdapter adapter;

        adapter = new SettingAdapter();

        listview = (ListView) findViewById(R.id.settinglist);
        listview.setAdapter(adapter);
        linearLayout = findViewById(R.id.setting_back);

        adapter.addItem("로그아웃") ;
        adapter.addItem("공지사항") ;
        adapter.addItem("관심유적지 변경") ;

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                SettingItem item = (SettingItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle();
                if (titleStr == "로그아웃") {
                    Toast.makeText(v.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            intent = new Intent(Setting.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else if (titleStr == "관심유적지 변경") {
                    Toast.makeText(v.getContext(), "유적지를 다시 선택해주세요!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Setting.this, Interest.class);
                    startActivity(intent);
                } else if (titleStr == "공지사항") {
                    //Toast.makeText(v.getContext(), "유적지를 다시 선택해주세요!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Setting.this, Notice.class);
                    startActivity(intent);
                }
            }
        }) ;
    }
}
