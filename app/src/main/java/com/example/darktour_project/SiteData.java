package com.example.darktour_project;
// 리사이클러 유적지 data

public class SiteData {


    private String desc;
    private String title;
    private int image; // image url
    private String like; // 따봉숫자
    private boolean isSelected = false;
    private int layout_;

    public SiteData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelected(){
        return isSelected;
    }
    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public int getLayout_() {
        return layout_;
    }

    public void setLayout_(int layout_) {
        this.layout_ = layout_;
    }
}