package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Intent;


import android.app.Service;

import android.os.IBinder;
import android.util.Log;

/**
 * Service to handle Account sync. This is invoked with an intent with action
 * ACTION_AUTHENTICATOR_INTENT. It instantiates the com.udinic.sync_adapter_example_app.syncadapter and returns its
 * IBinder.
 */
public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static carlSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.i("Service: ", "Created");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                Log.i("Service: ", "syncadapter created");
                sSyncAdapter = new carlSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
