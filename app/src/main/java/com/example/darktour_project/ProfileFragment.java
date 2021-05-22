package com.example.darktour_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import static com.example.darktour_project.R.id.recycler_view2;

public class ProfileFragment extends Fragment {
    View v;
    ImageButton setting;
    private LinearLayoutManager mLayoutManger;
    private LinearLayoutManager mLayoutManger2;

    private ProfileAdapter adapter = new ProfileAdapter();
    private ProfileAdapter2 adapter2 = new ProfileAdapter2();
    RecyclerView recyclerView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setting = v.findViewById(R.id.imageButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);

        //recycleView 초기화
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView2 = v.findViewById(R.id.recycler_view2);


        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mLayoutManger=new LinearLayoutManager(v.getContext());
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL); //리사이클뷰가로

        mLayoutManger2=new LinearLayoutManager(v.getContext());
        mLayoutManger2.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView2.setLayoutManager(mLayoutManger2);

        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);

        //아이템 로드
        adapter.setItems(new ProfileSampleData().getItems());
        adapter2.setItems(new ProfileSampleDataTwo().getItems());


        return v;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}