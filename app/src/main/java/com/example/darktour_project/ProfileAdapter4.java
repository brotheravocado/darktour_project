package com.example.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ProfileAdapter4 extends RecyclerView.Adapter<ProfileAdapter4.ViewHolder> {

    private ArrayList<Profile4> items = new ArrayList<>();

    @NonNull
    @Override
    public ProfileAdapter4.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myreview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter4.ViewHolder viewHolder, int position) {

        Profile4 item = items.get(position);

        /*Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);*/

        viewHolder.date.setText(item.getDate());
        viewHolder.coursename.setText(item.getCourseName());
        viewHolder.contents.setText(item.getContents());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Profile4> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, coursename,contents;

        ViewHolder(View itemView) {
            super(itemView);


            date = itemView.findViewById(R.id.traveldate);
            coursename = itemView.findViewById(R.id.coursename);
            contents = itemView.findViewById(R.id.coursereview);
        }
    }
}