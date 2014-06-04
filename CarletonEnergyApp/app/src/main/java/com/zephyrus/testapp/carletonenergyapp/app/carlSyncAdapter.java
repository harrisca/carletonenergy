package com.zephyrus.testapp.carletonenergyapp.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

// Sync adapter class - unfinished
public class carlSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String PREFS_NAME = "preferences";
    SharedPreferences sharedPref;

    ContentResolver mContentResolver;
    CarletonEnergyDataSource source;
    int units;


    public carlSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();

        Log.i("SyncAdapt: ", "created");


    }
        @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

            //logging
            Log.i("SYNCAdapt: ", "A sync started");
            syncUnThreaded(contentProviderClient);
    }

    public void syncUnThreaded(ContentProviderClient contentProviderClient)  {

        ContentValues contentValues = new ContentValues();
        contentValues.put("currentTemperature", 111);
        contentValues.put("currentWindspeed", 111);
        contentValues.put("liveProduction1", 111);
        contentValues.put("liveProduction2", 111);
        contentValues.put("lastUpdated", "never");

        try {
            contentProviderClient.insert(Uri.parse("dummy"), contentValues);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
