package com.example.darktour_project;

// 리뷰 쓰기

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class WriteReview extends AppCompatActivity {

    String items1 [] = {"선택","코스","유적지"};

    String none[] = {"선택"};
    public static String course [] = {};
    public static String historic [] = {};

    Spinner spinner_1, spinner_2;

    private static final int REQUEST_CODE = 0;
    private ImageView imageView;

    String user_id = "1";

    int position1 = 0, position2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String IP_ADDRESS = "113.198.236.105";
        Getuserfav getuserfav = new Getuserfav();
        getuserfav.execute("http://" + IP_ADDRESS + "/myfav.php", user_id);

        final String[] inputtype = new String[1];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);
        TextView finish_text = (TextView) findViewById(R.id.finish); // 완료 글자 눌렀을 때
        EditText input_box = (EditText) findViewById(R.id.input); // edittext 입력
        TextView textview  = (TextView) findViewById(R.id.count); // 글자수
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); // edittext누르면 화면에 올라오도록 설정

        spinner_1 = (Spinner)findViewById(R.id.spinner1); // 목록 상자 - 1
        spinner_2 = (Spinner)findViewById(R.id.spinner2); // 목록 상자 - 2

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_1.setSelection(0, false);
        spinner_1.setAdapter(adapter1);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 첫번째 spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 1은 코스 2은 유적지
                // 0은 선택
                position1 = position;
                if(position == 0){
                    spinner_2(none);
                }
                else if(position == 1){
                    inputtype[0] = "코스";
                    spinner_2(course);
                }
                else if(position == 2){
                    inputtype[0] = "유적지";
                    spinner_2(historic);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                position1 = 0;
            }
        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 두번째 spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2 = position;
                if (position1 > 0) {
                    input_box.setEnabled(true);
                    imm.showSoftInput(input_box, InputMethodManager.SHOW_IMPLICIT); // 키보드 보여줌
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        input_box.addTextChangedListener(new TextWatcher() { // editbox listener
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = input_box.getText().toString();
                
                if(input.length() == 200){ // 200자 이면 색깔 빨강으로 경고
                    textview.setTextColor(Color.RED);
                }
                else{
                    textview.setTextColor(Color.BLACK);
                }
                textview.setText(input.length()+" / 200"); // 200자 보이기
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener(){ // 사진 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        finish_text.setOnClickListener(new View.OnClickListener(){ // 완료 글자 눌렀을 때
            @Override
            public void onClick(View v) {
                String coursecode = "0", historicnum = "0";
                if(input_box.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "리뷰를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (position1 > 0) {
                        InsertUserReview addreview = new InsertUserReview();
                        if (position1 == 1) {//코스
                            coursecode = course[position2];
                        } else if (position1 == 2) {
                            historicnum = historic[position2];
                        }
                        addreview.execute("http://" + IP_ADDRESS + "/insert_review.php", user_id, items1[position1], coursecode, historicnum, input_box.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), ReviewFragment.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(getApplicationContext(), "유형을 선택하세요.", Toast.LENGTH_LONG);
                    }
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
    void spinner_2( String  items2 []){
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter2);

    }

    public class Getuserfav extends AsyncTask<String, Void, String> {
        private String TAG = "getmyfav";
        String errorString = null;

        @Override
        protected void onPreExecute() { super.onPreExecute(); }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.d("result : ", result);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String USER_ID = params[1];

            String postParameters = "USER_ID=" + USER_ID;

            Log.d("get user fav : ", postParameters);
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

                Log.d("sb : ", sb.toString().trim());
                getresult(sb.toString());

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "get user fav: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
        public void getresult(String s){
            String TAG_JSON="favorite";
            String TAG_COURSE = "LIKE_COURSE";
            String TAG_HISTORIC = "LIKE_HISTORIC";
            s = s.replaceAll("success", "");
            String Course = null, Historic = null, CourseName = null, HistoricName = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    Course = item.getString(TAG_COURSE);
                    Historic = item.getString(TAG_HISTORIC);
                    Log.d("추출 : ", Course+"+"+Historic);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //WriteReview.items2 = array1.split("/");

            historic = Historic.split(",");
            course = Course.split(",");
            Log.d("str(h) : ", historic.toString());
            Log.d("str(c) : ", course.toString());
        }
    }
}

