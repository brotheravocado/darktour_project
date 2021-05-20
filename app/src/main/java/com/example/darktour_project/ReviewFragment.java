package com.example.darktour_project;
// 리뷰 recycler
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ReviewFragment extends Fragment {
    View v;
    // adapter에 들어갈 list 입니다.
    private ReviewRecyclerAdapter adapter;
    String getId[]; //id
    String getReview[]; // review
    String getImage[]; // image
    String getTitle[]; // title
    String getLike[]; // counting like

    List<String> Listid; // id
    List<String> Listreview; // review
    List<String> Listimage; // image
    List<String> Listtitle; // title
    List<String> Listlike; // like

    boolean i = true; // 버튼 눌려졌는지 확인

    private int num; //버튼에 따른 좋아요 숫자 확인하기 위해서 넣은 변수로 나중에 변경 혹은 삭제 해야함

    // https://black-jin0427.tistory.com/222
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_review, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner); // 목록 상자
        ImageButton write = (ImageButton) v.findViewById(R.id.write_review); // 리뷰 쓰기 버튼

        ReviewFragment.GetReview task = new ReviewFragment.GetReview();
        task.execute("유적지");

        init();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 0은 코스 1은 유적지
                if (position == 0) {
                    ReviewFragment.GetReview task = new ReviewFragment.GetReview();
                    task.execute("코스");
                } else if (position == 1) {
                    ReviewFragment.GetReview task = new ReviewFragment.GetReview();
                    task.execute("유적지");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        write.setOnClickListener(new View.OnClickListener() { // 리뷰 쓰기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteReview.class); // 리뷰 쓰기 화면
                // id 보내야하나? 보내야하는디
                startActivity(intent);
            }
        });
        return v;
    }

    private void init() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(adapter);

    }

    public class GetReview extends AsyncTask<String, Void, String> {
        private static final String TAG_JSON = "review";

        String errorString = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result : ", result);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://113.198.236.105/getreview.php";
            String REVIEW_TYPE = (String) params[0];

            String postParameters = "REVIEW_TYPE=" + REVIEW_TYPE;

            Log.d("getreveiw : ", postParameters);

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                Log.d("sb : ", sb.toString().trim());
                showResult(REVIEW_TYPE, sb.toString().trim());

                return sb.toString().trim();
            } catch (Exception e) {

                Log.d(TAG, "getReivew: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }

        private void showResult(String REVIEW_TYPE, String mJsonString) {
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String reveiwnum = item.getString("REVIEW_NUM");
                    Log.d(TAG, "REVIEW_NUM" + reveiwnum);
                    String userid = item.getString("USER_ID");
                    String reviewtype = item.getString("REVIEW_TYPE");
                    String coursecode = item.getString("COURSE_CODE");
                    String historicnum = item.getString("HISTORIC_NUM");
                    String reviewcontent = item.getString("REVIEW_CONTENT");
                    String countreview = item.getString("COUNT_REVIEW");
                    String his_image = item.getString("HIS_IMAGE");

                    // 각 List의 값들을 data 객체에 set 해줍니다.
                    ReviewData data = new ReviewData();
                    data.setId(userid);
                    data.setReview(reviewcontent);
                    data.setLike(countreview);
                    data.setThumb_image(R.drawable.thumbs_up);// 따봉
                    data.setImage(his_image); // 리뷰사진

                    if (REVIEW_TYPE == "유적지") {
                        data.setTitle(historicnum);
                        data.setTag_color(R.color.site_pink);
                    } else if (REVIEW_TYPE == "코스") {
                        data.setTitle(coursecode);
                        data.setTag_color(R.color.course_blue);
                    }

                    // 각 값이 들어간 data를 adapter에 추가합니다.
                    adapter.addItem(data);
                }

                // adapter의 값이 변경되었다는 것을 알려줍니다.
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d(TAG, "showReviewResult : ", e);
            }
        }
    }
}


