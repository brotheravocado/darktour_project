package com.example.darktour_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Notice extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        ListView listview;
        NoticeAdapter adapter;

        adapter = new NoticeAdapter();

        listview = (ListView) findViewById(R.id.noticelist);
        listview.setAdapter(adapter);

        adapter.addItem("1", "2102.05.49","무") ;
        adapter.addItem("2", "2102.13.25","야") ;
        adapter.addItem("3", "2108.01.99","호") ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                NoticeItem item = (NoticeItem) parent.getItemAtPosition(position);
                String contentsstr = item.getContents();
                Toast.makeText(v.getContext(), contentsstr, Toast.LENGTH_SHORT).show();
            }
        }) ;
    }
}
