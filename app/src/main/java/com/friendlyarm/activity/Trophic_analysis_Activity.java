package com.friendlyarm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.friendlyarm.LEDDemo.R;

public class Trophic_analysis_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophic_analysis_);

        WebView webView = (WebView)findViewById(R.id.web_view2);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webView.getSettings();
        //适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);//webView默认不支持JavaScript
        settings.setSupportZoom(true);//是否支持缩放
        settings.setBuiltInZoomControls(true);//是否允许缩放
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//在内部显示滚动tiao
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//根据传入的数据再去加载新的网页
                return true;
            }
        });
        webView.loadUrl("http://www.quanyy.com/");
    }
}
