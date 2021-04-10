package com.example.darktour_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Getuserfav extends AsyncTask<String, Void, String> {
    private static String TAG = "getmyfav";
    String errorString = null;

    @Override
    protected void onPreExecute() { super.onPreExecute(); }


    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        Log.d("result : ", result);
        Log.d("strinbg : ", String.valueOf(WriteReview.items2.length));
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String)params[0];
        String user_num = params[1];
        String input_type = params[2];

        String postParameters = "user_num=" + user_num + "&input_type=" + input_type;

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
            WriteReview.items2 =  getresult(sb.toString());

            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "get user fav: Error ", e);
            return new String("Error: " + e.getMessage());
        }

    }
    public String[] getresult(String s){
        s = s.replaceAll("success", "");
        String array1 = null;
        try {
            JSONObject jsonObject = new JSONObject(s);
             JSONArray jsonArray = jsonObject.getJSONArray("favorite");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                array1 = item.getString("like_course");
                Log.d("추출 : ", array1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //WriteReview.items2 = array1.split("/");

        String[] str = array1.split("/");
        Log.d("str : ", String.valueOf(str.length));
        return str;
    }
}