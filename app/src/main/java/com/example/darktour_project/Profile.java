package com.example.darktour_project;

public class Profile {
    private String title;
    private String content;
    private String url;
    private String likecount;

    public Profile(String title, String content, String url, String likecount){
        this.title=title;
        this.content=content;
        this.url=url;
        this.likecount=likecount;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getUrl(){
        return url;
    }
    public String getLikecount(){
        return likecount;
    }
}
