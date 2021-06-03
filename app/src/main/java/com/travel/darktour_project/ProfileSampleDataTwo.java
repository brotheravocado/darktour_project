package com.travel.darktour_project;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileSampleDataTwo {
    ArrayList<Profile2> items=new ArrayList<>();

    public ArrayList<Profile2> getItems(String favorite_history_course) {

        String name[] = favorite_history_course.split(",");

        ArrayList<String> course_name = new ArrayList<>(Arrays.asList(name));

        for(int i=0; i< course_name.size(); i++){
            String []favoriteSite =  course_name.get(i).split("-");
            items.add(new Profile2(favoriteSite));
        }
        return items;
    }
}
