package com.travel.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        String str = item.getTitle();
        viewHolder.Title.setText(str);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Profile> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title;

        ViewHolder(View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.content);
        }
    }
}
