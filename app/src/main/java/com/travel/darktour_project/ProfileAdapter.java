package com.travel.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    ArrayList<Profile> items = new ArrayList<>();
    OnProfileItemClickListener listener;

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

    public void setOnItemClicklistener(OnProfileItemClickListener listener){
        this.listener=listener;
    }

    public void onItemClick(ProfileAdapter.ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onItemClick(holder, view, position);
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title;

        ViewHolder(View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.content);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onItemClick(ProfileAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }
    }
}
