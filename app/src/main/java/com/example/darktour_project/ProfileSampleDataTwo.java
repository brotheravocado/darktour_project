package com.example.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataTwo {
    ArrayList<Profile2> items=new ArrayList<>();

    public ArrayList<Profile2> getItems() {

        Profile2 profile = new Profile2("(제주) 4.3을 따라가보는 코스");
        Profile2 profile2 = new Profile2("(부산) 근현대사를 따라가보는 코스");
        Profile2 profile3 = new Profile2("(동의) 어 보감을 따라가보는 코스");
        Profile2 profile4 = new Profile2("(모라) 동원아파트");
        Profile2 profile5 = new Profile2("(부산) 6.25를 따라가보는 코스");

        items.add(profile);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4);
        items.add(profile5);

        return items;
    }
}
