package com.hiphople.letheapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.hiphople.letheapp.util.GCMRegistrationIntentService;
import com.hiphople.letheapp.util.LeConstants;

/**
 * TODO write javadoc
 * Created by MHY on 3/13/16.
 */
public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences mPref;

    private boolean isBoard1CheckedBefore;
    private boolean isBoard2CheckedBefore;
    private boolean isBoard3CheckedBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        new SettingsPreferenceFragment()).commit();

        mPref = getSharedPreferences(
                getPackageName() + LeConstants.DEFAULT_PREF_NAME_SUFFIX,
                MODE_PRIVATE);

        isBoard1CheckedBefore = mPref.getBoolean("board1", false);
        isBoard2CheckedBefore = mPref.getBoolean("board2", false);
        isBoard3CheckedBefore = mPref.getBoolean("board3", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO is there a better logic to subscribe/unsubscribe topic when the setting is changed?
        boolean isBoard1CheckedAfter = mPref.getBoolean("board1", false);
        boolean isBoard2CheckedAfter = mPref.getBoolean("board2", false);
        boolean isBoard3CheckedAfter = mPref.getBoolean("board3", false);

        //if nothing gets changed, do nothing
        if( (isBoard1CheckedBefore != isBoard1CheckedAfter)
                || (isBoard2CheckedBefore != isBoard2CheckedAfter)
                || (isBoard3CheckedBefore != isBoard3CheckedAfter)){
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            stopService(intent);
            startService(intent);
        }
    }

    public static class SettingsPreferenceFragment extends  PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }
}
