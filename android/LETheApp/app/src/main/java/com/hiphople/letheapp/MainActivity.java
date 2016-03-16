package com.hiphople.letheapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hiphople.letheapp.util.GCMRegistrationIntentService;
import com.hiphople.letheapp.util.LePreferences;
import com.hiphople.letheapp.util.LeServerMessage;
import com.hiphople.letheapp.webview.LEWebChromeClient;
import com.hiphople.letheapp.webview.LEWebViewClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private static final String URL_HIPHOPLE = "http://hiphople.com";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private WebView mWebView;
    private LEWebChromeClient mLeWcClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //register the listener for the buttons
        Button btnBack = (Button)findViewById(R.id.btnBack);
        Button btnRefresh = (Button)findViewById(R.id.btnRefresh);
        Button btnForward = (Button)findViewById(R.id.btnForward);
        btnBack.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnForward.setOnClickListener(this);

        //setup for webView
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mLeWcClient = new LEWebChromeClient(this);
        mWebView.setWebChromeClient(mLeWcClient);
        mWebView.setWebViewClient(new LEWebViewClient(this));
        mWebView.loadUrl(URL_HIPHOPLE);

        //setup for GCM
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(LePreferences.SENT_TOKEN_TO_SERVER, false);

                //for TEST
                if (sentToken) {
                    Toast.makeText(context, getString(R.string.gcm_send_message), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getString(R.string.token_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(LePreferences.REGISTRATION_COMPLETE));

        //receive document_srl from push notification only if it exists
        String docSrl = getIntent().getStringExtra(LeServerMessage.DOCUMENT_SRL);
        if(docSrl != null) {
            openPageFromPushNoti(docSrl);
        }

        if(mWebView != null){
            mWebView.onResume();
            mWebView.resumeTimers();
            mLeWcClient.hideSystemUI();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        if(mWebView != null){
            mWebView.onPause();
            mWebView.pauseTimers();
            mLeWcClient.showSystemUI();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_copy_url) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }

            /**
             * if full-screen video is playing, back button will only escape the full screen mode,
             * not go back to previous page.
             */
            if(mLeWcClient.closeCustomViewContainer()){
                return false;
            }else if(mWebView.canGoBack()){
                mWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        switch(btnId){
            case R.id.btnBack:
                if(mWebView.canGoBack())
                    mWebView.goBack();
                break;
            case R.id.btnForward:
                if(mWebView.canGoForward())
                    mWebView.goForward();
                break;
            case R.id.btnRefresh:
                mWebView.reload();
                break;
        }
    }

    //to make a video clip keep playing when there's an orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void openPageFromPushNoti(String docSrl){
        Log.d(TAG, "received document_srl : " + docSrl);
        getIntent().removeExtra(LeServerMessage.DOCUMENT_SRL);
        mWebView.loadUrl(URL_HIPHOPLE + "/6608583"); //TODO remove this hard-coded line; THIS is just for test
        //mWebView.loadUrl(URL_HIPHOPLE + "/" + docSrl); //TODO after removing the line above, make this line alive!
    }
}
