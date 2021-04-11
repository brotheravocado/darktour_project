package com.example.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    TextView thumb_count;
    TextView historic_site;
    ImageView his_picture;
    Activity activity;
    Date date = new Date(); // 현재 날짜
    Calendar cal = Calendar.getInstance(); // 시간 추출
    boolean i = true; // 버튼 눌려졌는지 확인
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;
    //유적지 이름 받아오는 함수/클래스 있어야함
    String his_name = "부산근대역사관";
    GetData task = new GetData();


    private String lon;
    private String lat;
    int count;

    public SiteFragment(String x, String y){ // 생성자
        lon = x;
        lat = y;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);
        //유적지 이름가지고 db 실행
        task.execute(his_name);

        // 유적지 이름 설정 -> 추후 db 가지고와서 수정하기
        historic_site = (TextView) view.findViewById(R.id.location_name);
        //historic_site.setText("제주 4.3 평화공원");
        thumb_count = (TextView) view.findViewById(R.id.thumb_count);
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
                    //db에 접속해서 좋아요 개수 1개 증가
                    InsertHistoricCount insertcount = new InsertHistoricCount();
                    String IP_ADDRESS = "113.198.236.105";
                    insertcount.execute("http://" + IP_ADDRESS + "/insert_count_plus.php", his_name);

                    GetData2 task = new GetData2();
                    task.execute(his_name);
                    i = false;
                }else { // 좋아요 버튼 취소
                    thumb_button.setImageResource(R.drawable.thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정
                    InsertHistoricCount insertcount2 = new InsertHistoricCount();
                    String IP_ADDRESS = "113.198.236.105";
                    insertcount2.execute("http://" + IP_ADDRESS + "/insert_count_minus.php", his_name);

                    GetData2 task = new GetData2();
                    task.execute(his_name);
                    i = true;
                }
            }
        });

        his_picture = (ImageView)view.findViewById(R.id.his_picture);
        //his_picture.setImageResource(R.drawable.test_pic);

        textView  = (TextView) view.findViewById(R.id.text);

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

    // DB 연결
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
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
    // 받아온 결과값 나누는거
    private void showResult(){
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

                historic_site.setText(name);
                thumb_count.setText(Integer.toString(count_historic));
                textView.setText(explain_his);

                new DownloadFilesTask().execute(his_image);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
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
            his_picture.setImageBitmap(result);
        }
    }


    private class GetData2 extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
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
                showResult2();
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

    private void showResult2(){
        try {
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
                String count_historic = item.getString("count_historic");

                thumb_count.setText(count_historic);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}