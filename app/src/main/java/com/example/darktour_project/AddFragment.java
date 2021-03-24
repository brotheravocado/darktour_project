package com.example.darktour_project;

// 윤지 fragment 코스 탐색 구현부
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.zip.Inflater;

public class AddFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        // 서울 버튼 눌렀을 때
        ImageButton seoul_btn = (ImageButton) view.findViewById(R.id.seoul_pic);
        seoul_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DetailPage.class); // 변경 해야함
                intent.putExtra("location","seoul"); // 서울 선택한 것을 다음 화면에 넘김
                startActivity(intent);
            }
        });

        // 부산 버튼 눌렀을 때
        ImageButton busan_btn = (ImageButton) view.findViewById(R.id.busan_pic);
        busan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DetailPage.class); // 변경 해야함
                intent.putExtra("location","busan"); // 부산 선택한 것을 다음 화면에 넘김
                startActivity(intent);
            }
        });

        // 제주 버튼 눌렀을 때
        ImageButton jeju_btn = (ImageButton) view.findViewById(R.id.jeju_pic);
        jeju_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DetailPage.class); // 변경 해야함
                intent.putExtra("location","jeju"); // 제주 선택한 것을 다음 화면에 넘김
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
