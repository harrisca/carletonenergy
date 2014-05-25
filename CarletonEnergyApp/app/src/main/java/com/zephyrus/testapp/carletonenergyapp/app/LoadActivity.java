package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
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

    private static int SPLASH_TIME_OUT = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // This should be the only time a new CarletonEnergyDataSource is instantiated
        final CarletonEnergyDataSource source = new CarletonEnergyDataSource(this);
        CarletonEnergyDataSource.setSingleton(source);
        source.sync();


        /*
         * This should not be here, but I wanted to test getGraphData() and I'm leaving
         * it for now as an example of how to call it
         */
        /*Calendar start_date = Calendar.getInstance();
        start_date.add(Calendar.YEAR, -1);
        Date start = start_date.getTime();

        Log.i("getGraphData", "about to get graph data");
        ArrayList<Double> graph_data = source.getGraphData("consumption", start, new Date(), "day");
        Log.i("getGraphData", "Final result: " + graph_data);
        graph_data = source.getGraphData("production1", start, new Date(), "quarterhour");
        Log.i("getGraphData", "Final result: " + graph_data);
        */

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
