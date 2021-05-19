package com.example.darktour_project;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.Toast;
import static com.kakao.kakaonavi.KakaoNaviService.*;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.kakao.kakaonavi.Destination;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;






import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CarFrag extends Fragment {
    String[] titleNumArr; // 유적지 이름 저장 arr
    String[] x; // 경도 -lon
    String[] y; // 위도 -lat
    int[] start_finish_arr; // 시작 도착지 좌표
    private int position = -1;
    View view;
    private Context context;
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private WebSettings mWebSettings_2; //웹뷰세팅
    private volatile WebChromeClient mWebChromeClient;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.carfrag_layout, container, false);
        context = container.getContext();
        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.

        if(bundle != null){
            titleNumArr = bundle.getStringArray("title"); //유적지 이름
            x = bundle.getStringArray("x"); //x
            y = bundle.getStringArray("y"); //y
            start_finish_arr = bundle.getIntArray("start_finish_arr"); //start_finish_arr
        }



        getAppKeyHash();

        mWebView = view.findViewById(R.id.webView);

        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // Bridge 인스턴스 등록
        //mWebView.addJavascriptInterface(new AndroidBridge(), "android2");

        mWebView.loadUrl("http://113.198.236.105/webview22.html");

        //('change text')"



        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });

        return view;
    }


    public void setWebChromeClient(WebChromeClient client) {
        mWebChromeClient = client;
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
// TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    private class MyWebViewClient extends WebViewClient {
        public static final String INTENT_PROTOCOL_START = "intent:";
        public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
        public static final String INTENT_PROTOCOL_END = ";end;";
        public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

        public void onPageFinished(WebView view,String url){
            mWebSettings_2 = view.getSettings(); //세부 세팅 등록
            mWebSettings_2.setJavaScriptEnabled(true);
            mWebSettings_2.setAllowFileAccess(true);
            mWebSettings_2.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
            mWebSettings_2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
            mWebSettings_2.setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebSettings_2.setDomStorageEnabled(true); // 로컬저장소 허용 여부
            mWebSettings_2.setGeolocationEnabled(true);

            String destination_name = "동의대학교";
            String x_ = "129.0319021";
            mWebView.loadUrl("javascript:navi_no('"+titleNumArr[start_finish_arr[1]]+"','"+ x[start_finish_arr[1]] +"','"+y[start_finish_arr[1]]+"')");
            //mWebView.loadUrl("javascript:navi_no('"+titleNumArr[start_finish_arr[1]]+"','"+ x[start_finish_arr[1]] +"','"+y[start_finish_arr[1]]+"','"+x[start_finish_arr[0]]+"','"+y[start_finish_arr[0]]+"')");
            Log.d("잉1",x[start_finish_arr[1]]);
            Log.d("잉",x[start_finish_arr[0]]);

            //mWebView.loadUrl("javascript:navi('"+x_+"','"+destination_name+"')"); 되는 코드 지우지마라
            //mWebView.loadUrl("javascript:navi('동의대학교')");
            //view.loadUrl("javascript:navi('동의대학교')");
            /*              Log.d(TAG, "onJsAlert(" + view + ", " + url + ", "
                    + message + ", " + result + ")");

출처: https://chiyo85.tistory.com/17 [코딩하는치요맘] */

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            mWebSettings_2 = view.getSettings(); //세부 세팅 등록
            mWebSettings_2.setJavaScriptEnabled(true);
            mWebSettings_2.setAllowFileAccess(true);
            mWebSettings_2.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
            mWebSettings_2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
            mWebSettings_2.setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebSettings_2.setDomStorageEnabled(true); // 로컬저장소 허용 여부
            mWebSettings_2.setGeolocationEnabled(true);
            if (url.startsWith(INTENT_PROTOCOL_START)) {
                final int customUrlStartIndex = INTENT_PROTOCOL_START.length();
                final int customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT);
                if (customUrlEndIndex < 0) {
                    return false;
                } else {
                    final String customUrl = url.substring(customUrlStartIndex, customUrlEndIndex);
                    try {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)));
                    } catch (ActivityNotFoundException e) {
                        final int packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length();
                        final int packageEndIndex = url.indexOf(INTENT_PROTOCOL_END);
                        final String packageName = url.substring(packageStartIndex, packageEndIndex < 0 ? url.length() : packageEndIndex);

                        int idx = customUrl.indexOf("?");
                        String string_back = customUrl.substring(idx+1);

//                        String realUrl = ("https://kakaonavi-wguide.kakao.com/navigate.html?"+string_back).replace("false","true");;
                        String realUrl = ("https://kakaonavi-wguide.kakao.com/navigate.html?"+string_back);
                        Log.d("아잉2", realUrl);
                        view.loadUrl(String.valueOf(Uri.parse(realUrl)));
                    }
                    return true;
                }
            } else {
                return false;
            }


        }
    }

}