package com.hiphople.letheapp.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hiphople.letheapp.R;

/**
 * TODO create javadoc
 * Created by MHY on 2/28/16.
 */
public class LEWebViewClient extends WebViewClient{
    private static final String TAG = "LEWebViewClient";
    private static final String ERROR_PAGE = "file:///android_asset/error.html";

    private ProgressBar mProgBar;

    public LEWebViewClient(Activity activity) {
        mProgBar = (ProgressBar)activity.findViewById(R.id.progressBar);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
        //return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mProgBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mProgBar.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        view.loadUrl(ERROR_PAGE);
    }
}
