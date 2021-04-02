package com.example.darktour_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;

public class Login extends AppCompatActivity {
    private Button kakaologinbutton;
    private Button loginbutton;
    private Button signupbutton;
    private EditText loginemail;
    private EditText loginpassword;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        kakaologinbutton = (Button) findViewById(R.id.kakao_login_bt);
        loginbutton = (Button) findViewById(R.id.login_bt);
        loginemail = (EditText) findViewById(R.id.signup_name);
        loginpassword = (EditText) findViewById(R.id.login_password);

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
                    Toast.makeText(Login.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    loginemail.requestFocus();
                    return;
                }
                // 비밀번호를 입력하지 않은 경우
                if (loginpassword.getText().toString().length() == 0) {
                    Toast.makeText(Login.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    loginpassword.requestFocus();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
