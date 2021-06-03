package com.travel.darktour_project;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class CustomDialogMap extends AppCompatActivity {
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] x; // 경도
    String[] y; // 위도
    int[] start_finish_arr; // 시작 도착지 좌표
    CarFrag carfrag;
    ArrayList finish_course=new ArrayList<String>();

    private Context context;

    public CustomDialogMap(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.mesgase);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                main_label.setText(message.getText().toString());
                Toast.makeText(context, "\"" +  message.getText().toString() + "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
    //
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.map_dialog);
        context=this;

        Intent intent = getIntent();
        titleNumArr=intent.getStringArrayExtra("title");

        x=intent.getStringArrayExtra("x");
        y=intent.getStringArrayExtra("y");

        carfrag=new CarFrag();
        setFrag();
    }
    public void setFrag(){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileSampleDataTwo test = new ProfileSampleDataTwo();
        Bundle bundle = new Bundle();

        for(i =0; i<test.items.size();i++) {
            bundle.putStringArray("title",titleNumArr); // 유적지 이름
            bundle.putStringArray("x", x); // x
            bundle.putStringArray("y", y); // y
            bundle.putIntArray("start_finish_arr", start_finish_arr); // 출발지 도착지 array
        }
        carfrag.setArguments(bundle);
        transaction.replace(R.id.intercourse_map, carfrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
        transaction.commit();
    }
    public void make_course(){ // 코스 생성 - 혜주

        StringBuffer course = new StringBuffer(); // 코스 저장되는 변수
        finish_course.add(titleNumArr[start_finish_arr[0]]); // 출발지 추가
        for(int i =1; i <titleNumArr.length-1; i++){
            finish_course.add(titleNumArr[i]); // 중간 경유지 추가
        }
        finish_course.add(titleNumArr[start_finish_arr[1]]); // 도착지 추가

        for(int i=0; i<finish_course.size(); i++){
            course.append(finish_course.get(i)+ "-");
        }

        String mycourse = course.toString();
        String IP_ADDRESS = "113.198.236.105";
        InsertMycourse insertcourse = new InsertMycourse();
        Log.d("it_check - " , mycourse);
        insertcourse.execute("http://" + IP_ADDRESS + "/insert_course.php", mycourse);
        String USER_ID = PreferenceManager.getString(context, "signup_id");
        UpdateMycourseMypage updatemypage = new UpdateMycourseMypage();
        updatemypage.execute("http://" + IP_ADDRESS + "/update_page_mycourse.php", USER_ID, mycourse);
    }
}
