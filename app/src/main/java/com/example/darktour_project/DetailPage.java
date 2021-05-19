package com.example.darktour_project;

// 상세페이지 - 윤지

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class DetailPage extends AppCompatActivity  {
    private static String TAG = "detailpage";
    private static final String TAG_JSON="webnautes";
    String mJsonString;
    GetData task = new GetData();

    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "DEkomlDfGx1Zp0dH%2FHX%2BX1sL6wGeLJvTMDoBr0JIH0SK3bjPdlwtJe8s0N5qnfJYwAX%2BqGlJkf6NxUpbhkxevg%3D%3D";
    WeatherInfoTask weatherTask;
    SimpleDateFormat format_ = new SimpleDateFormat("yyyyMMdd"); // 날짜
    TextView textView;
    ImageView weatherimage; // 날씨 사진
    TextView weatherstate; // 날씨 상태

    static String history_name; // intent된 유적지 이름
    Date date = new Date(); // 현재 날짜
    Calendar cal = Calendar.getInstance(); // 시간 추출
    Calendar cal1 = Calendar.getInstance(); // 시간 추출
    static String x;
    static String y;

    // nx = 60 / ny = 127 -> 서울
    // nx = 52 / ny = 38 -> 제주
    // nx = 98 / ny = 76 -> 부산
    
    String[] weather_x = {"60","52","98"}; // x
    String[] weather_y = {"127","38","76"}; // y
    int choice ; // 지역
    /* 0 - 서울
       1 - 제주
       2 - 부산
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        textView = (TextView)findViewById(R.id.text);
        weatherimage = (ImageView)findViewById(R.id.weather);
        weatherstate = (TextView)findViewById(R.id.weather_state);

        LinearLayout back_image = (LinearLayout) findViewById(R.id.back_selection); // 뒷배경을 위해 선언
      /*  Intent intent = getIntent(); // 데이터 수신

        String location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음
        
        if (location.equals("seoul")){ // 서울
            back_image.setBackgroundResource(R.drawable.seoul_backimage);
            choice = 0;
        }
        else if (location.equals("jeju")){ // 제주
            back_image.setBackgroundResource(R.drawable.jeju_backimage);
            choice = 1;
        }
        else{ // 부산
            back_image.setBackgroundResource(R.drawable.busan_backimage);
            choice = 2;
        } */

        Intent intent =getIntent();

        history_name= intent.getExtras().getString("historyname");
        task.execute(history_name);

        back_image.setBackgroundResource(R.drawable.busan_backimage); // 임시로 배경 부산
        choice = 2; // 부산 임시로


        // ViewPager랑 TabLayout 연동
        ViewPager pager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab);


        pager.setOffscreenPageLimit(1); //현재 페이지의 양쪽에 보유해야하는 페이지 수를 설정 (상황에 맞게 사용하시면 됩니다.) 2개랑 1개 차이를 모르겠어요 그래서 1개함
        tabLayout.setupWithViewPager(pager); //텝레이아웃과 뷰페이저를 연결
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(),this)); //뷰페이저 어뎁터 설정 연결

        
        // 날씨 api 연동
        getWeatherInfo();
        

    }
    public void back_button_click(View v){
        super.onBackPressed();
    }

    static class PageAdapter extends FragmentStatePagerAdapter { //뷰 페이저 어뎁터

        PageAdapter(FragmentManager fm, Context context) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) { //프래그먼트 사용 포지션 설정 0 이 첫탭
                Log.d("요롤롤롤롤롤롤롤1", x+y+history_name);
                return new SiteFragment(y,x,history_name);
            } else {
                Log.d("요롤롤롤롤롤롤롤2", x+y+history_name);
                return new ArroundFragment(y,x);
            }
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) { //텝 레이아웃의 타이틀 설정
                return "유적지 정보";
            } else {
                return "주변 음식점";
            }
        }

    }
    private void getWeatherInfo() { // 날씨 api
        if(weatherTask != null) {
            weatherTask.cancel(true);
        }
        weatherTask = new WeatherInfoTask();
        weatherTask.execute();
    }


    private class WeatherInfoTask extends AsyncTask<String, String, String> { // 날씨 api
        @Override

        protected String doInBackground(String... params) {
            // nx = 60 / ny = 127 -> 서울
            // nx = 52 / ny = 38 -> 제주
            // nx = 98 / ny = 76 -> 부산
            String nx = weather_x[choice];	//위도
            String ny = weather_y[choice];	//경도
            cal.add(Calendar.DATE, -1);
            cal1.add(Calendar.DATE, +2); // 다음 날짜 21시 이후
            Date d = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat ( "HH");
            String right_now = format1.format(d);
            String baseDate;
            if(Integer.parseInt(right_now) >=23){
                baseDate = format_.format(cal1.getTime());	//조회하고싶은 날짜
            }
            else{
                baseDate = format_.format(cal.getTime());	//조회하고싶은 날짜
            }



            String pageNo = "1"; // 페이지 수

            //int hour = cal.get(Calendar.HOUR_OF_DAY); // 시간 계산
            //String baseTime = Integer.toString(hour);	//API 제공 시간
            String baseTime = "2300";	//API 제공 시간
            String dataType = "json";	//타입 xml, json
            String numOfRows = "200";	//한 페이지 결과 수

            StringBuilder urlBuilder = new StringBuilder(WEATHER_URL); /*URL*/
            HttpURLConnection conn = null;
            BufferedReader rd = null;
            StringBuilder sb = null;
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+SERVICE_KEY);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));	/* 한 페이지 결과 수 )*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));	/* 페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));	/* 타입 */
                urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
                urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */

                urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
                urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도

                /*각각의 base_time 로 검색 참고자료 참조 : 규정된 시각 정보를 넣어주어야 함 */
                URL url = new URL(urlBuilder.toString());
                Log.d("아잉",urlBuilder.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                System.out.println("Response code: " + conn.getResponseCode());

                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                if(conn != null) {
                    conn.disconnect();
                }
                if(rd != null) {
                    try {
                        rd.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("Debug", sb.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("HH00"); // 현재 시간
            String timestr = sdf.format(cal.getTime()); // 현재 시간

            String time1 = format_.format(d);

            StringBuffer string = new StringBuffer();
            try {
                // 가장 큰 JSONObject를 가져옵니다.
                JSONObject jObject = new JSONObject(sb.toString());

                // response 키를 가지고 데이터를 파싱
                JSONObject parse_response = (JSONObject) jObject.get("response");
                // response 로 부터 body 찾기
                JSONObject parse_body = (JSONObject) parse_response.get("body");
                // body 로 부터 items 찾기
                JSONObject parse_items = (JSONObject) parse_body.get("items");

                // items로 부터 itemlist 를 받기
                JSONArray parse_item = (JSONArray) parse_items.get("item");
                String category;
                JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
                // 카테고리와 값만 받아오기
                String day = "";
                String time = "";
                for (int i = 0; i < parse_item.length(); i++) {
                    weather = (JSONObject) parse_item.get(i);
                    Object fcstValue = weather.get("fcstValue");
                    Object fcstDate = weather.get("fcstDate");
                    Object fcstTime = weather.get("fcstTime");

                    //double형으로 받고싶으면 아래내용 주석 해제
                    //double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
                    category = (String) weather.get("category");
                    // 출력
                    /*if (!day.equals(fcstDate.toString())) {
                        day = fcstDate.toString();
                    }*/
                    int now = Integer.parseInt(timestr);
                    String calTime = null;
                    if((now >=2100)){ // 9시 이상
                        time1 = format_.format(cal1.getTime());	//다음 날짜
                        calTime = "0000";
                    }
                    else{
                        if(now == 000){
                            now += 100;
                        }
                        while(now % 300 != 0){
                            now += 100;
                        }
                        calTime = String.format("%04d",now);

                    }

                    String value = fcstValue.toString();


                    if (time1.equals(fcstDate.toString()) && (category.equals("SKY") || category.equals("PTY")) && calTime.equals(fcstTime.toString())) {

                        if ((category.equals("PTY") && value.equals("0"))||(category.equals("SKY") && (value.equals("1") || value.equals("3") || value.equals("4")))){ // 강수형태가 없을 때
                            if (category.equals("SKY") && value.equals("1")){ // 하늘이 맑고 구름 많음
                                // sun
                                string.append("sun/");
                            }
                            else{
                                // cloudy
                                string.append("cloudy/");
                            }
                        } else if (category.equals("PTY") && (value.equals("1") || value.equals("2") || value.equals("4") || value.equals("5") || value.equals("6"))) {
                            // rainy
                            string.append("rainy/");
                        }
                        else if (category.equals("PTY") && value.equals("3")  && value.equals("7")) {
                            // snowman
                            string.append("snowman/");
                        }

                    }


                }

            }
            catch ( JSONException e) {
                e.printStackTrace();
            }

            return string.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String[] array = s.split("/"); // 날씨들
            String weather = null; // 날씨
            int count = array.length; // 날씨 개수
            Toast.makeText(DetailPage.this, ""+s, Toast.LENGTH_SHORT).show();

            if(count > 1 ){ // 날씨가 두개면 PTY로
                if(Arrays.asList(array).contains("snowman")){
                    int index = Arrays.binarySearch(array,"snowman");
                    weather = array[index];
                }
                else if(Arrays.asList(array).contains("rainy")){
                    int index = Arrays.binarySearch(array,"rainy");
                    weather = array[index];
                }
                else if(Arrays.asList(array).contains("cloudy")){
                    int index = Arrays.binarySearch(array,"cloudy");
                    weather = array[index];
                }
                else if(Arrays.asList(array).contains("sun")){
                    int index = Arrays.binarySearch(array,"sun");
                    weather = array[index];
                }
            }
            else{
                weather = array[0];
            }


            if (weather.equals("cloudy")){ // 흐림
                weatherimage.setImageResource(R.drawable.cloudy);
                weatherstate.setText("흐림");
            }
            else if(weather.equals("sun")){ // 맑음
                weatherimage.setImageResource(R.drawable.sun);
                weatherstate.setText("맑음");
            }
            else if(weather.equals("snowman")){ // 눈
                weatherimage.setImageResource(R.drawable.snowman);
                weatherstate.setText("눈");
            }
            else if(weather.equals("rainy")){ // 비
                weatherimage.setImageResource(R.drawable.rainy);
                weatherstate.setText("비");
            }



        }
    }

    // DB 연결
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DetailPage.this,
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
                try {
                    Log.d(TAG, "all" + mJsonString);

                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        int historic_num = item.getInt("historic_num");
                        double latitude = item.getDouble("latitude");
                        double longitude = item.getDouble("longitude");
                        String name = item.getString("name");
                        String incident = item.getString("incident");
                        String explain_his = item.getString("explain_his");
                        String address = item.getString("address");
                        String his_source = item.getString("his_source");
                        String his_image = item.getString("his_image");
                        int count_historic = item.getInt("count_historic");

                        x=Double.toString(latitude);
                        y=Double.toString(longitude);
                        String area = address.substring(0,2);
                        Log.d(TAG, "융디가 바라는거 : " +x +"/"+y +"/"+ area);
                        Toast.makeText(DetailPage.this, ""+x + "/" + y + "/" + area, Toast.LENGTH_SHORT).show();



                    }
                } catch (JSONException e) {
                    Log.d(TAG, "showResult : ", e);
                }

            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            String serverURL = "http://113.198.236.105/historic_explain.php";
            String postParameters = "NAME=" + searchKeyword1;

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
    }
}