package com.example.darktour_project;

// 상세페이지 - 윤지

 // weather0 - 맑음 sun
 // weather1 - 흐림 cloudy
 // weather2 - 눈 snowman
 // weather3 - 비 rainy

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class DetailPage extends AppCompatActivity  {
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "DEkomlDfGx1Zp0dH%2FHX%2BX1sL6wGeLJvTMDoBr0JIH0SK3bjPdlwtJe8s0N5qnfJYwAX%2BqGlJkf6NxUpbhkxevg%3D%3D";
    WeatherInfoTask weatherTask;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 날짜
    TextView textView;
    static String history_name; // intent된 유적지 이름

    Date date = new Date(); // 현재 날짜
    Calendar cal = Calendar.getInstance(); // 시간 추출
    static String x;
    static String y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        textView = (TextView)findViewById(R.id.text);
        LinearLayout back_image = (LinearLayout) findViewById(R.id.back_selection); // 뒷배경을 위해 선언
      /*  Intent intent = getIntent(); // 데이터 수신

        String location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음
        
        if (location.equals("seoul")){ // 서울
            back_image.setBackgroundResource(R.drawable.seoul_backimage);
        }
        else if (location.equals("jeju")){ // 제주
            back_image.setBackgroundResource(R.drawable.jeju_backimage);
        }
        else{ // 부산
            back_image.setBackgroundResource(R.drawable.busan_backimage);
        } */

        Intent intent =getIntent();

        history_name= intent.getExtras().getString("historyname");

        x = "33.4578142"; // x값
        y = "126.6075751"; // y값
        String add = "&x="+x+"&y="+y+"radius=100";
        // 값 호출


        // ViewPager랑 TabLayout 연동
        ViewPager pager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab);


        pager.setOffscreenPageLimit(1); //현재 페이지의 양쪽에 보유해야하는 페이지 수를 설정 (상황에 맞게 사용하시면 됩니다.) 2개랑 1개 차이를 모르겠어요 그래서 1개함
        tabLayout.setupWithViewPager(pager); //텝레이아웃과 뷰페이저를 연결
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(),this)); //뷰페이저 어뎁터 설정 연결

        // 날씨 api 연동
        //getWeatherInfo();
        

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
                return new SiteFragment(y,x,history_name);
            } else {
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

            String nx = "60";	//위도
            String ny = "127";	//경도
            String baseDate = sdf.format(date);	//조회하고싶은 날짜
            System.out.println(baseDate);

            String pageNo = "1"; // 페이지 수

            //int hour = cal.get(Calendar.HOUR_OF_DAY); // 시간 계산
            //String baseTime = Integer.toString(hour);	//API 제공 시간
            String baseTime = "0200";	//API 제공 시간
            String dataType = "json";	//타입 xml, json
            String numOfRows = "10";	//한 페이지 결과 수

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
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }
    


}