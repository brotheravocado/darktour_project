package com.example.darktour_project;
// 리뷰 recycler
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ReviewFragment extends Fragment {
    View v;
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
    
    boolean i = true; // 버튼 눌려졌는지 확인
    
    private int num; //버튼에 따른 좋아요 숫자 확인하기 위해서 넣은 변수로 나중에 변경 혹은 삭제 해야함
// https://black-jin0427.tistory.com/222
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_review, container, false);

        Spinner spinner = (Spinner)v.findViewById(R.id.spinner); // 목록 상자
        ImageButton write = (ImageButton)v.findViewById(R.id.write_review); // 리뷰 쓰기 버튼

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // position 0은 코스 1은 유적지

                // 테스트 (함수는 언제든지 변수를 통해 한개로 만들 수 있을듯)

                    init(); // recyclerview 세팅
                    getData(position); // recyclerview 데이터 값 가져오고 넣는 곳!!!



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        write.setOnClickListener(new View.OnClickListener() { // 리뷰 쓰기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteReview.class); // 리뷰 쓰기 화면
                 // id 보내야하나?
                startActivity(intent);
            }
        });


        init(); // recyclerview 세팅
        getData(0); // recyclerview 데이터 값 가져오고 넣는 곳!!!
        //default는 유적지라서 0

        return v;
    }
    private void init() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }
    private void getData(int position) { // 데이터 가져오는 곳!!!!!!!!!!!
        // 데이터 가져와서 추출 하는 작업!
        String category_name;
        int color;
        // 예시 data
        Listid = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        Listreview = Arrays.asList(
                "ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다.",
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다."
        );
        Listtitle = Arrays.asList("제주 > 부산 > 서울 > 예시", "양윤지", "박경민", "황현지", "이혜주", "백다은", "김선희", "아보카도",
                "어피치", "다은이 야식은 오지않음", "제주", "부산", "강아지", "쿠키런 킹덤", "바닐라", "초코");

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
            data.setImage(R.drawable.thumbs_up);
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}