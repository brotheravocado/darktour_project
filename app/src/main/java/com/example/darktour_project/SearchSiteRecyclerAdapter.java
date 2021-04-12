package com.example.darktour_project;
// 코스 탐색 유적지 화면 adpater
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchSiteRecyclerAdapter extends RecyclerView.Adapter<SearchSiteRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<SiteData> listData = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        SiteData item = listData.get(position);
        holder.onBind(listData.get(position));
        holder.background_change.setBackgroundResource(item.getLayout_());
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(SiteData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView desc;
        private TextView title; // 유적지 or 코스 이름
        private TextView total_like; // 따봉 숫자
        private ImageView image; // image
        private boolean press = true;  // 눌렸는가
        private LinearLayout background_change; // 배경 변경을 위한 레이아웃

        ItemViewHolder(View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            total_like = itemView.findViewById(R.id.thumb_count);
            image = itemView.findViewById(R.id.image);
            background_change = itemView.findViewById(R.id.background_change);
            background_change.setOnClickListener(this);
        }

        void onBind(SiteData data) {

            desc.setText(data.getDesc());
            title.setText(data.getTitle());
            total_like.setText(data.getLike());
            image.setImageBitmap(data.getImage());// 이미지
            image.setClipToOutline(true);
            background_change.setBackgroundResource(data.getLayout_()); // 눌렀을때 layout

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.background_change:
                    Boolean clickBefore = listData.get(getAdapterPosition()).isSelected();
                    if (clickBefore == false){ // item 눌렀을 때
                        listData.get(getAdapterPosition()).setLayout_(R.drawable.press_back);
                        listData.get(getAdapterPosition()).setSelected(true);
                        notifyItemChanged(getAdapterPosition());
                    }
                    else{ // item 취소
                        listData.get(getAdapterPosition()).setLayout_(R.drawable.write_review_back);
                        listData.get(getAdapterPosition()).setSelected(false);
                        notifyItemChanged(getAdapterPosition());
                    }

            }
        }
    }
    public SiteData getItem(int position){
        return listData.get(position); }

}


