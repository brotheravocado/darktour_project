package com.travel.darktour_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {
    View v;
    static TextView favoritecourse;
    ImageButton setting;
    private LinearLayoutManager mLayoutManger;
    private LinearLayoutManager mLayoutManger2;
    private LinearLayoutManager mLayoutManger3;
    private LinearLayoutManager mLayoutManger4;

    private ProfileAdapter adapter = new ProfileAdapter();
    private ProfileAdapter2 adapter2 = new ProfileAdapter2();
    private ProfileAdapter3 adapter3 = new ProfileAdapter3();
    private ProfileAdapter4 adapter4 = new ProfileAdapter4();

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    RecyclerView recyclerView4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setting = v.findViewById(R.id.imageButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);

        //recycleView 초기화
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView2 = v.findViewById(R.id.recycler_view2);
        recyclerView3 = v.findViewById(R.id.recycler_view3);
        recyclerView4 = v.findViewById(R.id.recycler_view4);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mLayoutManger=new LinearLayoutManager(v.getContext());
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManger2=new LinearLayoutManager(v.getContext());
        mLayoutManger2.setOrientation(LinearLayoutManager.VERTICAL);

        mLayoutManger3=new LinearLayoutManager(v.getContext());
        mLayoutManger3.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManger4=new LinearLayoutManager(v.getContext());
        mLayoutManger4.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView2.setLayoutManager(mLayoutManger2);
        recyclerView3.setLayoutManager(mLayoutManger3);
        recyclerView4.setLayoutManager(mLayoutManger4);

        recyclerView.setAdapter(adapter);
        recyclerView3.setAdapter(adapter3);
        recyclerView4.setAdapter(adapter4);

        //아이템 로드
        adapter.setItems(new ProfileSampleData("likehistoric", PreferenceManager.getString(getContext(), "signup_id")).getItems());
        adapter2.setItems(new ProfileSampleDataTwo().getItems());
        adapter3.setItems(new ProfileSampleDataThree().getItems());
        adapter4.setItems(new ProfileSampleDataFour().getItems());

        recyclerView2.setAdapter(adapter2);
        favoritecourse=(TextView) v.findViewById(R.id.favoriteCourse);
        adapter2.setOnItemClicklistener(new OnFCItemClickListener() {
            @Override
            public void onItemClick(ProfileAdapter2.ViewHolder holder, View view, int position) {
                Profile2 item =adapter2.getItem(position);
                Toast.makeText(getContext(),item.getFavoriteCourse(),
                        Toast.LENGTH_LONG).show();
                CustomDialog customDialog=new CustomDialog(getContext());
                customDialog.callFunction(favoritecourse);

            }
        });
        return v;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




    public class GetReview extends AsyncTask<String, Void, String> {
        private static final String TAG_JSON = "review";

        String errorString = null;
        ProgressDialog progressDialog;
        String USER_ID; // review 타입 -유적지 / 코스
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d("result : ", result);

            if (result == null){
            }
            else {

                showResult(result);
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://113.198.236.105/getreview.php";
            USER_ID = (String) params[0];

            String postParameters = "USER_ID=" + USER_ID;

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
                //showResult(REVIEW_TYPE, sb.toString().trim());

                return sb.toString().trim();
            } catch (Exception e) {

                Log.d(TAG, "getReivew: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }

        private void showResult(String mJsonString) {
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
/*
                    if (REVIEW_TYPE == "유적지") {
                        data.setTitle(historicnum);
                        data.setTag_color(R.color.site_pink);
                        data.setCategory("유적지");
                    } else if (REVIEW_TYPE == "코스") {
                        data.setTitle(coursecode);
                        data.setTag_color(R.color.course_blue);
                        data.setCategory("코스");
                    }

                    // 각 값이 들어간 data를 adapter에 추가합니다.
                    adapter.addItem(data);*/
                }

                // adapter의 값이 변경되었다는 것을 알려줍니다.
                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                Log.d(TAG, "showReviewResult : ", e);
            }
        }
    }
}


