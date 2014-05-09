package com.zephyrus.testapp.datatestapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.util.JsonReader;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

import java.io.InputStream;
import java.net.*;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Testing URL to string - should move this code back to CarletonEnergyDataSource
        URL url = null;
        try {
            url = new URL("https://rest.buildingos.com/reports/timeseries/?start=2014/05/07+20:00:00&resolution=hour&end=2014/5/07+20:00:00&name=carleton_campus_en_use");
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader reader = new InputStreamReader(in);
            int max_size = 200000;
            char[] buffer = new char[max_size];
            reader.read(buffer, max_size, 0);
            String json_string = new String(buffer);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
