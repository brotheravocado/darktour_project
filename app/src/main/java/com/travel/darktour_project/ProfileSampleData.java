package com.travel.darktour_project;

import java.util.ArrayList;

public class ProfileSampleData {
    String IP_ADDRESS = "113.198.236.105";
    String tablename = "";
    String userid = "";
    ArrayList<Profile> items=new ArrayList<>();

    public ProfileSampleData(String tablename, String userid){
        this.tablename = tablename;
        this.userid = userid;

        ListLikes listLikes = new ListLikes();
        listLikes.execute("http://" + IP_ADDRESS + "/select.php", "likehistoric", userid);
    }

    public ArrayList<Profile> getItems() {
        // 유적지 리사이클러뷰 목록 추가
        Profile profile = new Profile("부산근대역사관",
                "1929년 일제 강점기에 지어진 이 건물은 최초에는 식민지 수탈기구인 동양척식주식 회사 부산지점으로 사용되었고, 해방후인 1949년부터는 미국 해외공보처 부산문화원이 되었다. 이후 부산시민들의 끊임없는 반환요구로 미문화원이 철수하고, 1999년 대한민국정부로 반환된 것을 그 해 6월 부산시가 인수하였다. 이 건물이 침략의 상징이었던 만큼 부산시는 시민들에게 우리의 아픈 역사를 알릴 수 있는 교육의 공간으로 활용하기 위해 근대역사관으로 조성하였다. 1920년대에 세워진 이 건물은 철근콘크리트조 건물로 서구양식이 도입될 당시의 건축 경향을 살필 수 있는 몇 남지 않은 자료이다. 전시내용은 외세의 침략과 수탈로 형성된 부산의 근현대역사를 중심으로 하였다. 개항기 부산, 일제의 부산수탈, 근대도시 부산, 동양척식주식회사, 근현대 한미관계, 부산의 비전 등으로 구성하였다.",
                "https://www.much.go.kr/cooperation/images/introimg_mod.jpg", "10");

        Profile profile1 = new Profile("임시수도기념관",
                "임시수도기념관은 한국전쟁이라는 국난의 시기에 대한민국 임시수도로서 소명을 훌륭하게 마친 부산의 위상과 역사성을 기념하고 부산시민의 자긍심을 고취하기 위해 1984년 6월 25일 개관하였다. 개관 당시 중심 건물이었던 대통령관저는 1925년 경상남도 도청이 진주에서 부산으로 이전하면서 1926년 8월 10일 도지사 관사로 지어진 벽돌조의 2층 가옥으로, 1950년 6.25전쟁(한국전쟁)의 발발로 부산이 대한민국의 임시수도로 기능하던 기간(1950~53년)에는 대한민국의 초대 대통령인 이승만 대통령이 거처하셨던 곳이다. 현재 근대건축물로서의 역사성이 인정되어 부산시 기념물 제53호로 지정되었으며, 잘 가꾸어진 야외정원과 어우러져 고즈넉한 운치를 자랑하는 곳이기도 하다. 지하1층, 지상1층의 벽돌조 가옥인 전시관은 한국전쟁의 발발과 피란민들의 생활상, 임시수도 시기 부산의 정치, 경제, 문화 등을 보여주는 상성전시실로 꾸며져 있다.",
                "https://www.bsseogu.go.kr/upload_data/board_data/BBS_0000085/152963494202482.jpg", "10");

        Profile profile2 = new Profile("40계단",
                "40계단은 6.25전쟁(한국전쟁) 피난시절 교통·행정의 중심지였던 부산중구에 위치하여 많은 피난민들이 부산항이 내려다보이는 영주동과 동광동 산비탈에 임시로 판자촌을 이루고 살았으며 바로 앞 부두에서 들어오는 구호물자를 내다 파는 장터로 이용되었다. 그리고 피난 중 헤어진 가족들의 상봉 장소로 유명했으며 피난살이의 애환을 상징하는 곳이다. 1951년 박재홍이 부른 “경상도아가씨” 라는 곡의 소재로도 사용되는 등 당시 영주동 뒷산, 동광동, 보수동 일대에 흩어져 살았던 10만이 넘는 피난민들에게는 가장 친근한 장소였다. 현재 동광동주민센터 건물 5층 전시실에는 40계단과 6·25 전쟁을 주제로 하는 부산광역시 중구의 역사가 전시되어 있다.",
                "http://www.dbeway.co.kr/_UPLOAD/IMAGE/TravelPoint/TravelMain/2016/12/roiOnlvzjGeE6wLB.JPG", "10");

        Profile profile3 = new Profile("임시수도정부청사",
                "일제는 부산이 유일한 항만 관문이고 교통의 중심지이며, 산업·교육·문화가 발달하였다는 점 등을 내세워 경남도청을 진주에서 부산으로 이전하려고 하였다. 식민 통치의 효율을 높이면서, 개항 이후부터 공을 들여 건설한 부산을 대륙 침략의 전초 기지로 활용하기 위한 속셈이었다. 이러한 배경으로 임시중앙청(부산임시수도정부청사)은 일제 강점기인 1925년 4월 ‘경상남도청사’로 건립되어 사용되다가 해방 이후 부산이 임시수도가 되면서 임시수도정부청사로 사용되었다. 즉, 1950년 9월 28일부터 그해 10월 27일까지 1차 임시수도정부청사로, 1951년 1·4 후퇴 때부터 1953년 8월 15일까지 2차 임시수도정부청사로 사용되었다. 1953년 8월 15일부터는 다시 경남도청으로 사용되었으며, 1983년 7월 경남도청이 창원으로 이전하면서 도청으로서의 역사를 마감하였다. 본 건물은 우리나라 근대사의 정치·사회적 변화를 간직한 역사적인 건물로 임시수도 대통령관저와 더불어 대표적인 피란수도기의 정부관련 건축물이다.",
                "https://www.busan.go.kr/pr/heritage0102/view?curPage=1&bbsNo=7&dataNo=5232", "10");

        Profile profile4 = new Profile("우암동소막마을",
                "소막마을은 소의 막사가 있었던 곳으로 부산 사람에게도 생소한 곳이다. 일본은 일제 강점기 시절 쌀뿐만 아니라 소가죽도 많이 수탈해갔고, 많은 소가 이곳에서 길러지고 도축되어 일본으로 보내졌다. 해방 이후 6.25 한국전쟁이 발발했고, 인구 50만이던 부산은 전쟁 이후 88만 명으로 늘었다. 갈 곳이 없던 피란민들은 해방 이후 비어있던 이곳 소 막사까지 찾게 되었다.소 막사 한 칸의 규격은 폭 2.5m, 길에 4m로 약 4평.이 한 칸 한 칸에 나무판을 벽 삼아 살다가 시간이 흘러 시멘트 벽으로 바뀌면서 지금의 골목 형태를 갖추게 되었다. 지금의 좁고 빽빽한 골목, 집들의 모습은 예전 막사의 형태에서 조금씩 개조되어 지금의 모습이 되었다. 지금도 한 사람이 채 지나가기도 좁은 골목길에 들어서면 당시의 흔적들이 남아있다. 예전 소 막사 지붕의 모습과 환기구로 쓰이던 작은 창들을 쉽게 찾을 수 있다.유독 피란민들이 많았던 부산은 이런 곳들을 근대유산으로 보존하고 복원하고 있다. 2018년 등록문화제 715호로 지정된 소막마을은 부산시 역사문화마을로 지정되어 정비 중이다. 현재는 소 막사 복원에 한창이다.",
                "https://lh3.googleusercontent.com/proxy/2GfLP9KkOsANk_a9IRLhNBLjPBEeYXTctiV8_DyeQtKUXmG752T6H2cKJSftzmsCH9om369SOMFjT4jkU44VNTIzN6u-afv29GmD_9H3LpEDUTKiKUkoVA", "10");

        items.add(profile);
        items.add(profile1);
        items.add(profile2);
        items.add(profile3);
        items.add(profile4);


        return items;
}
}
