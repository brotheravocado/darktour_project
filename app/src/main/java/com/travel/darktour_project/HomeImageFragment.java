package com.travel.darktour_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class HomeImageFragment extends Fragment {

    Bundle args;
    Bundle name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = view.findViewById(R.id.main_image);
        TextView textView = view.findViewById(R.id.name);

        if (getArguments() != null) {
            args = getArguments();
            name = getArguments();
            // MainActivity에서 받아온 Resource를 ImageView에 셋팅
            Glide.with(this).load(args.getString("imgRes")).into(imageView);
            //imageView.setImageResource(args.getString("imgRes"));
            textView.setText(args.getString("nameRes"));
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", args.getString("nameRes"));
                Intent intent = new Intent(getActivity(), DetailPage.class);
                intent.putExtra("historyname",args.getString("nameRes")); // 코스이름 DetailPage로 넘김
                startActivity(intent);
            }
        });
        return view;
    }
}