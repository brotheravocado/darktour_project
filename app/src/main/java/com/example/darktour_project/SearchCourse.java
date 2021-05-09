package com.example.darktour_project;
// 코스 탐색 화면

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.edsergeev.TextFloatingActionButton;


import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchCourse extends AppCompatActivity implements View.OnClickListener{
    //UI
    Spinner spinner1;
    Spinner spinner2;

    //Adapter
    CourseSearch1Adapter adapterSpinner1;
    CourseSearch2Adapter adapterSpinner2;

    EditText searchview;
    TextFloatingActionButton favorite_fab ; //fab 버튼

    private SearchSiteRecyclerAdapter adapter; // recyclerview adapter


    String getContent []; // content
    String getImage []; // image
    String getTitle [] ; // title
    String getLike [] ; // counting like

    public static Context mContext;
    List<String> ListContent; // content
    List<String> Listimage; // image
    List<String> Listtitle; // title
    List<String> Listlike; // like
    ArrayList num = new ArrayList<Integer>();
    ArrayList data_name = new ArrayList<String>() ; // 다음 화면(유적지 선택되는 화면) 유적지 이름
    ArrayList data_content = new ArrayList<String>() ; // 다음 화면(유적지 선택되는 화면) 유적지 설명
    String location; // 지역
    String transportation; // 이동수단
    String checked_ai; // ai check 여부
    int count = 0;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);
        Intent intent = getIntent(); // 데이터 수신

        location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음
        mContext = this;

        searchview = findViewById(R.id.editSearch);
        favorite_fab = (TextFloatingActionButton) findViewById(R.id.fab); // fab 선언

        favorite_fab.setOnClickListener(this);

        Switch ai_switch = findViewById(R.id.ai_switch); // ai 버튼


        ai_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // ai 버튼 listener

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // check 되어있을 때
                    //Toast.makeText(getApplicationContext(), "button is checked", Toast.LENGTH_SHORT).show();
                    checked_ai = "AI 추천";
                } else { // check 안되어있을 때
                    //Toast.makeText(getApplicationContext(), "button is not checked", Toast.LENGTH_SHORT).show();
                    checked_ai = " "; // 추천 안눌렀을때
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
                    if(position == 0){ // 대중교통
                        transportation = "대중교통";
                    }
                    else if(position == 1){ // 자동차
                        transportation = "자동차";
                    }
                    else{ // 도보
                        transportation = "도보";
                    }
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
                if(count == 0 || count == 1){
                    Toast.makeText(SearchCourse.this, "2개 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    click_fab(); // fab버튼 눌렀을 때 지금까지 선택된 화면
                }
                break;
        }
    }
    public void click_fab(){ // fab버튼 눌렀을 때 지금까지 선택된 화면
        Intent intent = new Intent(this,FavoriteSite.class);
        String [] arr_title  = (String[]) data_name.toArray(new String[data_name.size()]);
        String [] arr_content  = (String[]) data_content.toArray(new String[data_content.size()]);
        intent.putExtra("select_title",arr_title);
        intent.putExtra("select_content",arr_content);
        intent.putExtra("location",location);
        intent.putExtra("transportation",transportation);
        intent.putExtra("ai",checked_ai);
        startActivity(intent);
    }
    private void init() { // recyclerview 세팅
        RecyclerView recyclerView = findViewById(R.id.site_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SearchSiteRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnSiteItemClickListener() {
            @Override
            public void onItemClick(SearchSiteRecyclerAdapter.ItemViewHolder holder, View view, int position) {

                            Boolean clickBefore = adapter.getItem(position).isSelected();
                            if (clickBefore == false){ // item 눌렀을 때
                                if(count < 5) {
                                    adapter.getItem(position).setLayout_(R.drawable.press_back);
                                    adapter.getItem(position).setSelected(true);
                                    num.add(count, position);
                                    data_name.add(count, adapter.getItem(position).getTitle()); // 유적지 이름 추가
                                    data_content.add(count, adapter.getItem(position).getDesc()); // 유적지 설명 추가
                                    adapter.notifyItemChanged(position);
                                    count++;

                                    //notifyItemChanged(getAdapterPosition());
                                }
                                else{
                                    Toast.makeText(SearchCourse.this, "최대 5개까지 선택가능합니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{ // item 취소w
                                adapter.getItem(position).setLayout_(R.drawable.write_review_back);
                                adapter.getItem(position).setSelected(false);
                                adapter.notifyItemChanged(position);
                                int temp = num.indexOf(position);
                                num.remove(temp);
                                data_name.remove(temp); // 유적지 이름 삭제
                                data_content.remove(temp);// 유적지 설명 삭제
                                count --;

                            }
                            favorite_fab.setText(Integer.toString(count));

            }
        });
    }
    public void refresh(int position){
        int temp = (int) num.get(position);
        adapter.getItem(temp).setLayout_(R.drawable.write_review_back);
        adapter.getItem(temp).setSelected(false);
        adapter.notifyItemChanged(temp);

        num.remove(position);
        data_name.remove(position); // 유적지 이름 삭제
        data_content.remove(position);// 유적지 설명 삭제
        count --;
        favorite_fab.setText(Integer.toString(count));

    }
    private void getData() { // 데이터 가져오는 곳!!!!!!!!!!!
        // 데이터 가져와서 추출 하는 작업!

        // 예시 data
        Listimage = Arrays.asList("https://www.much.go.kr/cooperation/images/introimg_mod.jpg",
                "https://www.bsseogu.go.kr/upload_data/board_data/BBS_0000085/152963494202482.jpg",
                "http://www.dbeway.co.kr/_UPLOAD/IMAGE/TravelPoint/TravelMain/2016/12/roiOnlvzjGeE6wLB.JPG",
                "http://www.koya-culture.com/data/photos/20160834/art_1472008360.jpg",
                "https://lh3.googleusercontent.com/proxy/2GfLP9KkOsANk_a9IRLhNBLjPBEeYXTctiV8_DyeQtKUXmG752T6H2cKJSftzmsCH9om369SOMFjT4jkU44VNTIzN6u-afv29GmD_9H3LpEDUTKiKUkoVA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRw0w5d-YIg3bGPe8aov6y-P2Lold5_cwJpQg&usqp=CAU",
                "https://www.visitbusan.net/uploadImgs/files/cntnts/20191229150845004_ttiel");
        ListContent = Arrays.asList(
            "일제 강점기인 1929년 지어진 이 건물은 최초에는 식민지 수탈기구인 동양척식주식 회사 부산지점으로 사용되었고, 해방후인 1949년부터는 미국 해외공보처 부산문화원이 되었다. 이 후 부산시민들의 끊임없는 반환요구로 미문화원이 철수하고, 1999년 대한민국정부로 반환된 것을 그 해 6월 부산시가 인수하였다. 이 건물이 침략의 상징이었던 만큼 부산시는 시민들 에게 우리의 아픈 역사를 알릴 수 있는 교육의 공간으로 활용하기 위해 근대역사관으로 조성하였다. 전시내용은 외세의 침략과 수탈로 형성된 부산의 근현대역사를 중심으로 하였다. 개항기 부산, 일제의 부산수탈, 근대도시 부산, 동양척식주식회사, 근현대 한미관계, 부산의 비전 등으로 구성하였다.",
             "임시수도기념관은 한국전쟁이라는 국난의 시기에 대한민국 임시수도로서 소명을 훌륭하게 마친 부산의 위상과 역사성을 기념하고 부산시민의 자긍심을 고취하기 위해 1984년 6월 25일 개관하였다. 개관 당시 중심 건물이었던 대통령관저는 일제강점기인 1925년 경상남도 도청이 진주에서 부산으로 이전하면서 1926년 8월 10일 도지사 관사로 지어진 벽돌조의 2층 가옥으로, 1950년 한국전쟁 발발로 부산이 대한민국의 임시수도로 기능하던 기간(1950~53년)에는 대한민국의 초대 대통령인 이승만 대통령이 거처하셨던 곳이다. 현재 근대건축물로서의 역사성이 인정되어 부산시 기념물 제53호로 지정되었으며, 잘 가꾸어진 야외정원과 어우러져 고즈넉한 운치를 자랑하는 곳이기도 하다.",
             "40계단은 1950년 6·25 피난시절 교통·행정의 중심지였던 부산중구에 위치하여 많은 피난민들이 그 주위에 판잣 집을 짓고 밀집해서 살았었고 바로 앞 부두에서 들어오는 구호물자를 내다 파는 장터로, 그리고 피난 중 헤어진 가족들의 상봉 장소로 유명했던, 피난살이의 애환을 상징하던 곳으로 1951년 박재홍이 부른 “경상도아가씨” 라는 곡의 소재로도 사용되는 등 당시 영주동 뒷산, 동광동, 보수동 일대에 흩어져 살았던 10만이 넘는 피난민들에게는 가장 친근한 장소였다.",
             "임시중앙청(부산임시수도정부청사)은 일제강점기인 1925년 4월 ‘경상남도청사’로 건립되어 사용되다가 해방 이후 한국전쟁기 부산이 임시수도가 되면서 임시수도정부청사로 사용되었다. 즉, 1950년 9월 28일부터 그해 10월 27일까지 1차 임시수도정부청사로, 1951년 1·4 후퇴 때부터 1953년 8월 15일까지 2차 임시수도정부청사로 사용되었다.\n" +
                     "1953년 8월 15일부터는 다시 경남도청으로 사용되었으며, 1983년 7월 경남도청이 창원으로 이전하면서 도청으로서의 역사를 마감하였다.1984년 11월부터 2001년 9월까지 부산지방검찰청 청사로 사용되다가, 2002년 동아대학교가 매입하여 건립 당시의 형태로 외관을 복원, 2009년부터 동아대학교 석당박물관으로 사용하고 있다. 본 건물은 우리나라 근대사의 정치·사회적 변화를 간직한 역사적인 건물로 임시수도 대통령관저와 더불어 대표적인 피란수도기의 정부관련 건축물이다.",
              "일제강점기 시절 일본이 한국소를 수탈하기 위한 근거지를 마련한 곳이다. 부산항의 동쪽 항 즉, 동항에서 가장 가까운 이곳에 소 막사와 검역소를 설치하고 대대적으로 한우를 일본으로 빼돌렸다. 광복 후에는 일본에서 돌아온 동포들이, 한국전쟁 중에는 수많은 피란민들이, 비어있던 우암동 소 막사에 거주하게 되면서 소막마을이 탄생하게 되었다. 인구에 비해 턱없이 부족했던 공간은 막사와 막사 사이 그 틈 속에도 판잣집들이 빼곡하게 들어차게 만들었다.",
              "가덕도 최남쪽에 위치한 한적한 포구인 이곳은 1904년 2월 러일전쟁이 시작되며 일본의 군사거점확보를 위해 당시에 거주하던 주민들을 강제로 이주시키고 중포병대대와 진해만요사령부로 자리 잡은 곳으로 1948년 8월 해방되고 다시 주민들이 정착하며 자리를 잡은 마을입니다. 현재 외양포 마을에도 막사와 창고, 우물, 탄약고, 엄폐막사 등이 주민들이 거주하며 실 사용하고 있으며 수목으로 은폐된 포진지 유탄포 포좌흔적 6문과 당시의 상태 그대로 보존되어 있습니다. ",
              "부산민주공원 또는 공식명칭 민주공원은 4·19 혁명과 부마민주항쟁, 6월 항쟁으로 이어져 내려오는 부산 시민의 숭고한 민주 희생 정신을 기리고 계승 발전시키기 위해 부산 민주화 운동의 상징적 공간으로 부산광역시 중구 중앙공원 안에 만든 공원이다."
        );
        Listtitle = Arrays.asList("부산근대역사관","임시수도기념관","40계단","임시수도정부청사","우암동소막마을",
                "가덕도외양포마을","부산민주공원");

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
            try { // url 이미지
                data.setImage(new DownloadFilesTask().execute(Listimage.get(i)).get()); // 이미지

            } catch (Exception e) {

                e.printStackTrace();

            }
            
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
    //이미지 url 가져오는거
    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소

        }
    }
}
