package com.travel.darktour_project;
// 윤지 상세 유적지 정보 프레그먼트

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SiteFragment extends Fragment {
    private int num;
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
    private static final String SERVICE_KEY = "DEkomlDfGx1Zp0dH%2FHX%2BX1sL6wGeLJvTMDoBr0JIH0SK3bjPdlwtJe8s0N5qnfJYwAX%2BqGlJkf6NxUpbhkxevg%3D%3D";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 날짜
    TextView textView;
    TextView thumb_count;
    TextView historic_site;
    ImageView his_picture;
    Activity activity;
    String IP_ADDRESS = "113.198.236.105";
    Date date = new Date(); // 현재 날짜

    boolean i = true; // 버튼 눌려졌는지 확인
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;
    //유적지 이름 받아오는 함수/클래스 있어야함
    String his_name ;


     String lon;
     String lat;
    int count;

    public SiteFragment(String x, String y,String his_name){ // 생성자
        lon = x;
        lat = y;
        this.his_name = his_name; //검색 결과 유적지


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);

        //유적지 이름가지고 db 실행
        GetData task = new GetData();
        task.execute(his_name);


        // 유적지 이름 설정 -> 추후 db 가지고와서 수정하기
        historic_site = (TextView) view.findViewById(R.id.location_name);
        //historic_site.setText("제주 4.3 평화공원");
        thumb_count = (TextView) view.findViewById(R.id.thumb_count);

        // 좋아요 손가락
        editlike editLike = new editlike();
        editLike.execute("http://" + IP_ADDRESS + "/select.php", "likehistoric", PreferenceManager.getString(getContext(), "signup_id"), his_name);

        ImageButton thumb_button;
        boolean chk = true;
        if(chk){
            thumb_button = (ImageButton) view.findViewById(R.id.thumb_button);
            thumb_button.setImageResource(R.drawable.press_thumbs_up);
        } else {
            thumb_button = (ImageButton) view.findViewById(R.id.thumb_button);
            thumb_button.setImageResource(R.drawable.press_thumbs_up);
        }
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
                    insertcount.execute("http://" + IP_ADDRESS + "/insert_count_plus.php", his_name);
                    editLike.execute("http://" + IP_ADDRESS + "/insert.php", "likehistoric", PreferenceManager.getString(getContext(), "signup_id"), his_name);

                    GetData task = new GetData();
                    task.execute(his_name);
                    i = false;
                }else { // 좋아요 버튼 취소
                    thumb_button.setImageResource(R.drawable.thumbs_up);
                    // db 반영 숫자 들고와야함 - 수정
                    InsertHistoricCount insertcount2 = new InsertHistoricCount();
                    String IP_ADDRESS = "113.198.236.105";
                    insertcount2.execute("http://" + IP_ADDRESS + "/insert_count_minus.php", his_name);
                    editLike.execute("http://" + IP_ADDRESS + "/delete.php", "likehistoric", PreferenceManager.getString(getContext(), "signup_id"), his_name);

                    GetData task = new GetData();
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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }

    // 좋아요 연결
    private class editlike extends AsyncTask<String, Void, String>{

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
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0]; // 그 유적지 이름 받아오는 함수 있어야함
            String TABLE = params[1]; // 그 유적지 이름 받아오는 함수 있어야함
            String USER_ID = params[2]; // 그 유적지 이름 받아오는 함수 있어야함
            String CONTENT = params[3]; // 그 유적지 이름 받아오는 함수 있어야함

            String postParameters = "TABLE=" + TABLE + "&USER_ID=" + USER_ID+ "&CONTENT=" + CONTENT;

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

                Glide.with(this).load(his_image).into(his_picture);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}