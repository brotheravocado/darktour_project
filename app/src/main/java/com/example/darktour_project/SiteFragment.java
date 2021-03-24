package com.example.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SiteFragment extends Fragment {
    private int num;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);
        
        // 유적지 이름 설정 -> 추후 db 가지고와서 수정하기
        TextView historic_site = (TextView) view.findViewById(R.id.location_name);
        //historic_site.setText("북촌리 애기무덤");
        historic_site.setText("제주시 충혼묘지 (박진경 추도비)");




        // 좋아요 숫자
        num = 15;
        TextView thumb = (TextView) view.findViewById(R.id.thumb);
        thumb.setText(Integer.toString(num));



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}