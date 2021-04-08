package com.example.darktour_project;
// reviewrecycleradapter 리뷰 리사이클러뷰 윤지
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ReviewData> listData = new ArrayList<>();
    OnItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.

        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        ReviewData item = listData.get(position);
        holder.onBind(listData.get(position));

        //holder.thumb_button.setTag(position); // 따봉 버튼
        holder.thumb_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if ( holder.press == true){ // 좋아요 버튼 눌려졌을 때
                    holder.thumb_button.setImageResource(R.drawable.press_thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정

                    int num = 16;
                    holder.total_like.setText(Integer.toString(num));
                    holder.press = false;
                }else { // 좋아요 버튼 취소
                    holder.thumb_button.setImageResource(R.drawable.thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정
                    int num = 15;
                    holder.total_like.setText(Integer.toString(num));
                    holder.press = true;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    void addItem(ReviewData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    public void setOnItemClicklistener(OnItemClickListener listener){
        this.listener = listener;
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView user_id;
        private TextView user_review;
        private TextView user_title; // 유적지 or 코스 이름
        private TextView total_like; // 따봉 숫자
        private TextView category; // 코스 인지 유적지인지 카테고리
        private ImageButton thumb_button; // 따봉 버튼
        private boolean press;  // 눌렸는가
        private LinearLayout review; // 리뷰 내용 클릭을 위해 레이아웃

        ItemViewHolder(View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_review = itemView.findViewById(R.id.user_review);
            user_title = itemView.findViewById(R.id.title);
            total_like = itemView.findViewById(R.id.thumb_count);
            category = itemView.findViewById(R.id.tag);
            thumb_button = itemView.findViewById(R.id.thumb_button);
            review = itemView.findViewById(R.id.review_content);

        }

        void onBind(ReviewData data) {
            user_id.setText(data.getId());
            Html.ImageGetter imgGetter = new Html.ImageGetter(){ // 옆에 책 모양 넣을라고

                public Drawable getDrawable(String source) {

                    //Drawable d = itemView.getResources().getDrawable(id);
                    Drawable d = ResourcesCompat.getDrawable(user_review.getResources(), R.drawable.more_img, null);

                    d.setBounds(10, 0,60,40);
                    //d.setBounds(0, 0, 30,30);
                    return d;

                }
            };
            String review =  data.getReview()+ "<img src=\"more_img\">";

            user_review.setText(Html.fromHtml(review,imgGetter,null));
            user_title.setText(data.getTitle());
            
            category.setBackgroundResource(data.getTag_color()); // 카테고리 색상
            category.setText(data.getCategory()); // 카테고리 이름
        }

    }
    public ReviewData getItem(int position){
        return listData.get(position); }


}



