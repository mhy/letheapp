package com.hiphople.letheapp.util;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * TODO write javadoc
 * Created by MHY on 2/27/16.
 */
public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "GCMInstIDListenerS";

    @Override
    public void onTokenRefresh() {
        Log.i(TAG, "onTokenRefresh");
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
