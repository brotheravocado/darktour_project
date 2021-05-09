package com.example.darktour_project;
// 메인홈화면
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

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
    VerticalAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    int MAX_ITEM_COUNT = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_home, container, false);

        listImage = new ArrayList<>(); // 이미지 추가
        listImage.add(R.drawable.busan);
        listImage.add(R.drawable.seoul);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);
        listImage.add(R.drawable.jeju);

        viewPager = v.findViewById(R.id.mainhome_viewpager);
        indicator = v.findViewById(R.id.homeindi);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager());
        // ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);
        mVerticalView = v.findViewById(R.id.home_recycler);
        ArrayList<VerticalData> data = new ArrayList<>();

        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (int i = 0; i < listImage.size(); i++) {
            HomeImageFragment imageFragment = new HomeImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", listImage.get(i));
            imageFragment.setArguments(bundle);
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged();
        indicator.setViewPager(viewPager);

        /*int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data.add(new VerticalData(R.mipmap.ic_launcher, i+"번째 데이터"));
            i++;
        }*/
        // init LayoutManager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        // setLayoutManager
        mVerticalView.setLayoutManager(mLayoutManager);
        // init Adapter
        mAdapter = new VerticalAdapter();
        // set Data
        mAdapter.setData(data);
        // set Adapter
        mVerticalView.setAdapter(mAdapter);
        data.add(new VerticalData(R.drawable.busan, "부산"));
        data.add(new VerticalData(R.drawable.jeju, "제주"));
        data.add(new VerticalData(R.drawable.seoul, "서울"));


        return v;
    }

    class FragmentAdapter extends FragmentPagerAdapter {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            holder.description.setText(data.getText());
            holder.icon.setImageResource(data.getImg());

            // setOnClick
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, data.getText(), Toast.LENGTH_SHORT).show();
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

        public VerticalViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);

        }
    }

    class VerticalData {

        private int img;
        private String text;

        public VerticalData(int img, String text) {
            this.img = img;
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public int getImg() {
            return this.img;
        }
    }
}