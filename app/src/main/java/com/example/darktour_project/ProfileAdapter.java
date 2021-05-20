package com.example.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private ArrayList<Profile> items = new ArrayList<>();

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interestsite_item, parent, false);
        //유적지 카드뷰 레이아웃 연결
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder viewHolder, int position) {

        Profile item = items.get(position);

        Glide.with(viewHolder.itemView.getContext()).load(item.getUrl()).into(viewHolder.historyImg);
        //이미지 연결

        viewHolder.historyTitle.setText(item.getTitle()); //유적지 이름
        viewHolder.historyContent.setText(item.getContent()); // 유적지 설명
        viewHolder.historyLikecount.setText(item.getLikecount()); // 유적지 좋아요 갯수

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Profile> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView historyTitle, historyContent, historyLikecount;
        ImageView historyImg;

        ViewHolder(View itemView) {
            super(itemView);

            historyImg = itemView.findViewById(R.id.historic_img); //이미지 뷰 연결

            historyTitle = itemView.findViewById(R.id.historic_name); // 유적지 이름 연결
            historyContent = itemView.findViewById(R.id.historic_desc); // 유적지 설명 연결
            historyLikecount = itemView.findViewById(R.id.likes_count); // 유적지 좋아요 연결
        }
    }
}
