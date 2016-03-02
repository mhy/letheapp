package com.hiphople.letheapp.webview;

import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hiphople.letheapp.R;

/**
 * TODO write javadoc
 * Created by MHY on 2/28/16.
 */
public class LEWebChromeClient extends WebChromeClient{
    private static final String TAG = "LEWebChromeClient";
    private WebView mWebView;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private ProgressBar mProgBar;
    private WebChromeClient.CustomViewCallback mCvCallBack;

    public LEWebChromeClient(Activity activity){
        mWebView = (WebView)activity.findViewById(R.id.webView);
        mProgBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        mCustomViewContainer = (FrameLayout)activity.findViewById(R.id.customViewContainer);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        //super.onShowCustomView(view, callback);
        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        mCustomView = view;
        mCustomViewContainer.addView(mCustomView);
        mCvCallBack = callback;

        mWebView.setVisibility(View.GONE);
        mCustomViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        if (mCustomView == null)
            return;

        mWebView.setVisibility(View.VISIBLE);
        mCustomView.setVisibility(View.GONE);
        mCustomViewContainer.setVisibility(View.GONE);

        // Remove the custom view from its container.
        mCustomViewContainer.removeView(mCustomView);
        mCvCallBack.onCustomViewHidden();

        mCustomView = null;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mProgBar.setProgress(newProgress);
    }

    /**
     *
     * @return true if Custom View is closed successfully
     */
    public boolean closeCustomViewContainer(){
        boolean returnValue = false;
        if(mCustomView != null){
            onHideCustomView();
            returnValue = true;
        }
        return returnValue;
    }
}
