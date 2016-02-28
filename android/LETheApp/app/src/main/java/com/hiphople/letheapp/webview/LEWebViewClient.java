package com.hiphople.letheapp.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * TODO create javadoc
 * Created by MHY on 2/28/16.
 */
public class LEWebViewClient extends WebViewClient{
    private static final String TAG = "LEWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
        //return super.shouldOverrideUrlLoading(view, url);
    }
}
