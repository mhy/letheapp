package com.hiphople.letheapp.webview;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
    //private static final String TAG = "LEWebViewClient";
    private static final String ERROR_PAGE = "file:///android_asset/error.html";
    private static final String PATTERN_BOARD_WRITE = ".*dispBoardWrite.*";
    private static final String[] RELEASE_OF_BUG = {"4.4", "4.4.1", "4.4.2"};

    private Activity mAct;
    private ProgressBar mProgBar;

    public LEWebViewClient(Activity activity) {
        mAct = activity;
        mProgBar = (ProgressBar)activity.findViewById(R.id.progressBar);
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        boolean returnValue = false;
        if(url.matches(PATTERN_BOARD_WRITE)){
            boolean isVersionOfBug = false;

            for(String version : RELEASE_OF_BUG){
                if (version.equals(Build.VERSION.RELEASE)){
                    isVersionOfBug = true;
                    break;
                }
            }

            if(isVersionOfBug){
                AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
                builder.setMessage(R.string.specific_kitkat_error_meessage)
                        .setTitle(R.string.specific_kitkat_error_title)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.loadUrl(url);
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                builder.show();

                returnValue = true;
            }
        }
        return returnValue;
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
