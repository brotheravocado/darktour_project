package com.travel.darktour_project;

public class Profile4 {
    private String date;
    private String courseName;
    private String contents;

    public Profile4(String date, String courseName, String contents){
        this.date=date;
        this.courseName=courseName;
        this.contents=contents;
    }

    public String getDate(){
        return date;
    }
    public String getCourseName(){
        return courseName;
    }
    public String getContents(){
        return contents;
    }
}
