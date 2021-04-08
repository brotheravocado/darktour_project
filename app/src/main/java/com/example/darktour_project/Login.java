package com.example.darktour_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;

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
import java.util.HashMap;

public class Login extends AppCompatActivity {
    private Button kakaologinbutton;
    private Button loginbutton;
    private Button signupbutton;
    private EditText loginemail;
    private EditText loginpassword;
    private TextView login_email_eroor;
    private TextView login_pw_eroor;
    private SessionCallback sessionCallback = new SessionCallback();

    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;
    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="login";
    private static final String TAG_ID = "id";
    //private static final String TAG_NAME = "name";
    private static final String TAG_PWD ="pwd";



    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        kakaologinbutton = (Button) findViewById(R.id.kakao_login_bt);
        loginbutton = (Button) findViewById(R.id.login_bt);
        loginemail = (EditText) findViewById(R.id.signup_name);
        loginpassword = (EditText) findViewById(R.id.login_password);
        login_email_eroor = (TextView) findViewById(R.id.login_email_eroor);
        login_pw_eroor = (TextView) findViewById(R.id.login_pw_eroor);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        // 카카오 로그인 버튼을 눌렀을 때
        kakaologinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, Login.this);
            }

        });
        //로그인 버튼을 눌렀을 때
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 아이디를 입력하지 않은 경우
                if (loginemail.getText().toString().length() == 0) {
                    login_email_eroor.setText("아이디를 입력하세요");
                    loginemail.setBackgroundResource(R.drawable.red_rectangle);
                    loginemail.requestFocus();
                    return;
                } else {
                    login_email_eroor.setText("");
                    loginemail.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호를 입력하지 않은 경우
                if (loginpassword.getText().toString().length() == 0) {
                    login_pw_eroor.setText("비밀번호를 입력하세요");
                    loginpassword.setBackgroundResource(R.drawable.red_rectangle);
                    loginpassword.requestFocus();
                    return;
                } else {
                    login_pw_eroor.setText("");
                    loginpassword.setBackgroundResource(R.drawable.input_rectangle);
                }

                mArrayList.clear();
                GetData task = new GetData();
                task.execute( loginemail.getText().toString(), loginpassword.getText().toString());

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });

        mArrayList = new ArrayList<>();


        // 회원가입 버튼을 눌렀을 때
        signupbutton = (Button) findViewById(R.id.signup_bt);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), Interest.class);
        startActivity(intent);
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                Toast.makeText(getApplicationContext(),"로그인 실패!", Toast.LENGTH_LONG).show();
                //mTextViewResult.setText(errorString);
            }
            else {

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
               // mJsonString = result;
               // showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://113.198.236.105//login4.php";
            String postParameters = "USER_ID=" + searchKeyword1 + "&USER_PWD=" + searchKeyword2;


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
                    if(line.equalsIgnoreCase("User Found")) {
                        Intent intent = new Intent(getApplicationContext(), Interest.class);
                        startActivity(intent);
                    }
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

}


