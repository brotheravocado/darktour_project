package com.travel.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataFour {
    ArrayList<Profile4_likehis> items=new ArrayList<>();

    public ArrayList<Profile4_likehis> getItems() {

        Profile4_likehis profile1 = new Profile4_likehis("21년 05월 24일",
                "(부산)부산근현대사를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩");

        Profile4_likehis profile2 = new Profile4_likehis("21년 05월 25일",
                "(제주)제주 4.3 역사를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩2");

        Profile4_likehis profile3 = new Profile4_likehis("21년 05월 26일",
                "(SM)SMP 계보를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩3");

        Profile4_likehis profile4Likehis = new Profile4_likehis("21년 05월 27일",
                "(만덕) 쌍용아파트를 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩4");

        Profile4_likehis profile5 = new Profile4_likehis("21년 05월 28일",
                "(동의) 어보감을 따라가보는 코스",
                "얄리얄리 얄라셩 얄라리 얄라셩5");


        items.add(profile1);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4Likehis);
        items.add(profile5);


        return items;
    }
}
