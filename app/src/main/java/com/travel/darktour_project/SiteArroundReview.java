package com.travel.darktour_project;
// 유적지에 대한 리뷰가 궁금하신가요? 리뷰 or 코스에 대한 리뷰가 궁금하신가요? 리뷰
// 우선은 유적지 리뷰 중심으로 만듬

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;


public class SiteArroundReview extends AppCompatActivity {
    // adapter에 들어갈 list 입니다.
    private ReviewRecyclerAdapter adapter;
    String getId []; //id
    String getReview []; // review
    String getImage []; // image
    String getTitle [] ; // title
    String getLike [] ; // counting like

    List<String> Listid; // id
    List<String> Listreview; // review
    List<String> Listimage; // image
    List<String> Listtitle; // title
    List<String> Listlike; // like
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_site_arround);
        init();
        getData(1); // 이 화면 넘어올 때 코스 리뷰인지 유적지 리뷰인지 들고와야함
        // 이 화면올 때는 유적지 or 코스 이름 이랑 유적지인지 코스인지 알 수 있는 카테고리 정보
        // (position 0 이면 코스 1)
    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }
    private void getData(int position) { // 데이터 가져오는 곳!!!!!!!!!!!
        // 데이터 가져와서 추출 하는 작업!
        String category_name;
        int color;
        // 예시 data
        Listid = Arrays.asList("dbswl5920@gmail.com", "sunny233@naver.com", "pkm030800@naver.com", "danni22@gmail.com", "tot368@naver.com", "hyeju977@naver.com", "hyunji022@gmail.com"); // 7개
        Listreview = Arrays.asList(
                "일제강점기 시대에 대해서 자세히 알 수 있는 시간이었습니다.",
                "6.25전쟁 시절 일어난 전반적인 흐름에 대해 알 수 있었습니다.",
                "제주 4.3사건에 대해 몰랐지만 추천해주신 코스 덕분에 많이 배우고 갑니다.",
                "일제강점기 시대에 대해서 자세히 알 수 있는 시간이었습니다.",
                "제주 4.3사건에 대해 몰랐지만 추천해주신 코스 덕분에 많이 배우고 갑니다.",
                "과거 한국전쟁 때 부산에서 일어난 전반적인 흐름에 대해 알 수 있었습니다.",
                "일제강점기 시대에 대해서 자세히 알 수 있는 시간이었습니다."
        );
        Listtitle = Arrays.asList("부산근대역사관 > 가덕도외양포마을 > 가덕도외양포마을",
                "아비동비석문화마을 > 40계단 > 임시수도기념관",
                "4.3해원방사탑 > 너븐숭이 4.3기념관 > 중문리 신사터",
                "부산근대역사관 > 가덕도외양포마을 > 가덕도외양포마을",
                "4.3해원방사탑 > 너븐숭이 4.3기념관 > 중문리 신사터",
                "아비동비석문화마을 > 40계단 > 임시수도기념관",
                "부산근대역사관 > 가덕도외양포마을 > 가덕도외양포마을");

        Listlike = Arrays.asList("10", "15", "20", "30", "10", "50", "100", "35",
                "12", "1", "7", "9", "200", "102", "5", "20");
        // int list_cnt ;
        // array length - 데이터 개수가져오기
        // 참고는 ArroundFragment

        //key의 value를 가져와 저장하기 위한 배열을 생성한다
        /*getId = new String[list_cnt]; // 사용자 id 저장용
        getReview = new String[list_cnt]; // 리뷰 저장용
        getImage = new String[list_cnt]; // 이미지 경로
        getTitle = new String[list_cnt]; // 코스나 유적지 이름
        getLike = new String[list_cnt]; // 따봉 숫자
         */

        // 배열의 모든 아이템을 넣음
        /*
        for (int i = 0; i < list_cnt; i++) {
            JSONObject obj = jArray.getJSONObject(i); // ex) json\
            // name은 db 컬럼이름
            getId[i] = obj.getString("like");
            getReview[i] = obj.getString("review");
            getImage[i] = obj.getString("image");
            getTitle[i] = obj.getString("title");
            getLike[i] = obj.getString("like");
        }
         */

        // array를 list로 변환
        /*
        Listid = Arrays.asList(getId);
        Listreview = Arrays.asList(getReview);
        Listimage = Arrays.asList(getImage);
        Listtitle = Arrays.asList(getTitle);
        Listlike = Arrays.asList(getLike);
         */

        if (position == 0){ // 카테고리가 코스일때
            category_name = "코스";
            color = R.color.course_blue;
        }
        else{ // 카테고리가 유적지일때
            category_name = "유적지";
            color = R.color.site_pink;
        }
        for (int i = 0; i < Listid.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ReviewData data = new ReviewData();
            data.setId(Listid.get(i));
            data.setReview(Listreview.get(i));
            data.setTitle(Listtitle.get(i));
            data.setLike(Listlike.get(i));
            data.setTag_color(color);
            data.setCategory(category_name);
            data.setThumb_image(R.drawable.thumbs_up);// 따봉
            //data.setImage(R.drawable.ic_no_image); // 리뷰사진
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
