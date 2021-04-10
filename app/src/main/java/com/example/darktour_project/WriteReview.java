package com.example.darktour_project;

// 리뷰 쓰기

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class WriteReview extends AppCompatActivity {

    String items1 [] = {"선택","코스","유적지"};

    public static String items2 [] = {"선택","코스","유적지"};

    Spinner spinner_1, spinner_2;

    private static final int REQUEST_CODE = 0;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String IP_ADDRESS = "113.198.236.105";
        final String[] inputtype = new String[1];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);
        TextView finish_text = (TextView) findViewById(R.id.finish); // 완료 글자 눌렀을 때
        EditText input_box = (EditText) findViewById(R.id.input); // edittext 입력
        TextView textview  = (TextView) findViewById(R.id.count); // 글자수
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); // edittext누르면 화면에 올라오도록 설정

        spinner_1 = (Spinner)findViewById(R.id.spinner1); // 목록 상자 - 1
        spinner_2 = (Spinner)findViewById(R.id.spinner2); // 목록 상자 - 2

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setSelection(0, false);

        spinner_1.setAdapter(adapter1);

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 첫번째 spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position 1은 코스 2은 유적지
                // 0은 선택
                if(position == 1){
                    inputtype[0] = "코스";

                    Getuserfav getuserfav = new Getuserfav();
                    getuserfav.execute("http://" + IP_ADDRESS + "/myfav.php","1",inputtype[0]);

                    //!!=========================================================================
                    //String items2 [] = new String[10]; //좋아요할 수 있는 최대 개수로 사이즈 만들기/
                    Log.d("strinwr : ", String.valueOf(items2.length));
                    spinner_2(items2);
                }
                else if(position == 2){
                    inputtype[0] = "유적지";

                    Getuserfav getuserfav = new Getuserfav();
                    getuserfav.execute("http://" + IP_ADDRESS + "/myfav.php","1",inputtype[0]);

                    //!!=========================================================================
                    //String items2 [] = getuserfav.getresult(); //좋아요할 수 있는 최대 개수로 사이즈 만들기/
                    spinner_2(items2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 두번째 spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                    if (position > 0){
                        input_box.setEnabled(true);
                        imm.showSoftInput(input_box, InputMethodManager.SHOW_IMPLICIT); // 키보드 보여줌
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        input_box.addTextChangedListener(new TextWatcher() { // editbox listener
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = input_box.getText().toString();
                
                if(input.length() == 200){ // 200자 이면 색깔 빨강으로 경고
                    textview.setTextColor(Color.RED);
                }
                else{
                    textview.setTextColor(Color.BLACK);
                }
                textview.setText(input.length()+" / 200"); // 200자 보이기
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener(){ // 사진 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        finish_text.setOnClickListener(new View.OnClickListener(){ // 완료 글자 눌렀을 때
            @Override
            public void onClick(View v) {
                ;
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
    void spinner_2( String  items2 []){
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter2);

    }
}