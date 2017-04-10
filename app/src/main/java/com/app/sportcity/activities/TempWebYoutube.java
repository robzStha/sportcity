package com.app.sportcity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.sportcity.R;

public class TempWebYoutube extends AppCompatActivity {
WebView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_new);

        String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/Ry8fFmON_GY?autoplay=0&loop=0&rel=0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
//<iframe width='425' height='344' src='www.youtube.com/embed/Ry8fFmON_GY?autoplay=0&loop=0&rel=0' frameborder='0'></iframe>
        WebView displayYoutubeVideo = (WebView) findViewById(R.id.wb_temp);
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

//        temp = (WebView) findViewById(R.id.wb_temp);
//        temp.setWebChromeClient(new WebChromeClient(){});
//        temp.getSettings().setJavaScriptEnabled(true);
//        String temp1 = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/Ry8fFmON_GY?autoplay=0&loop=0&rel=0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
//
////        String temp1 = "<Html><Head><style>img{display: inline;height: auto;max-width: 100%;}</style></Head><Body><iframe width='425' height='344' src='www.youtube.com/embed/Ry8fFmON_GY?autoplay=0&loop=0&rel=0' frameborder='0'></iframe></body></html>";
//        temp.loadData(temp1, "text/html", null);
//        System.out.println("Temp: "+temp1);
    }
}
