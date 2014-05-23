package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SyncAlerts extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.zephyrus.testapp.carletonenergyapp.app.action.FOO";
    private static final String ACTION_BAZ = "com.zephyrus.testapp.carletonenergyapp.app.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.zephyrus.testapp.carletonenergyapp.app.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.zephyrus.testapp.carletonenergyapp.app.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncAlerts.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SyncAlerts.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public SyncAlerts() {
        super("SyncAlerts");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*
    public void sync()  {

        new Thread(new Runnable() {
            public void run() {
                //System.out.println("In run function");
                currentTemperature = getHulingsTemperature();
                //System.out.println("In run function");
                currentWindspeed = getHulingsWindSpeed();
                //System.out.println("Current Temperature: " + currentTemperature);

                try {
                    syncEnergyData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    syncWeatherData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lastUpdated = new Date();
            }
        }).start();
        //System.out.println(this.currentTemperature);
        //System.out.println(this.currentWindspeed);
    }



    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
    */

}
