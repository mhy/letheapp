package com.hiphople.letheapp.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hiphople.letheapp.R;
import com.hiphople.letheapp.util.LeConstants;

/**
 * TODO write javadoc
 * Created by MHY on 2/28/16.
 */
public class LEWebChromeClient extends WebChromeClient{
    //private static final String TAG = "LEWebChromeClient";
    private Activity mAct;
    private WebView mWebView;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private ProgressBar mProgBar;
    private WebChromeClient.CustomViewCallback mCvCallBack;
    private Window mWindow;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mFilePathCallback;

    private boolean isPlaying;

    public LEWebChromeClient(Activity activity){
        mAct = activity;
        mWebView = (WebView)activity.findViewById(R.id.webView);
        mProgBar = (ProgressBar)activity.findViewById(R.id.progressBar);
        mCustomViewContainer = (FrameLayout)activity.findViewById(R.id.customViewContainer);
        mWindow = activity.getWindow();
        isPlaying = false;
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

        isPlaying = true;
        hideSystemUI();
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

        showSystemUI();
        isPlaying = false;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mProgBar.setProgress(newProgress);
    }

    //This is a callback method for versions from [API level 16] to [API level 18]
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType,
                                String capture) {
        mUploadMsg = uploadMsg;
        startFileChooserActivity(LeConstants.FILE_CHOOSER_REQUEST_CODE_BEFORE_19);
    }

    //This is a callback method for versions after [API level 21]
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        mFilePathCallback = filePathCallback;
        startFileChooserActivity(LeConstants.FILE_CHOOSER_REQUEST_CODE_AFTER_21);
        return true;
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

    /**
     * hide System UI such as Navigation bar and Status bar
     */
    public void hideSystemUI(){
        if(isPlaying) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //for overlaying navigation bar
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            if(Build.VERSION.SDK_INT > 18){
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            mWindow.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * show (recover) System UI that was hidden by calling hideSystemUI()
     */
    public void showSystemUI(){
        if(isPlaying) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            mWindow.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * get a photo to attach from File Chooser (for API level 21 ~)
     */
    public void getDataFromFileChooser(Uri[] resultValue){
        if(mFilePathCallback!=null) {
            mFilePathCallback.onReceiveValue(resultValue);
            mFilePathCallback = null;
        }
    }

    /**
     * get a photo to attach from File Chooser (for API level 16 ~ API level 18)
     */
    public void getDataFromFileChooser(Uri resultValue){
        if(mUploadMsg!=null) {
            mUploadMsg.onReceiveValue(resultValue);
            mFilePathCallback = null;
        }
    }

    /**
     * clean up after File Chooser is closed without attaching anything
     */
    public void cleanUpFileChooser(){
        if(mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
        if(mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;
        }
    }

    private void startFileChooserActivity(int reqCode){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mAct.startActivityForResult(Intent.createChooser(i, "File Chooser"), reqCode);
    }
}