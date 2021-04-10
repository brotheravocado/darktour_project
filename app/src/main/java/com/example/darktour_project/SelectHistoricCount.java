package com.example.darktour_project;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectHistoricCount extends AsyncTask<String, Void, String> {

    private static String TAG = "insert"; // 로그
    private static final String TAG_JSON="webnautes";

    String mJsonString;
    int his_count;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "POST response  - " + result);

        mJsonString = result;
        showResult();
    }

    @Override
    protected String doInBackground(String... params) {
//--------------------------------------------------- changes params
        String serverURL = (String)params[0];
        String NAME = (String)params[1];



        String postParameters = "NAME=" + NAME;

        Log.d("url + historic_name: ", postParameters);

        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

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
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString();


        } catch (Exception e) {
            Log.d(TAG, "InsertData: Error ", e);
            return new String("Error: " + e.getMessage());
        }
    }


    // 받아온 결과값 나누는거
    public void showResult(){
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
                Log.d(TAG, "select 클래스 for문 안 좋아요 개수" + count_historic);
                Log.d(TAG, "url address" + his_image);
                Log.d(TAG, "historic_name" + name);
                getcount(count_historic);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public int getcount(int count_historic){
        Log.d(TAG, "select 클래스 get 함수안 좋아요 개수" + count_historic);
        his_count = count_historic;
        Log.d(TAG, "select 클래스 get 함수안 진짜 좋아요 개수" + count_historic);

        return 0;
    }
    public int sendcount(){
        return his_count;
    }
}