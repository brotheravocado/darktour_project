package com.travel.darktour_project;
// reviewrecycleradapter 리뷰 리사이클러뷰 윤지
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ReviewData> listData = new ArrayList<>();


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
        holder.thumb_button.setImageResource(item.getThumb_image());
        holder.total_like.setText(item.getLike()); // 좋아요 숫자
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


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView user_id;
        private TextView user_review;
        private TextView user_title; // 유적지 or 코스 이름
        private TextView total_like; // 따봉 숫자
        private TextView category; // 코스 인지 유적지인지 카테고리
        private ImageButton thumb_button; // 따봉 버튼
        private ImageView image; // 리뷰 사진

        ItemViewHolder(View itemView) {
            super(itemView);

            user_id = itemView.findViewById(R.id.user_id);
            user_review = itemView.findViewById(R.id.user_review);
            user_title = itemView.findViewById(R.id.title);
            total_like = itemView.findViewById(R.id.thumb_count);
            category = itemView.findViewById(R.id.tag);
            thumb_button = itemView.findViewById(R.id.thumb_button);
            image = itemView.findViewById(R.id.review_image); // 리뷰 사진

            thumb_button.setOnClickListener(this);
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
            total_like.setText(data.getLike()); // 좋아요 숫자
            category.setBackgroundResource(data.getTag_color()); // 카테고리 색상
            category.setText(data.getCategory()); // 카테고리 이름
            thumb_button.setImageResource(data.getThumb_image()); // 따봉 이미지
            //image.setImageResource(data.getImage());// 리뷰 사진 이미지
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.thumb_button:

                    Boolean clickBefore = listData.get(getAdapterPosition()).isPress();
                    if (clickBefore == false){
                        listData.get(getAdapterPosition()).setThumb_image(R.drawable.press_thumbs_up);
                        listData.get(getAdapterPosition()).setPress(true);
                        int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) + 1 ; // 좋아요 숫자 변경
                        listData.get(getAdapterPosition()).setLike(Integer.toString(num)); //  좋아요 숫자 설정
                        notifyItemChanged(getAdapterPosition());
                    }
                    else{
                        listData.get(getAdapterPosition()).setThumb_image(R.drawable.thumbs_up);
                        listData.get(getAdapterPosition()).setPress(false);
                        int num = Integer.parseInt(listData.get(getAdapterPosition()).getLike()) - 1 ; // 좋아요 숫자 변경
                        listData.get(getAdapterPosition()).setLike(Integer.toString(num)); //  좋아요 숫자 설정
                        notifyItemChanged(getAdapterPosition());
                    }

            }
        }
    }
    public ReviewData getItem(int position){
        return listData.get(position); }


}



