package com.travel.darktour_project;

public class Profile {
    private String title="";
    private String content="";
    private String url="";

    public Profile(String title){
        this.title=title;
    }

    public Profile(String title, String content, String url){
        this.title=title;
        this.content=content;
        this.url=url;
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
}
