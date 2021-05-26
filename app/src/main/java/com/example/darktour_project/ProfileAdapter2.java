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

public class ProfileAdapter2 extends RecyclerView.Adapter<ProfileAdapter2.ViewHolder> {

    private ArrayList<Profile2> items = new ArrayList<>();

    @NonNull
    @Override
    public ProfileAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interestcourse_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter2.ViewHolder viewHolder, int position) {

        Profile2 item = items.get(position);

        viewHolder.favoriteCourse.setText(item.getFavoriteCourse());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Profile2> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteCourse;

        ViewHolder(View itemView) {
            super(itemView);

            favoriteCourse=itemView.findViewById(R.id.favoriteCourse);
        }
    }
}
