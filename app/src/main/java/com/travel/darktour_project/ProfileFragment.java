package com.travel.darktour_project;

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
import android.widget.ImageButton;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    View v;
    ImageButton setting;
    private LinearLayoutManager mLayoutManger;
    private LinearLayoutManager mLayoutManger2;
    private LinearLayoutManager mLayoutManger3;
    private LinearLayoutManager mLayoutManger4;

    private ProfileAdapter adapter = new ProfileAdapter();
    private ProfileAdapter2 adapter2 = new ProfileAdapter2();
    private ProfileAdapter3 adapter3 = new ProfileAdapter3();
    private ProfileAdapter4 adapter4 = new ProfileAdapter4();

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    RecyclerView recyclerView4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
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
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView2 = v.findViewById(R.id.recycler_view2);
        recyclerView3 = v.findViewById(R.id.recycler_view3);
        recyclerView4 = v.findViewById(R.id.recycler_view4);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mLayoutManger=new LinearLayoutManager(v.getContext());
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManger2=new LinearLayoutManager(v.getContext());
        mLayoutManger2.setOrientation(LinearLayoutManager.VERTICAL);

        mLayoutManger3=new LinearLayoutManager(v.getContext());
        mLayoutManger3.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManger4=new LinearLayoutManager(v.getContext());
        mLayoutManger4.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView2.setLayoutManager(mLayoutManger2);
        recyclerView3.setLayoutManager(mLayoutManger3);
        recyclerView4.setLayoutManager(mLayoutManger4);

        recyclerView.setAdapter(adapter);
        recyclerView3.setAdapter(adapter3);
        recyclerView4.setAdapter(adapter4);

        //아이템 로드
        adapter.setItems(new ProfileSampleData().getItems());
        adapter2.setItems(new ProfileSampleDataTwo().getItems());
        adapter3.setItems(new ProfileSampleDataThree().getItems());
        adapter4.setItems(new ProfileSampleDataFour().getItems());

        recyclerView2.setAdapter(adapter2);

        adapter2.setOnItemClicklistener(new OnFCItemClickListener() {
            @Override
            public void onItemClick(ProfileAdapter2.ViewHolder holder, View view, int position) {
                Profile2 item =adapter2.getItem(position);
                Toast.makeText(getContext(),item.getFavoriteCourse(),
                        Toast.LENGTH_LONG).show();
            }
        });
        return v;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}