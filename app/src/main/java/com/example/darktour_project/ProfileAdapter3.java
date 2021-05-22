package com.example.darktour_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ProfileAdapter3 extends RecyclerView.Adapter<ProfileAdapter3.ViewHolder> {

    private ArrayList<Profile3> items = new ArrayList<>();

    @NonNull
    @Override
    public ProfileAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycourse_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter3.ViewHolder viewHolder, int position) {

        Profile3 item = items.get(position);

        /*Glide.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .into(viewHolder.ivMovie);*/

        viewHolder.startlocal.setText(item.getStartlocal());
        viewHolder.finishlocal.setText(item.getFinishlocal());
        viewHolder.time.setText(item.getTraffictime());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Profile3> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView startlocal, finishlocal, time;

        ViewHolder(View itemView) {
            super(itemView);


            startlocal = itemView.findViewById(R.id.startlocal);
            finishlocal = itemView.findViewById(R.id.finishlocal);
            time = itemView.findViewById(R.id.traffictime);
        }
    }
}