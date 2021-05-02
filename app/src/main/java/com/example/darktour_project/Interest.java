package com.example.darktour_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Interest extends AppCompatActivity {
    private CheckBox interest;
    private CheckBox interest2;
    private CheckBox interest3;
    private CheckBox interest4;
    private CheckBox interest5;
    private CheckBox interest6;
    private CheckBox interest7;
    private CheckBox interest8;
    private CheckBox interest9;
    private Button completed;

    private Context mContext;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_history);
        mContext = this;

        interest = (CheckBox) findViewById(R.id.interest);
        interest2 = (CheckBox) findViewById(R.id.interest2);
        interest3 = (CheckBox) findViewById(R.id.interest3);
        interest4 = (CheckBox) findViewById(R.id.interest4);
        interest5 = (CheckBox) findViewById(R.id.interest5);
        interest6 = (CheckBox) findViewById(R.id.interest6);
        interest7 = (CheckBox) findViewById(R.id.interest7);
        interest8 = (CheckBox) findViewById(R.id.interest8);
        interest9 = (CheckBox) findViewById(R.id.interest9);
        completed = (Button) findViewById(R.id.completed_bt);

        // 체크박스 이미지 설정
        interest.setButtonDrawable(R.drawable.select_interest_check);
        interest2.setButtonDrawable(R.drawable.select_interest_check);
        interest3.setButtonDrawable(R.drawable.select_interest_check);
        interest4.setButtonDrawable(R.drawable.select_interest_check);
        interest5.setButtonDrawable(R.drawable.select_interest_check);
        interest6.setButtonDrawable(R.drawable.select_interest_check);
        interest7.setButtonDrawable(R.drawable.select_interest_check);
        interest8.setButtonDrawable(R.drawable.select_interest_check);
        interest9.setButtonDrawable(R.drawable.select_interest_check);

        interest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });
        interest9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedOrNot(isChecked);
            }
        });

        // 선택완료 버튼을 눌렀을 경우
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (count > 0 && count <= 3) {
                    intent.putExtra("it_check", Checked(v));
                    Log.d("it_check - ", Checked(v));

                    String signupid = PreferenceManager.getString(mContext, "signup_id");
                    String history = Checked(v).toString();
                    System.out.println("---------------");
                    System.out.println(signupid);
                    System.out.println(history);
                    System.out.println("---------------");
                    InsertFavorite inserthistory = new InsertFavorite();
                    String IP_ADDRESS = "113.198.236.105";
                    inserthistory.execute("http://" + IP_ADDRESS + "/update_favorite_his.php", signupid, history);

                    startActivity(intent);
                } else if (count == 0) {
                    Toast.makeText(getApplicationContext(),"관심사건을 선택해 주세요!", Toast.LENGTH_LONG).show();
                } else if (count > 3) {
                    Toast.makeText(getApplicationContext(),"3개 이하로 선택해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String Checked(View view) { // 체크되었을 때 동작하는 메소드 구현
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
    }

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
}
