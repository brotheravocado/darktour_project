package com.example.darktour_project;
// 코스 탐색 화면

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.edsergeev.TextFloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchCourse extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    //UI
    Spinner spinner1;
    Spinner spinner2;


    CourseSearch1Adapter adapterSpinner1;//Adapter
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
    ArrayList image_string = new ArrayList<String>() ; // 사진 이미지
    ArrayList histoy_likes = new ArrayList<String>() ; // 사진 이미지
    String location; // 지역
    String transportation; // 이동수단
    String checked_ai; // ai check 여부
    int count = 0;
    ArrayList latitude = new ArrayList<Double>(); // 위도
    ArrayList longitude = new ArrayList<Double>();// 경도


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
                    //코스탐색 화면에서 지역을 바꿧을때 인식이 안되서 밑에 getSelectedItem 이거 추가! - 혜쥬
                    location = (String) spinner1.getSelectedItem();
                    Log.d(TAG, "location spinner - " + location);
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
        if (location.equals("서울")) { // 서울
            spinner1.setSelection(0);
        } else if (location.equals("제주")) { // 제주
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
        String [] arr_image  = (String[]) image_string.toArray(new String[image_string.size()]);
        Double [] arr_lat = (Double[]) latitude.toArray(new Double[latitude.size()]); //y
        Double [] arr_long = (Double[]) longitude.toArray(new Double[longitude.size()]); //x
        String [] arr_likes  = (String[]) histoy_likes.toArray(new String[histoy_likes.size()]);

        intent.putExtra("select_title",arr_title);
        intent.putExtra("select_content",arr_content);
        intent.putExtra("location",location);
        intent.putExtra("transportation",transportation);
        intent.putExtra("ai",checked_ai);
        intent.putExtra("latitude",arr_lat);
        intent.putExtra("longitude",arr_long);
        intent.putExtra("image",arr_image);
        intent.putExtra("like",arr_likes);

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
                        latitude.add(count, adapter.getItem(position).getLatitude()); // y 추가
                        longitude.add(count, adapter.getItem(position).getLongitude()); // x 추가
                        image_string.add(count, adapter.getItem(position).getImage()); // 이미지 추가
                        histoy_likes.add(count, adapter.getItem(position).getLike()); // 좋아요 추가
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
                    latitude.remove(temp); // y 삭제
                    longitude.remove(temp); // x 삭제
                    image_string.remove(temp); // image 삭제
                    histoy_likes.remove(temp); // 좋아요 삭제
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
        longitude.remove(position);// 유적지 설명 삭제
        latitude.remove(position);// 유적지 설명 삭제
        count --;
        favorite_fab.setText(Integer.toString(count));

    }
    private void getData() { // 데이터 가져오는 곳!!!!!!!!!!!
        // 데이터 가져와서 추출 하는 작업!
        GetData task = new GetData();
        task.execute(location);
        Log.d(TAG, "location 내가 선택한 지역 - " + location);
    }

    // DB 연결
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchCourse.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/select_area.php";
            String postParameters = "ADDRESS=" + searchKeyword1;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }
        // 받아온 결과값 나누는거
        private void showResult(){
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    int historic_num = item.getInt("historic_num");
                    double latitude = item.getDouble("latitude"); // 위도
                    double longitude = item.getDouble("longitude"); // 경도
                    String name = item.getString("name");
                    String incident = item.getString("incident");
                    String explain_his = item.getString("explain_his");
                    String address = item.getString("address");
                    String his_source = item.getString("his_source");
                    String his_image = item.getString("his_image");
                    int count_historic = item.getInt("count_historic");

                    //ListContent.setText(name);
                    //thumb_count.setText(Integer.toString(count_historic));
                    //textView.setText(explain_his);

                    SiteData data = new SiteData();

                    data.setImage(his_image);
                    /*
                    Glide.with(getApplicationContext()).asBitmap().load(his_image).placeholder(R.drawable.ic_no_image)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    data.setImage(resource);
                                    //할일

                                }
                            });

                     */

                    data.setLayout_(R.drawable.write_review_back); // background 지정
                    data.setDesc(explain_his); // 내용
                    data.setTitle(name);
                    data.setLike(Integer.toString(count_historic));
                    data.setLatitude(latitude); //y
                    data.setLongitude(longitude); // x
                    data.setAccident_text(incident); // 사건
                    //new DownloadFilesTask().execute(his_image);
                    //data.setImage(new DownloadFilesTask().execute(Listimage.get(i)).get()); // 이미지

                    //data.setImage(new DownloadFilesTask().execute(his_image).get()); // 이미지


                    adapter.addItem(data);


                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
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
