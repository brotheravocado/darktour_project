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
    private int position = -1;
    View view;
    private Context context;
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private volatile WebChromeClient mWebChromeClient;
    @Nullable
    @Override
    @JavascriptInterface
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.carfrag_layout, container, false);
        context = container.getContext();

        getAppKeyHash();

        mWebView = view.findViewById(R.id.webView);
        //mWebView.getSettings().setJavaScriptEnabled(true);

        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebSettings.setGeolocationEnabled(true);


//        mWebView.loadUrl("file:///android_asset/exam_html");
        mWebView.loadUrl("http://113.198.236.105/webview.html");
        mWebView.setWebViewClient(new MyWebViewClient());
        //mWebView.loadUrl("http://naver.com");
        //url 스킴
        //mWebView.loadUrl("https://kakaonavi-wguide.kakao.com/navigate.html?appkey=7007683e4564ff159abb86bf3b46afc0&apiver=1.0&extras=%7B%22KA%22%3A%22sdk%2F1.39.15%20os%2Fjavascript%20sdk_type%2Fjavascript%20lang%2Fko-KR%20device%2FLinux_armv8l%20origin%2Fhttp%253A%252F%252F113.198.236.105%22%7D&param=%7B%22destination%22%3A%7B%22name%22%3A%22%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90%20%ED%8C%90%EA%B5%90%EC%A0%90%22%2C%22x%22%3A127.11205203011632%2C%22y%22%3A37.39279717586919%7D%2C%22option%22%3A%7B%22coord_type%22%3A%22wgs84%22%2C%22vehicle_type%22%3A1%2C%22rpoption%22%3A100%2C%22route_info%22%3Atrue%7D%7D");
        // 웹을 더 쾌적하게 돌리기 위한 세팅
        //mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });









        //mWebView.loadUrl("javascript:exam_script.navi()"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
       /* mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //view.loadUrl(request.getUrl().toString());
                String url = String.valueOf(request.getUrl());
                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = context.getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (url != null && url.startsWith("market://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                view.loadUrl(url);
                return true;


            }

        });*/







        //Log.i("url1",mWebView.getUrl());


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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith(INTENT_PROTOCOL_START)) {
                final int customUrlStartIndex = INTENT_PROTOCOL_START.length();
                final int customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT);
                if (customUrlEndIndex < 0) {
                    return false;
                } else {
                    final String customUrl = url.substring(customUrlStartIndex, customUrlEndIndex);
                    try {
                        //view.loadUrl(url);
//                        Log.d("아잉", String.valueOf(Uri.parse(url)));

                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)));
                    } catch (ActivityNotFoundException e) {
                        final int packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length();
                        final int packageEndIndex = url.indexOf(INTENT_PROTOCOL_END);
                        final String packageName = url.substring(packageStartIndex, packageEndIndex < 0 ? url.length() : packageEndIndex);
                        Log.d("아잉2", packageName);
                        int idx = customUrl.indexOf("?");
                        String string_back = customUrl.substring(idx+1);
                        String realUrl = "https://kakaonavi-wguide.kakao.com/navigate.html?"+string_back;

                        //Toast.makeText(context, ""+packageName, Toast.LENGTH_SHORT).show();

                        //mWebView.loadUrl("http://113.198.236.105/webview.html/#");
                        mWebView.loadUrl(String.valueOf(Uri.parse(realUrl)));
                        //mWebView.loadUrl("https://kakaonavi-wguide.kakao.com/navigate.html?appkey=7007683e4564ff159abb86bf3b46afc0&apiver=1.0&extras=%7B%22KA%22%3A%22sdk%2F1.39.15%20os%2Fjavascript%20sdk_type%2Fjavascript%20lang%2Fko-KR%20device%2FLinux_armv8l%20origin%2Fhttp%253A%252F%252F113.198.236.105%22%7D&param=%7B%22destination%22%3A%7B%22name%22%3A%22%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90%20%ED%8C%90%EA%B5%90%EC%A0%90%22%2C%22x%22%3A127.11205203011632%2C%22y%22%3A37.39279717586919%7D%2C%22option%22%3A%7B%22coord_type%22%3A%22wgs84%22%2C%22vehicle_type%22%3A1%2C%22rpoption%22%3A100%2C%22route_info%22%3Afalse%7D%7D");

                        //getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kakaonavi-wguide.kakao.com/navigate.html?appkey=7007683e4564ff159abb86bf3b46afc0&apiver=1.0&extras=%7B%22KA%22%3A%22sdk%2F1.39.15%20os%2Fjavascript%20sdk_type%2Fjavascript%20lang%2Fko-KR%20device%2FLinux_armv8l%20origin%2Fhttp%253A%252F%252F113.198.236.105%22%7D&param=%7B%22destination%22%3A%7B%22name%22%3A%22%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90%20%ED%8C%90%EA%B5%90%EC%A0%90%22%2C%22x%22%3A127.11205203011632%2C%22y%22%3A37.39279717586919%7D%2C%22option%22%3A%7B%22coord_type%22%3A%22wgs84%22%2C%22vehicle_type%22%3A1%2C%22rpoption%22%3A100%2C%22route_info%22%3Afalse%7D%7D")));
                        //getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse()));
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

}