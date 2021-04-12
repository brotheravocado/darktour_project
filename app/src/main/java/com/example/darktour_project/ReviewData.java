package com.example.darktour_project;
// 리사이클러 리뷰 data



public class ReviewData {

    private String id;
    private String review;
    private String title;
    private int image; // image url
    private String like;
    private int tag_color; // 카테고리 색상
    private String category; // 카테고리 이름
    private boolean press = false ; // 버튼 눌려졌는가

    public ReviewData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }


    public int getTag_color() {
        return tag_color;
    }

    public void setTag_color(int tag_color) {
        this.tag_color = tag_color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }
}