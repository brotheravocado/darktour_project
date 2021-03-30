package com.example.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
    ArrayList<Story> al = new ArrayList<Story>(); // 리스트뷰 array
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
        textView.setText("4·3사건으로 인한 제주도 민간인학살과 제주도민의 처절한 삶을 기억하고 추념하며, 화해와 상생의 미래를 열어가기 위한 평화·인권기념공원입니다. " +
                "제주4·3평화공원 조성은 제주4·3사건에 대한 공동체적 보상의 하나로 이루어졌습니다. 1980년대 말 4·3진상규명운동에 매진하던 민간사회단체 등은 진상규명과 함께 지속적으로 위령사업을 요구하였으며 이런 요구에 부응하여 제주도는 1995년 8월 위령공원 조성계획을 발표하였습니다.");

        // 1. 다량의 데이터
        // 2. Adapter
        // 3. AdapterView
        al.add(new Story("리뷰1"));
        al.add(new Story("리뷰2"));
        al.add(new Story("리뷰3"));


        //al.add(new Story("2015.05.11","퇴근합니다~",R.drawable.q));

        // adapter
        MyAdapter adapter = new MyAdapter(
                getActivity().getApplicationContext(), // 현재화면의 제어권자
                R.layout.review_item, al);

        // adapterView - ListView, GridView
       /*ListView lv = (ListView) view.findViewById(R.id.listView1);
        lv.setAdapter(adapter);*/

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
    class NetworkThread extends Thread{
        @Override
        public void run() {
            try{
                String keyword = "category_group_code=FD6&page=1&size=15&x=126.6075751&y=33.4578142&sort=distance";
                // x 경도 y 위도
                //String address = "https://dapi.kakao.com/v2/search/vclip?query="+keyword;
                String address = "https://dapi.kakao.com/v2/local/search/category.json?"+ keyword;

                URL url = new URL(address);
                //접속
                URLConnection conn = url.openConnection();
                //요청헤더 추가
                conn.setRequestProperty("Authorization","KakaoAK 7ce78d3c36644e24fc44fdc6afa0f7f2");

                //서버와 연결되어 있는 스트림을 추출한다.
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                //읽어온다.
                do{
                    str = br.readLine();
                    if(str!=null){
                        buf.append(str);
                    }
                }while(str!=null);

                final String result = buf.toString();


                // 가장 큰 JSONObject를 가져옵니다.
                JSONObject jObject = new JSONObject(result);
                // 배열을 가져옵니다.
                JSONArray jArray = jObject.getJSONArray("documents");
                StringBuffer new_buf = new StringBuffer();
                // 배열의 모든 아이템을 출력합니다.
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    String address_name = obj.getString("address_name");
                    String place_name = obj.getString("place_name");

                    System.out.println("address_name(" + i + "): " + address_name);
                    System.out.println("place_name(" + i + "): " + place_name);
                    new_buf.append(address_name+"\n");
                    new_buf.append(place_name+"\n");
                    System.out.println();
                }
                final String new_result = new_buf.toString();
                getActivity().runOnUiThread (new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(result);
                        Log.d("Test", result);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        ArrayList<Story> al;
        LayoutInflater inf;
        public MyAdapter(Context context, int layout, ArrayList<Story> al) {
            this.context = context;
            this.layout = layout;
            this.al = al;
            this.inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() { // 총 데이터의 개수
            return al.size();
        }
        @Override
        public Object getItem(int position) { // 해당 행의 데이터
            return al.get(position);
        }
        @Override
        public long getItemId(int position) { // 해당 행의 유니크한 id
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inf.inflate(layout, null);

            TextView tv1 = (TextView) convertView.findViewById(R.id.location);


            Story s = al.get(position);
            tv1.setText(s.location);

            return convertView;
        }
    }

    class Story { // listview


        String location;
        public Story( String location) {

            this.location = location;
        }
        public Story() {} // 기본생성자 : 생성자 작업시 함께 추가하자
    }

}