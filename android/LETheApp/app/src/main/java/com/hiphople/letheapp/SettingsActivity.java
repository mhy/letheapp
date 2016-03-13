package com.hiphople.letheapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

/**
 * TODO write javadoc
 * Created by MHY on 3/13/16.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);  //TODO used deprecated method; replace THIS!
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
    }
}
