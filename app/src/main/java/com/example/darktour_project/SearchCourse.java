package com.example.darktour_project;
// 코스 탐색 화면

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchCourse extends AppCompatActivity implements View.OnClickListener  {
    //UI
    Spinner spinner1;
    Spinner spinner2;

    //Adapter
    CourseSearch1Adapter adapterSpinner1;
    CourseSearch2Adapter adapterSpinner2;

    EditText searchview;
    FloatingActionButton favorite_fab ; //fab 버튼

    private SearchSiteRecyclerAdapter adapter; // recyclerview adapter


    String getContent []; // content
    int getImage []; // image
    String getTitle [] ; // title
    String getLike [] ; // counting like


    List<String> ListContent; // content
    List<Integer> Listimage; // image
    List<String> Listtitle; // title
    List<String> Listlike; // like

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);
        Intent intent = getIntent(); // 데이터 수신

        String location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음


        searchview = findViewById(R.id.editSearch);
        favorite_fab = (FloatingActionButton) findViewById(R.id.fab); // fab 선언

        favorite_fab.setOnClickListener(this);

        Switch ai_switch = findViewById(R.id.ai_switch); // ai 버튼


        ai_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // ai 버튼 listener

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // check 되어있을 때
                    Toast.makeText(getApplicationContext(), "button is checked", Toast.LENGTH_SHORT).show();
                } else { // check 안되어있을 때
                    Toast.makeText(getApplicationContext(), "button is not checked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //spinner1 - 지역선택
        set_spinner1();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner1 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchview.setCursorVisible(false);
                init(); // recyclerview 세팅
                set_spinner2();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //--------------------------------------------------------------------
        // spinner2 - 교통 선택
        set_spinner2();
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner2 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // position 3은 hint라서 쓰지않음
                if (position < 3) { // 처음부터 recyclerview 안보이게 할려고
                    //init(); // recyclerview 세팅
                    getData(); // recyclerview 데이터 값 가져오고 넣는 곳!!!
                    searchview.setCursorVisible(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // spinner1에 위치 설정
        if (location.equals("seoul")) { // 서울
            spinner1.setSelection(0);
        } else if (location.equals("jeju")) { // 제주
            spinner1.setSelection(1);
        } else { // 부산
            spinner1.setSelection(2);
        }

        searchview.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    performSearch();

                    return true;

                }

                return false;
            }

        });
        searchview.addTextChangedListener(new TextWatcher() { // 검색
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                // input창에 문자를 입력할때마다 호출된다.


            }
        });


    }




    private void performSearch() {
        searchview.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchview.getWindowToken(), 0);
        //...perform search
    }

    private void set_spinner1() { // spinner1 설정
        //데이터 - 지역선택
        List<String> data1 = new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data1.add("서울"); data1.add("제주"); data1.add("부산"); // spinner1에 넣을 데이터

        //UI생성 spinner1 - 지역선택
        spinner1 = (Spinner)findViewById(R.id.spinner_1); // 지역선택

        //Adapter
        adapterSpinner1 = new CourseSearch1Adapter(this, data1);

        spinner1.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해
        // spinner1.setSelection(0, false); //선택되면
        //Adapter 적용 - 지역
        spinner1.setAdapter(adapterSpinner1);

    }
    private void set_spinner2(){ // spinner2 설정
        //데이터 - 교통선택
        List<String> data2= new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data2.add("대중교통"); data2.add("자동차"); data2.add("도보"); data2.add("선택"); // spinner2에 넣을 데이터 마지막이 hint

        //UI생성 spinner2- 교통
        spinner2 = (Spinner)findViewById(R.id.spinner_2); // 교통

        //Adapter
        adapterSpinner2 = new CourseSearch2Adapter(this, data2);

        spinner2.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해

        //Adapter 적용 - 교통
        spinner2.setAdapter(adapterSpinner2);
        //spinner2.setSelection(3); //힌트로 세팅
        spinner2.setSelection(adapterSpinner2.getCount()); //힌트로 세팅


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                click_fab(); // fab버튼 눌렀을 때 지금까지 선택된 화면

                break;



        }
    }
    public void click_fab(){ // fab버튼 눌렀을 때 지금까지 선택된 화면
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,FavoriteSite.class);
        startActivity(intent);
    }
    private void init() { // recyclerview 세팅
        RecyclerView recyclerView = findViewById(R.id.site_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SearchSiteRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }
    private void getData() { // 데이터 가져오는 곳!!!!!!!!!!!
        // 데이터 가져와서 추출 하는 작업!

        // 예시 data
        Listimage = Arrays.asList(R.drawable.seoul,R.drawable.busan,R.drawable.jeju,R.drawable.tobecontinued,R.drawable.seoul,R.drawable.busan,R.drawable.jeju,R.drawable.tobecontinued,
                R.drawable.seoul,R.drawable.busan,R.drawable.jeju,R.drawable.tobecontinued,R.drawable.seoul,R.drawable.busan,R.drawable.jeju,R.drawable.tobecontinued);
        ListContent = Arrays.asList(
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

        for (int i = 0; i < Listtitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SiteData data = new SiteData();
            data.setImage(Listimage.get(i));
            data.setDesc(ListContent.get(i)); // 내용
            data.setTitle(Listtitle.get(i));
            data.setLike(Listlike.get(i));
            data.setLayout_(R.drawable.write_review_back); // background 지정
            /*data.setImage(Listimage.get(i));
            data.setTitle(Listtitle.get(i));
            data.setLike(Listlike.get(i));*/
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
