package com.travel.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataThree {
    ArrayList<Profile3> items=new ArrayList<>();

    public ArrayList<Profile3> getItems() {

        Profile3 profile1 = new Profile3("부산근대역사관",
                "부산근대역사관",
                "10시간");

        Profile3 profile2 = new Profile3("부산근대역사관",
                "부산근대역사관",
                "10시간");

        Profile3 profile3 = new Profile3("부산근대역사관",
                "부산근대역사관",
                "10시간");

        Profile3 profile4 = new Profile3("부산근대역사관",
                "부산근대역사관",
                "10시간");

        Profile3 profile5 = new Profile3("부산근대역사관",
                "부산근대역사관",
                "10시간");

        items.add(profile1);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4);
        items.add(profile5);


        return items;
    }
}
