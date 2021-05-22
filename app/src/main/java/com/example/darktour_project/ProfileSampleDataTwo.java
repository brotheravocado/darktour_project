package com.example.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataTwo {
    ArrayList<Profile2> items=new ArrayList<>();

    public ArrayList<Profile2> getItems() {

        Profile2 profile = new Profile2("https://www.much.go.kr/cooperation/images/introimg_mod.jpg");
        Profile2 profile2 = new Profile2("https://www.much.go.kr/cooperation/images/introimg_mod.jpg");
        Profile2 profile3 = new Profile2("https://www.much.go.kr/cooperation/images/introimg_mod.jpg");
        Profile2 profile4 = new Profile2("https://www.much.go.kr/cooperation/images/introimg_mod.jpg");
        Profile2 profile5 = new Profile2("https://www.much.go.kr/cooperation/images/introimg_mod.jpg");

        items.add(profile);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4);
        items.add(profile5);

        return items;
    }
}
