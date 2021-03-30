package com.example.darktour_project;
// 윤지 상세페이지 주변 정보 프래그먼트
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView; //꼭 이 mapview import!

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;


public class ArroundFragment extends Fragment {
    String getPhone []; //전화번호 저장용
    String getURL []; //place_url 저장용
    String getRoad_name []; //도로명 주소 저장용
    String getPlace_name []; // 음식점 이름
    String getX []; // x
    String getY [] ; // y
    MapView mapView; //지도


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_arround, container, false);
         mapView = new MapView(getActivity()); // mapview 연결

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        /* 일반 activity에서는 하단을 윗 부분이 아닌 주석 부분을 이용하길
        	        MapView mapView = new MapView(this);

	        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
	        mapViewContainer.addView(mapView);
         */
        // 중심점 변경 - 예제 좌표는 제주 43평화공원


        double lat = 33.4511596;
        double lon = 126.6167527;

        mapView.setMapCenterPoint(mapPointWithGeoCoord(lat, lon), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(3, true);

        //중심 마커 찍기
        MapPoint MARKER_POINT = mapPointWithGeoCoord(lat, lon);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("제주 43 평화공원");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        // 해당 위치 bluepin 주변 위치 초록pin 누르면 빨간핀
        mapView.addPOIItem(marker);

        NetworkThread thread = new NetworkThread();
        thread.start();
        
        // poiitem 설정을 위해
        mapView.setPOIItemEventListener(mvel);


        return v;
    }

    // 말풍선 눌렀을 때
    MapView.POIItemEventListener mvel = new MapView.POIItemEventListener() {

        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        }

        // 일단은 해당 링크로 이동됨
        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            int tag = mapPOIItem.getTag();
            Intent intent = new Intent(getActivity(),WebViewActivity.class); // 웹뷰
            intent.putExtra("url",getURL[tag-1]); // url 다음 화면에 넘김
            startActivity(intent);
        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    class NetworkThread extends Thread{
        @Override
        public void run() {
            try{
                String keyword = "category_group_code=FD6&page=1&size=15&x=126.6075751&y=33.4578142&sort=distance";
                // x 경도 y 위도

                //String address = "https://dapi.kakao.com/v2/search/vclip?query="+keyword;
                String address = "https://dapi.kakao.com/v2/local/search/category.json?"+ keyword;

                URL url = new URL(address);
                //접속
                URLConnection conn = url.openConnection();
                //요청헤더 추가
                conn.setRequestProperty("Authorization","KakaoAK 7ce78d3c36644e24fc44fdc6afa0f7f2");

                //서버와 연결되어 있는 스트림을 추출한다.
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                StringBuffer buf = new StringBuffer();

                //읽어온다.
                do{
                    str = br.readLine();
                    if(str!=null){
                        buf.append(str);
                    }
                }while(str!=null);

                final String result = buf.toString();

                // 가장 큰 JSONObject를 가져옵니다.
                JSONObject jObject = new JSONObject(result);
                // 배열을 가져옵니다.
                JSONArray jArray = jObject.getJSONArray("documents");
                int list_cnt = jArray.length(); //Json 배열 내 JSON 데이터 개수를 가져옴
                //key의 value를 가져와 저장하기 위한 배열을 생성한다
                getPhone = new String[list_cnt]; //전화번호 저장용
                getURL = new String[list_cnt]; //place_url 저장용
                getRoad_name = new String[list_cnt]; //도로명 주소 저장용
                getPlace_name = new String[list_cnt]; // 음식점 이름
                getX = new String[list_cnt]; // x
                getY = new String[list_cnt]; // y


                // 배열의 모든 아이템
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    getPhone[i] = obj.getString("phone");
                    getURL[i] = obj.getString("place_url");
                    getRoad_name[i] = obj.getString("road_address_name");
                    getPlace_name[i] = obj.getString("place_name");
                    getX[i] = obj.getString("x");
                    getY[i] = obj.getString("y");
                    //Log.d("Test",getX[i]);
                    //Log.d("Test",getY[i]);
                }

                // 주변 마커 추가
                getActivity().runOnUiThread (new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < 15; i++) {

                            MapPOIItem poiItem = new MapPOIItem();
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(getY[i]),Double.parseDouble(getX[i]));
                            poiItem.setItemName(getPlace_name[i]);
                            poiItem.setTag(i+1);
                            poiItem.setMapPoint(mapPoint);
                            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                            // 해당 위치 YellowPin 주변 위치 초록pin 누르면 빨간핀
                            mapView.addPOIItem(poiItem);
                        }


                    }

                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}

