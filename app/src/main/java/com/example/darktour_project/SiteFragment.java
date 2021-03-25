package com.example.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteFragment extends Fragment {
    private int num;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);
        
        // 유적지 이름 설정 -> 추후 db 가지고와서 수정하기
        TextView historic_site = (TextView) view.findViewById(R.id.location_name);
        //historic_site.setText("북촌리 애기무덤");
        //historic_site.setText("제주시 충혼묘지 (박진경 추도비)");
        historic_site.setText("제주 4.3 평화공원");

        // 좋아요 숫자
        num = 15;
        TextView thumb = (TextView) view.findViewById(R.id.thumb);
        thumb.setText(Integer.toString(num));
        ImageView his_picture = (ImageView)view.findViewById(R.id.his_picture);
        his_picture.setImageResource(R.drawable.test_pic);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}