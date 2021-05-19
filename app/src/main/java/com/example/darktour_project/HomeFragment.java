package com.example.darktour_project;
// 메인홈화면
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.kakao.kakaotalk.StringSet.args;

public class HomeFragment extends Fragment {
    View v;
    ViewPager viewPager;
    Timer timer;
    ArrayList<Integer> listImage;

    int currentPage = 0;
    final long DELAY_MS = 3000; // 오토 플립용 타이머 시작 후 해당 시간에 작동(초기 웨이팅 타임) ex) 앱 로딩 후 3초 뒤 플립됨.
    final long PERIOD_MS = 5000; // 5초 주기로 작동
    CircleIndicator indicator; // 이미지 인디케이터
    RecyclerView mVerticalView;
    RecyclerView mVerticalView2;
    VerticalAdapter mAdapter;
    VerticalAdapter mAdapter2;
    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;

    TextView textView; // 글씨굵기
    TextView textView2;
    TextView textView3;
    int page;
    Bundle bundle;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        listImage = new ArrayList<>(); // viewpager 이미지 추가
        listImage.add(R.drawable.busan);
        listImage.add(R.drawable.seoul);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);

        viewPager = v.findViewById(R.id.mainhome_viewpager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager());
        // ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);

        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (page = 0; page < listImage.size(); page++) {
            HomeImageFragment imageFragment = new HomeImageFragment();
            bundle = new Bundle();
            bundle.putInt("imgRes", listImage.get(page));
            imageFragment.setArguments(bundle);
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged();


        // md 추천 코스 인디케이터
        indicator = v.findViewById(R.id.homeindi);
        indicator.setViewPager(viewPager);


        //// 많이 추천된 코스
        mVerticalView = v.findViewById(R.id.home_recycler);
        ArrayList<VerticalData> data = new ArrayList<>();
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(v.getContext());
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
        data.add(new VerticalData("1", R.drawable.seoul, "[서울]","을사늑약 체결"));
        data.add(new VerticalData("2", R.drawable.jeju, "[제주]","제주 4.3 사건"));
        data.add(new VerticalData("3", R.drawable.busan, "[부산]","부산민주공원"));

        //// 많이 추천된 유적지
        mVerticalView2 = v.findViewById(R.id.home_recycler2);
        ArrayList<VerticalData> data2 = new ArrayList<>();
        // init LayoutManager
        mLayoutManager2 = new LinearLayoutManager(v.getContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView2.setLayoutManager(mLayoutManager2);
        // init Adapter
        mAdapter2 = new VerticalAdapter();
        // set Data
        mAdapter2.setData(data2);
        // set Adapter
        mVerticalView2.setAdapter(mAdapter2);
        // 유적지 data 추가
        data2.add(new VerticalData("1", R.drawable.seoul, "[서울]","덕수궁중명전"));
        data2.add(new VerticalData("2", R.drawable.jeju, "[제주]","제주시 충혼묘지 4·3추모비"));
        data2.add(new VerticalData("3", R.drawable.busan, "[부산]","부산민주공원"));


        //MD 글씨 굵게
        textView = v.findViewById(R.id.md_cos);
        String content = textView.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word = "'MD'";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

        //추천한 코스 글씨 굵게
        textView2 = v.findViewById(R.id.rank1);
        String content2 = textView2.getText().toString();
        SpannableString spannableString2 = new SpannableString(content2);

        String word2 = "추천한 코스";
        int start2 = content2.indexOf(word2);
        int end2 = start2 + word2.length();

        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new RelativeSizeSpan(1f), start2, end2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView2.setText(spannableString2);

        //추천한 유적지 글씨 굵게
        textView3 = v.findViewById(R.id.rank2);
        String content3 = textView3.getText().toString();
        SpannableString spannableString3 = new SpannableString(content3);

        String word3 = "추천한 유적지";
        int start3 = content3.indexOf(word3);
        int end3 = start3 + word3.length();

        spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new RelativeSizeSpan(1f), start3, end3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView3.setText(spannableString3);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // MD 추천
    static class FragmentAdapter extends FragmentPagerAdapter {

        // ViewPager에 들어갈 Fragment들을 담을 리스트
        private ArrayList<Fragment> fragments = new ArrayList<>();

        // 필수 생성자
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List에 Fragment를 담을 함수
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final int NUM_PAGES = listImage.size(); // 이미지의 총 갯수

        // Adapter 세팅 후 타이머 실행
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = viewPager.getCurrentItem();
                int nextPage = currentPage + 1;

                if (nextPage >= NUM_PAGES) {
                    nextPage = 0;
                }
                viewPager.setCurrentItem(nextPage, true);
                currentPage = nextPage;
            }
        };

        timer = new Timer(); // thread에 작업용 thread 추가
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    @Override
    public void onPause() {
        super.onPause();
        // 다른 액티비티나 프레그먼트 실행시 타이머 제거
        // 현재 페이지의 번호는 변수에 저장되어 있으니 취소해도 상관없음
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
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
                    .inflate(R.layout.rank_item, parent, false);

            VerticalViewHolder holder = new VerticalViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(VerticalViewHolder holder, int position) {
            final VerticalData data = verticalDatas.get(position);

            // setData
            holder.num.setText(data.getRank());
            holder.icon.setImageResource(data.getImg());
            holder.description.setText(data.getArea());
            holder.name.setText(data.getHistory());

            // 추천 코스 클릭했을때
            // setOnClick
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Debug", data.getArea());
                    Intent intent = new Intent(getActivity(), DetailPage.class);
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

        public TextView num;
        public ImageView icon;
        public TextView description;
        public TextView name;

        public VerticalViewHolder(View itemView) {
            super(itemView);

            num = (TextView) itemView.findViewById(R.id.rank_num);
            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);
            name = (TextView) itemView.findViewById(R.id.horizon_description2);

        }
    }
    //사용자 추천 코스 순위 data
    class VerticalData {

        private String rank;
        private int img;
        private String area;
        private String history;


        public VerticalData(String rank, int img, String area, String history) {
            this.rank = rank;
            this.img = img;
            this.area = area;
            this.history = history;
        }

        public String getRank() { return this.rank;}

        public int getImg() {
            return this.img;
        }

        public String getArea() { return this.area; }

        public String getHistory() { return this.history; }
    }
}