package com.example.darktour_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;


public class PublicFrag extends Fragment {
    View view;
    String[] titleNumArr; // 유적지 이름 저장 arr
    ArrayList<String> x ;
    ArrayList<String> y;
    int[] start_finish_arr; // 시작 도착지 좌표
    static MapPolyline polyline ;
    //MapPolyline polylines[] ;
    ArrayList<String> x_ = new ArrayList<String>(); // 경도 -지도
    ArrayList<String> y_ = new ArrayList<String>(); // 위도 -지도
    ArrayList<String> stationName = new ArrayList<String>(); // 정류장이름 -지도
    static int num = 0;
    TextView timeandkm;
    MapView mapView ;
    public ArrayList<MyLocationData> locationarray ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.publicfragment_layout, container, false);

        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.

        if(bundle != null){
            titleNumArr = bundle.getStringArray("title"); //유적지 이름
            x = new ArrayList<String>();; // 경도 -lon 유적지
            y = new ArrayList<String>();; // 위도 -lat 유적지
            Collections.addAll(x,bundle.getStringArray("x"));
            Collections.addAll(y,bundle.getStringArray("y"));
            start_finish_arr = bundle.getIntArray("start_finish_arr"); //start_finish_arr

        }


        // 싱글톤 생성, Key 값을 활용하여 객체 생성
        ODsayService odsayService = ODsayService.init(getContext(), "NX4vSxBft0skkbvCg62G8vP6qDnuvGi9vNDw0rANFJA");
        // 서버 연결 제한 시간(단위(초), default : 5초)
        odsayService.setReadTimeout(5000);
        // 데이터 획득 제한 시간(단위(초), default : 5초)
        odsayService.setConnectionTimeout(5000);
        // API 호출
        mapView = new MapView(getContext());// mapview 연결
        timeandkm = view.findViewById(R.id.time_km); // 이동시간 km 값
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        polyline = new MapPolyline();


        // 콜백 함수 구현
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            // 호출 성공 시 실행
            @Override
            public void onSuccess(ODsayData odsayData, API api) {

        /*polylines = new MapPolyline [titleNumArr.length];
        for(int i=0; i < titleNumArr.length; i++){

            polylines[i] = new MapPolyline();

        }*/


                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(200, 255, 0, 0)); // Polyline 컬러 지정.
                try {
                    // API Value 는 API 호출 메소드 명을 따라갑니다.
                    if (api == API.SEARCH_PUB_TRANS_PATH) {
                        //String stationName = odsayData.getJson().getJSONObject("result").getString("stationName");
                        JSONObject result = odsayData.getJson().getJSONObject("result");
                        JSONArray path = (JSONArray) result.get("path");
                        JSONObject path_0 = (JSONObject) path.get(0);
                        JSONArray subPath = (JSONArray) path_0.get("subPath");
                        ArrayList<JSONObject> realpath = new ArrayList<JSONObject>(); // 세부

                        for(int i=0; i < subPath.length();i++){ //object 넣기

                            realpath.add((JSONObject) subPath.get(i));
                        }
                        JSONObject info = (JSONObject) path_0.get("info");
                        int totalTime = (int) info.get("totalTime"); // 이동시간
                        int trafficDistance = (int) info.get("trafficDistance"); // 이동거리
                        String time = "";
                        String minutes = totalTime % 60 +" 분";
                        if (totalTime / 60 > 0){ // 1시간 이상
                            time = String.format("%d",(int)totalTime / 60) + " 시간 "+ minutes ;
                        }
                        else{
                            time = minutes;
                        }

                        timeandkm.setText("이동거리: "+trafficDistance/1000+" KM "+"이동시간: "+time);


                        for(int i=0; i<=realpath.size(); i++){
                            JSONObject passStopList;
                            try {
                                if ((passStopList = realpath.get(i).getJSONObject("passStopList")) != null) {

                                    JSONArray stations = (JSONArray) passStopList.get("stations");

                                    for (int j = 0; j < stations.length(); j++) {
                                        JSONObject seperate_st = (JSONObject) stations.get(j); // 정류장
                                        stationName.add(seperate_st.getString("stationName"));
                                        x_.add(seperate_st.getString("x"));
                                        y_.add(seperate_st.getString("y"));

                                    }
                                }
                            }catch (Exception e){
                                Log.d("없음", String.valueOf(i));
                            }
                        }


                        for(int i=0; i<x_.size();i++){

                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(y_.get(i)),Double.parseDouble(x_.get(i))));
                        }

                        mapView.addPolyline(polyline);

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                /*if(num==0){
                    polylines[num].setTag(0);
                    polylines[num].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                    polylines[num].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y_.get(0)),Double.parseDouble(x_.get(0)))));
                    polylines[num+1].setTag(1);
                    polylines[num+1].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                    polylines[num+1].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y_.get(y_.size()-1)),Double.parseDouble(x_.get(x_.size()-1)))));
                    mapView.addPolyline(polylines[0]);
                    mapView.addPolyline(polylines[1]);
                }
                else{
                    polylines[num+1].setTag(num+1);
                    polylines[num+1].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                    polylines[num+1].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y_.get(y_.size()-1)),Double.parseDouble(x_.get(x_.size()-1)))));
                    mapView.addPolyline(polylines[num+1]);
                }*/
                //num++;
                // 줌 레벨 변경
                mapViewContainer.addView(mapView);
                mapView.setZoomLevel(20, true);

                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 200; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
            }
            // 호출 실패 시 실행
            @Override
            public void onError(int i, String s, API api) {
                if (api == API.SEARCH_PUB_TRANS_PATH) {
                    Toast.makeText(getContext(), "Error "+ i, Toast.LENGTH_SHORT).show();
                }

            }
        };
        locationarray = new ArrayList<>();
        locationarray.add(new MyLocationData(titleNumArr[start_finish_arr[0]],x.get(start_finish_arr[0]),y.get(start_finish_arr[0]))); // 출발지

        for(int i =0; i < titleNumArr.length; i++){
            if(!(x.get(i).equals(x.get(start_finish_arr[0]))) && !(x.get(i).equals(x.get(start_finish_arr[1])))){
                locationarray.add(new MyLocationData(titleNumArr[i],x.get(i),y.get(i)));
            }
        }
        locationarray.add(new MyLocationData(titleNumArr[start_finish_arr[1]],x.get(start_finish_arr[1]),y.get(start_finish_arr[1]))); // 도착지


        // 2개면 1번 3개면 2번 4개면 3번
        for(int i=0; i < titleNumArr.length-1;i++){
            odsayService.requestSearchPubTransPath(locationarray.get(i).getLon(),locationarray.get(i).getLat(),locationarray.get(i+1).getLon(),locationarray.get(i+1).getLat(),"0","0","0",onResultCallbackListener);
        }



        for(int i=0; i<titleNumArr.length;i++){ // 출발지 도착지 좌표 지정
            MapPOIItem poiItem = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(locationarray.get(i).getLat()),Double.parseDouble(locationarray.get(i).getLon())); // 좌표
            poiItem.setItemName(locationarray.get(i).getName());
            poiItem.setMapPoint(mapPoint);
            if(i ==0){
                poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomImageResourceId(R.drawable.custom_poi_marker_start);
                poiItem.setCustomImageAutoscale(true);
                poiItem.setCustomImageAnchor(0.5f, 1.0f);
                /*polylines[i].setTag(i);
                polylines[i].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                polylines[i].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)),Double.parseDouble(x.get(i)))));*/

            }else if(i == titleNumArr.length-1){
                poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                poiItem.setCustomImageResourceId(R.drawable.custom_poi_marker_end);
                poiItem.setCustomImageAutoscale(true);
                poiItem.setCustomImageAnchor(0.5f, 1.0f);
                /*polylines[i].setTag(i);
                polylines[i].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                polylines[i].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)),Double.parseDouble(x.get(i)))));*/

            }else{
                poiItem.setMarkerType(MapPOIItem.MarkerType.RedPin);
                poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                /*polylines[i].setTag(i);
                polylines[i].setLineColor(Color.argb(200, 128, 128, 128)); // Polyline 컬러 지정. -출발지
                polylines[i].addPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)),Double.parseDouble(x.get(i)))));*/

            }
            //mapView.addPolyline(polylines[i]);
            mapView.addPOIItem(poiItem);
        }


        return view;
    }
    public class MyLocationData { // locationdata 저장 클래스
        private String name;
        private String lon;
        private String lat;

        public MyLocationData(String name,String lon,String lat){
            this.name = name;
            this.lon = lon;
            this.lat = lat;
        }
        public String getLon() {
            return lon;
        }

        public String getLat() {
            return lat;
        }

        public String getName() {
            return name;
        }
    }


}


