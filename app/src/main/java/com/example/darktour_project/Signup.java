package com.example.darktour_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
    private EditText signupname;
    private EditText signupemail;
    private EditText signuppw;
    private EditText confirmpw;
    private Button signupbt;
    private TextView name_eroor;
    private TextView email_eroor;
    private TextView pw_eroor;
    private TextView pw_con_eroor;
    private CheckBox ok_box;
    public static int check_for_register = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signupname = (EditText) findViewById(R.id.signup_name);
        signupemail = (EditText) findViewById(R.id.signup_email);
        signuppw = (EditText) findViewById(R.id.signup_password);
        confirmpw = (EditText) findViewById(R.id.signup_confirm_pw);
        signupbt = (Button) findViewById(R.id.signup_bt);
        name_eroor = findViewById(R.id.name_eroor);
        email_eroor = findViewById(R.id.email_eroor);
        pw_eroor = findViewById(R.id.pw_eroor);
        pw_con_eroor = findViewById(R.id.pw_con_eroor);
        ok_box = findViewById(R.id.ok_box);

        // 회원가입 버튼을 눌렀을 때
        signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이름을 입력하지 입력하지 않은 경우
                if (signupname.getText().toString().length() == 0) {
                    name_eroor.setText("이름을 입력하세요");
                    signupname.setBackgroundResource(R.drawable.red_rectangle);
                    signupname.requestFocus();
                    return;
                } else {
                    name_eroor.setText("");
                    signupname.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 이메일을 입력하지 입력하지 않은 경우
                if (signupemail.getText().toString().length() == 0) {
                    email_eroor.setText("이메일을 입력하세요");
                    signupemail.setBackgroundResource(R.drawable.red_rectangle);
                    signupemail.requestFocus();
                    return;
                } else {
                    email_eroor.setText("");
                    signupemail.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호를 입력하지 입력하지 않은 경우
                if (signuppw.getText().toString().length() == 0) {
                    pw_eroor.setText("비밀번호를 입력하세요");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                } else {
                    pw_eroor.setText("");
                    signuppw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호 확인을 입력하지 않은 경우
                if (confirmpw.getText().toString().length() == 0) {
                    pw_con_eroor.setText("비밀번호 확인을 입력하세요");
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.requestFocus();
                    return;
                } else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 비밀번호가 일치하지 않는 경우
                if (!signuppw.getText().toString().equals(confirmpw.getText().toString())) {
                    signuppw.setText("");
                    confirmpw.setText("");
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                } else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                // 약관동의를 체크하지 않은 경우
                /*if (! signuppw.getText().toString().equals(confirmpw.getText().toString())) {
                    signuppw.setText("");
                    confirmpw.setText("");
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                    signuppw.requestFocus();
                    return;
                }
                else {
                    pw_con_eroor.setText("");
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }*/

                //약관동의 안 먹힘.
                if (check_for_register <= 0) {
                    InsertUserData insertdata = new InsertUserData();
                    String IP_ADDRESS = "113.198.236.105";
                    insertdata.execute("http://" + IP_ADDRESS + "/register.php",
                            signupname.getText().toString(), signupemail.getText().toString(), signuppw.getText().toString());

                    Log.d("insert name - ", signupname.getText().toString());
                    Log.d("insert email - ", signupemail.getText().toString());
                    Log.d("insert pwd - ", signuppw.getText().toString());
                }

                Intent intent = new Intent(getApplicationContext(), Interest.class);
                startActivity(intent);
            }
        });
        // 이메일 입력받는 박스
        signupemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email_eroor.setText("올바른 이메일 형식을 입력하세요");
                    signupemail.setBackgroundResource(R.drawable.red_rectangle);
                }
                else {
                    email_eroor.setText("");
                    signupemail.setBackgroundResource(R.drawable.input_rectangle);
                }
            }
        });
        // 비밀번호 입력, 비밀번호 확인 입력 박스
        confirmpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signuppw.getText().toString();
                String confirm = confirmpw.getText().toString();

                if(password.equals(confirm)) {
                    pw_con_eroor.setText("");
                    signuppw.setBackgroundResource(R.drawable.input_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.input_rectangle);
                }
                else {
                    pw_con_eroor.setText("비밀번호가 일치하지 않습니다");
                    signuppw.setBackgroundResource(R.drawable.red_rectangle);
                    confirmpw.setBackgroundResource(R.drawable.red_rectangle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}