package com.example.darktour_project;
// 코스 탐색 화면

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchCourse extends AppCompatActivity implements View.OnClickListener {
    //UI
    Spinner spinner1;
    Spinner spinner2;

    //Adapter
    CourseSearch1Adapter adapterSpinner1;
    CourseSearch2Adapter adapterSpinner2;

    EditText searchview;
    FloatingActionButton favorite_fab ; //fab 버튼

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);
        Intent intent = getIntent(); // 데이터 수신

        String location = intent.getExtras().getString("location"); // 어떤 위치 선택했는지 intent를 통해 받음

        searchview = findViewById(R.id.editSearch);
        favorite_fab = (FloatingActionButton) findViewById(R.id.fab); // fab 선언

        favorite_fab.setOnClickListener(this);

        Switch ai_switch = findViewById(R.id.ai_switch); // ai 버튼


        ai_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // ai 버튼 listener

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // check 되어있을 때
                    Toast.makeText(getApplicationContext(), "button is checked", Toast.LENGTH_SHORT).show();
                } else { // check 안되어있을 때
                    Toast.makeText(getApplicationContext(), "button is not checked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //spinner1 - 지역선택
        set_spinner1();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner1 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                set_spinner2();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //--------------------------------------------------------------------
        // spinner2 - 교통 선택
        set_spinner2();
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // spinner2 클릭 event
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // position 3은 hint라서 쓰지않음

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // spinner1에 위치 설정
        if (location.equals("seoul")) { // 서울
            spinner1.setSelection(0);
        } else if (location.equals("jeju")) { // 제주
            spinner1.setSelection(1);
        } else { // 부산
            spinner1.setSelection(2);
        }

        searchview.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    performSearch();

                    return true;

                }

                return false;
            }

        });




    }
    private void performSearch() {
        searchview.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchview.getWindowToken(), 0);
        //...perform search
    }

    private void set_spinner1() { // spinner1 설정
        //데이터 - 지역선택
        List<String> data1 = new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data1.add("서울"); data1.add("제주"); data1.add("부산"); // spinner1에 넣을 데이터

        //UI생성 spinner1 - 지역선택
        spinner1 = (Spinner)findViewById(R.id.spinner_1); // 지역선택

        //Adapter
        adapterSpinner1 = new CourseSearch1Adapter(this, data1);

        spinner1.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해
        // spinner1.setSelection(0, false); //선택되면
        //Adapter 적용 - 지역
        spinner1.setAdapter(adapterSpinner1);

    }
    private void set_spinner2(){ // spinner2 설정
        //데이터 - 교통선택
        List<String> data2= new ArrayList<>(); // 지역 서울 - 제주 -부산 순서
        data2.add("대중교통"); data2.add("자동차"); data2.add("도보"); data2.add("선택"); // spinner2에 넣을 데이터 마지막이 hint

        //UI생성 spinner2- 교통
        spinner2 = (Spinner)findViewById(R.id.spinner_2); // 교통

        //Adapter
        adapterSpinner2 = new CourseSearch2Adapter(this, data2);

        spinner2.setDropDownVerticalOffset(120); // spinner dropdown 간격 주기위해

        //Adapter 적용 - 교통
        spinner2.setAdapter(adapterSpinner2);
        //spinner2.setSelection(3); //힌트로 세팅
        spinner2.setSelection(adapterSpinner2.getCount()); //힌트로 세팅


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,FavoriteSite.class);
                startActivity(intent);

                break;



        }
    }

}
