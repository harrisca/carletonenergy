package com.zephyrus.testapp.carletonenergyapp.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class LoadActivity extends Activity {

    private static int SPLASH_TIME_OUT = 5000;

    //not sure if these are needed but supposedly they are required...
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 30L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE *
                    MILLISECONDS_PER_SECOND;

    public static final String AUTHORITY = "com.zephyrus.testapp.carletonenergyapp.app.provider";
    public static final String ACCOUNT_TYPE = "syncAccount";
    public static final String ACCOUNT = "dummyAccount";
    Account mAccount;
    ContentResolver mResolver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);



        // This should be the only time a new CarletonEnergyDataSource is instantiated
        final CarletonEnergyDataSource source = new CarletonEnergyDataSource(this);
        CarletonEnergyDataSource.setSingleton(source);

        source.sync();


        //for syncing...
        Log.i("Load: ", "Starting Sync");
        mAccount = CreateSyncAccount(this);
        mResolver = getContentResolver();
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean( ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean( ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, null, SYNC_INTERVAL);

        Log.i("Load: ", "Sync Successfully started");


        // show splash screen for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static Account CreateSyncAccount(Context context) {

        // Create the account type and default account
        Account newAccount = new Account( ACCOUNT, ACCOUNT_TYPE);


        System.out.println(newAccount);

        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        System.out.println("Load: "+ accountManager.getAccounts());
         // Add the account and account type, no password or user data
         // If successful, return the Account object, otherwise report an error.

        System.out.println("Load: " + accountManager.addAccountExplicitly(newAccount, null, null));

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            System.out.println("uoooooo");

        }
        else {
            Log.i("Load: ", "Failed Sync");
        }


        return newAccount;
    }


}
