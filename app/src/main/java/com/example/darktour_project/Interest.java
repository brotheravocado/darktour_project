package com.example.darktour_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;

public class Interest extends AppCompatActivity {
    RecyclerView mVerticalView;
    RecyclerView mVerticalView2;
    RecyclerView mVerticalView3;
    VerticalAdapter mAdapter;
    VerticalAdapter mAdapter2;
    VerticalAdapter mAdapter3;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;
    LinearLayoutManager mLayoutManager3;
    ArrayList<VerticalData> data = new ArrayList<>();
    ArrayList<VerticalData> data2 = new ArrayList<>();
    ArrayList<VerticalData> data3 = new ArrayList<>();
    private Button completed;

    private Context mContext;

    int count = 0;

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="webnautes";
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_history);
        mContext = this;

        //// 서울 유적지
        mVerticalView = findViewById(R.id.history_recycler_seoul);
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);
        // init Adapter
        mAdapter = new VerticalAdapter();
        // set Data
        mAdapter.setData(data);
        // set Adapter
        mVerticalView.setAdapter(mAdapter);
        // 코스 data 추가
        //GetData task = new GetData();
        //task.execute("서울");

        //// 제주 유적지
        mVerticalView2 = findViewById(R.id.history_recycler_jeju);
        // init LayoutManager
        mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView2.setLayoutManager(mLayoutManager2);
        // init Adapter
        mAdapter2 = new VerticalAdapter();
        // set Data
        mAdapter2.setData(data2);
        // set Adapter
        mVerticalView2.setAdapter(mAdapter2);
        // 코스 data 추가
        //GetData task2 = new GetData();
        //task2.execute("제주");

        //// 부산 유적지
        mVerticalView3 = findViewById(R.id.history_recycler_busan);
        // init LayoutManager
        mLayoutManager3 = new LinearLayoutManager(getApplicationContext());
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView3.setLayoutManager(mLayoutManager3);
        // init Adapter
        mAdapter3 = new VerticalAdapter();
        // set Data
        mAdapter3.setData(data3);
        // set Adapter
        mVerticalView3.setAdapter(mAdapter3);
        // 코스 data 추가
        //GetData task3= new GetData();
        //task3.execute("부산");

        GetData task = new GetData();
        String IP_ADDRESS = "113.198.236.105";
        task.execute();

        // 선택완료 버튼을 눌렀을 경우
        /*completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String history = Checked(v).toString();
                if (count > 0 && count <= 10) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("it_check", Checked(v));
                    Log.d("it_check - ", Checked(v));

                    String signupid = PreferenceManager.getString(mContext, "signup_id");

                    String IP_ADDRESS = "113.198.236.105";
                    InsertFavorite inserthistory = new InsertFavorite();
                    inserthistory.execute("http://" + IP_ADDRESS + "/update_favorite_his.php", signupid, history);
                    Toast.makeText(getApplicationContext(), "관심유적지가 선택되었습니다!", Toast.LENGTH_LONG).show();
                    startActivity(intent);

                } else if (count == 0) {
                    Toast.makeText(getApplicationContext(), "관심유적지 선택해 주세요!", Toast.LENGTH_LONG).show();
                } else if (count > 10) {
                    Toast.makeText(getApplicationContext(), "10개 이하로 선택해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    // 카드뉴스
    class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {

        private ArrayList<VerticalData> verticalDatas;
        private Context context;

        public void setContext(Context context) {
            this.context = context;
        }
        public void setData(ArrayList<VerticalData> list){
            verticalDatas = list;
        }

        @Override
        public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.historic_sites_item, parent, false);

            VerticalViewHolder holder = new VerticalViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(VerticalViewHolder holder, int position) {
            final VerticalData data = verticalDatas.get(position);

            // setData
            //holder.icon.setImageResource(data.getImg());
            Glide.with(Interest.this).load(data.getImg()).into(holder.icon);
            holder.description.setText(data.getArea());
            holder.name.setText(data.getHistory());

            // 추천 코스 클릭했을때
            // setOnClick
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Debug", data.getArea());
                    Intent intent = new Intent(getApplicationContext(), DetailPage.class);
                    intent.putExtra("historyname",data.getHistory()); // 코스이름 DetailPage로 넘김
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return verticalDatas.size();
        }
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView description;
        public TextView name;

        public VerticalViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);
            name = (TextView) itemView.findViewById(R.id.horizon_description2);

        }
    }
    //사용자 추천 코스 순위 data
    class VerticalData {

        private String img;
        private String area;
        private String history;


        public VerticalData(String img, String area, String history) {
            this.img = img;
            this.area = area;
            this.history = history;
        }
        public String getImg() { return this.img; }

        public String getArea() { return this.area; }

        public String getHistory() { return this.history; }
    }

    /*public String Checked(View view) { // 체크되었을 때 동작하는 메소드 구현
        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
        if(interest.isChecked()) {
            resultText += interest.getText().toString() + ", ";
        }
        if(interest2.isChecked()) {
            resultText += interest2.getText().toString() + ", ";
        }
        if(interest3.isChecked()) {
            resultText += interest3.getText().toString() + ", ";
        }
        if(interest4.isChecked()) {
            resultText += interest4.getText().toString() + ", ";
        }
        if(interest5.isChecked()) {
            resultText += interest5.getText().toString() + ", ";
        }
        if(interest6.isChecked()) {
            resultText += interest6.getText().toString() + ", ";
        }
        if(interest7.isChecked()) {
            resultText += interest7.getText().toString() + ", ";
        }
        if(interest8.isChecked()) {
            resultText += interest8.getText().toString() + ", ";
        }
        if(interest9.isChecked()) {
            resultText += interest9.getText().toString() + ", ";
        }
        return resultText; // 체크된 값 리턴
    }*/

    // 체크된 체크박스의 개수 count
    private  void isCheckedOrNot(boolean isChecked) {
        if (isChecked) {
            count ++;
        } else {
            if (count > 0) {
                count --;
            }
        }
    }

    // DB 연결
    private class GetData extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Interest.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null) {
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            //String searchKeyword1 = params[0]; // 그 유적지 이름 받아오는 함수 있어야함

            //String serverURL =  params[0];
            //String postParameters = params[1];

            try {

                String IP_ADDRESS = "113.198.236.105";
                //task.execute("http://" + IP_ADDRESS + "/select_all_historic.php");

                URL url = new URL("http://" + IP_ADDRESS + "/select_all_historic.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

               // OutputStream outputStream = httpURLConnection.getOutputStream();
                //outputStream.write(postParameters.getBytes("UTF-8"));
                //outputStream.flush();
                //outputStream.close();

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
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }

        }

        // 받아온 결과값 나누는거
        private void showResult() {
            try {
                Log.d(TAG, "all" + mJsonString);

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String name = item.getString("name");
                    String incident = item.getString("incident");
                    String his_image = item.getString("his_image");
                    String address = item.getString("address");

                    if(address.substring(0,2).equals("서울")){
                        data.add(new VerticalData(his_image, incident, name));
                    }

                    else if(address.substring(0,2).equals("제주")){
                        data2.add(new VerticalData(his_image, incident, name));
                    }
                    else if(address.substring(0,2).equals("부산")){
                        data3.add(new VerticalData(his_image, incident, name));
                    }
                }
                mAdapter.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                mAdapter3.notifyDataSetChanged();



            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }
}