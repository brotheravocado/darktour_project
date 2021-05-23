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
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder viewHolder, int position) {

        Profile item = items.get(position);

        Glide.with(viewHolder.itemView.getContext()).load(item.getUrl()).into(viewHolder.historyImg);

        viewHolder.historyTitle.setText(item.getTitle());
        viewHolder.historyContent.setText(item.getContent());
        viewHolder.historyLikecount.setText(item.getLikecount());

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

            historyImg = itemView.findViewById(R.id.historic_img);

            historyTitle = itemView.findViewById(R.id.historylocal_name);
            historyContent = itemView.findViewById(R.id.reviewdetail);
            historyLikecount = itemView.findViewById(R.id.likes_count);
        }
    }
}
