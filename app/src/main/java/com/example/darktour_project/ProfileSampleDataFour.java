package com.example.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataFour {
    ArrayList<Profile4> items=new ArrayList<>();

    public ArrayList<Profile4> getItems() {

        Profile4 profile1 = new Profile4("21년 05월 24일",
                "(부산)부산근현대사를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩");

        Profile4 profile2 = new Profile4("21년 05월 25일",
                "(제주)제주 4.3 역사를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩2");

        Profile4 profile3 = new Profile4("21년 05월 26일",
                "(SM)SMP 계보를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩3");

        Profile4 profile4 = new Profile4("21년 05월 27일",
                "(만덕) 쌍용아파트를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩4");

        Profile4 profile5 = new Profile4("21년 05월 28일",
                "(동의) 어보감을 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩5");


        items.add(profile1);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4);
        items.add(profile5);


        return items;
    }
}
