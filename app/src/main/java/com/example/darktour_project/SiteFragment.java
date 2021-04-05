package com.example.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SiteFragment extends Fragment {
    private int num;
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "DEkomlDfGx1Zp0dH%2FHX%2BX1sL6wGeLJvTMDoBr0JIH0SK3bjPdlwtJe8s0N5qnfJYwAX%2BqGlJkf6NxUpbhkxevg%3D%3D";
    WeatherInfoTask weatherTask;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 날짜
    TextView textView;
    Activity activity;
    Date date = new Date(); // 현재 날짜
    Calendar cal = Calendar.getInstance(); // 시간 추출
    boolean i = true; // 버튼 눌려졌는지 확인


    private String lon;
    private String lat;

    public SiteFragment(String x, String y){ // 생성자
        lon = x;
        lat = y;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);
        
        // 유적지 이름 설정 -> 추후 db 가지고와서 수정하기
        TextView historic_site = (TextView) view.findViewById(R.id.location_name);
        //historic_site.setText("북촌리 애기무덤");
        //historic_site.setText("제주시 충혼묘지 (박진경 추도비)");
        historic_site.setText("제주 4.3 평화공원");
        // 좋아요 숫자
        num = 15;
        TextView thumb_count = (TextView) view.findViewById(R.id.thumb_count);
        thumb_count.setText(Integer.toString(num));

        // 좋아요 손가락
        ImageButton thumb_button = (ImageButton) view.findViewById(R.id.thumb_button);
        thumb_button.setOnClickListener(new View.OnClickListener() { // 이미지 버튼 이벤트 정의
            @Override

            public void onClick(View v) { //클릭 했을경우

                // TODO Auto-generated method stub

                //버튼 클릭 시 발생할 이벤트내용
                if (i == true){ // 좋아요 버튼 눌려졌을 때
                    thumb_button.setImageResource(R.drawable.press_thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정
                    num = 16;
                    thumb_count.setText(Integer.toString(num));
                    i = false;
                }else { // 좋아요 버튼 취소
                    thumb_button.setImageResource(R.drawable.thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정
                    num = 15;
                    thumb_count.setText(Integer.toString(num));
                    i = true;
                }


            }

        });


        ImageView his_picture = (ImageView)view.findViewById(R.id.his_picture);
        his_picture.setImageResource(R.drawable.test_pic);

        textView  = (TextView) view.findViewById(R.id.text);
        /*textView.setText("4·3사건으로 인한 제주도 민간인학살과 제주도민의 처절한 삶을 기억하고 추념하며, 화해와 상생의 미래를 열어가기 위한 평화·인권기념공원입니다. " +
                "제주4·3평화공원 조성은 제주4·3사건에 대한 공동체적 보상의 하나로 이루어졌습니다. 1980년대 말 4·3진상규명운동에 매진하던 민간사회단체 등은 진상규명과 함께 지속적으로 위령사업을 요구하였으며 이런 요구에 부응하여 제주도는 1995년 8월 위령공원 조성계획을 발표하였습니다.");*/
        textView.setText("4·3사건으로 인한 제주도 민간인학살과 제주도민의 처절한 삶을 기억하고 추념하며, 화해와 상생의 미래를 열어가기 위한 평화·인권기념공원입니다.\n" +
                "\n" +
                "제주4·3평화공원 조성은 제주4·3사건에 대한 공동체적 보상의 하나로 이루어졌습니다. 1980년대 말 4·3진상규명운동에 매진하던 민간사회단체 등은 진상규명과 함께 지속적으로 위령사업을 요구하였으며 이런 요구에 부응하여 제주도는 1995년 8월 위령공원 조성계획을 발표하였습니다. 1997년 12월 김대중 대통령후보자의 4·3특별법 제정을 통한 진상규명, 위령사업과 보상을 공약 제시, 4·3범도민추진위원회의 4·3위령사업 공청회 실시, 김대중 대통령 제주 방문 시 4·3공원조성관련 특별교부금 지원약속(1999), 특별법 공포(2000) 등이 이어져 2003년 4월 3일 평화공원 기공식이, 2008년 3월 28일 평화기념관이 개관하게 되었습니다.\n" +
                "\n" +
                "4·3사건의 역사적 의미를 되새겨 희생자의 명예회복 및 평화·인권의 의미와 통일의 가치를 되새길 수 있는 평화와 통일의 성지이자 인권교육의 장으로 활용되고 있습니다.");


        TextView review = (TextView)view.findViewById(R.id.move_review); // 하단 리뷰 이동
        review.setText(Html.fromHtml("<u>" + "유적지에 대한 리뷰가 궁금하신가요?" + "</u>")); // 밑줄

        
        // 리뷰 넘어가는 하단 부분 linearlayout 클릭했을 때 화면 intent
        LinearLayout layout_review = (LinearLayout) view.findViewById(R.id.go_review);
        layout_review.setOnClickListener(new View.OnClickListener() { // 클릭 이벤트
            @Override

            public void onClick(View v) { //클릭 했을경우

                Intent intent = new Intent(v.getContext(), SiteArroundReview.class);
                v.getContext().startActivity(intent);

            }

        });


        // 날씨 api 연동
        //getWeatherInfo();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void getWeatherInfo() { // 날씨 api
        if(weatherTask != null) {
            weatherTask.cancel(true);
        }
        weatherTask = new WeatherInfoTask();
        weatherTask.execute();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }
    private class WeatherInfoTask extends AsyncTask<String, String, String> { // 날씨 api
        @Override

        protected String doInBackground(String... params) {

            String nx = "60";	//위도
            String ny = "127";	//경도
            //String baseDate = sdf.format(date);	//조회하고싶은 날짜
            String baseDate = "20210328"; // test
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