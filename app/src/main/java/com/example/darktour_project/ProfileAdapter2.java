package com.example.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileAdapter2 extends RecyclerView.Adapter<ProfileAdapter2.ViewHolder> {
    private ArrayList<Profile2> items = new ArrayList<>();
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interestcourse_item, parent, false);
        ProfileAdapter2.ViewHolder viewHolder=new ProfileAdapter2.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile2 item = items.get(position);
        holder.onBind(items.get(position));
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setItems(ArrayList<Profile2> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView=itemView.findViewById(R.id.textView);
        }

        public void onBind(Profile2 profile2) {
        }
    }
}
