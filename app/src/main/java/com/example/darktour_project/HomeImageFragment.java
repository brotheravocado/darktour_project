package com.example.darktour_project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class HomeImageFragment extends Fragment {

    Bundle args;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = view.findViewById(R.id.main_image);

        if (getArguments() != null) {
            args = getArguments();
            // MainActivity에서 받아온 Resource를 ImageView에 셋팅
            imageView.setImageResource(args.getInt("imgRes"));
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (args.getInt("imgRes")) {
                    case (R.drawable.busan): // 클릭한 이미지가 부산이라면
                        Log.d("지역", "부산");
                        intent = new Intent(getActivity(), Intro.class); // 넘길 화면 없어서 intro 화면으로 일단 넘김
                        //intent.putExtra("historyname",homeFragment.bundle.); // 코스이름 DetailPage로 넘김
                        startActivity(intent);
                        break;
                    case (R.drawable.seoul):
                        Log.d("지역", "서울");
                        intent = new Intent(getActivity(), Intro.class);
                        startActivity(intent);
                        break;
                    case (R.drawable.jeju):
                        Log.d("지역", "제주");
                        intent = new Intent(getActivity(), Intro.class);
                        startActivity(intent);
                        break;

                }
            }
        });
        return view;
    }
}