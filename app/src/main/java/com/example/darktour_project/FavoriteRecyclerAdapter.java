package com.example.darktour_project;
// 코스 선택 화면 adpater

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ItemViewHolder> implements OnFavoriteItemClickListener{

    // adapter에 들어갈 list 입니다.
    private ArrayList<FavoriteData> listData = new ArrayList<>();
    OnFavoriteItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        FavoriteData item = listData.get(position);
        holder.onBind(listData.get(position));
        holder.cancel.setTag(position);

        holder.cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                listData.remove(pos);
                notifyDataSetChanged();

                ((SearchCourse)SearchCourse.mContext).refresh(pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(FavoriteData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }
    public void setOnItemClicklistener(OnFavoriteItemClickListener listener){
        this.listener = listener;
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView desc;
        private TextView title; // 유적지
        private LinearLayout cancel; // 삭제버튼

        ItemViewHolder(View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            cancel = itemView.findViewById(R.id.cancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                }
            });
        }

        void onBind(FavoriteData data) {

            desc.setText(data.getDesc());
            title.setText(data.getTitle());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(listener != null){
                listener.onItemClick(ItemViewHolder.this, v, position);
            }
        }
    }
    public FavoriteData getItem(int position){
        return listData.get(position); }

    public void delete(int position) {

        try {

            listData.remove(position);

            notifyItemRemoved(position);

        } catch(IndexOutOfBoundsException ex) {

            ex.printStackTrace();

        }

    }

}


