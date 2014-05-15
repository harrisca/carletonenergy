package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Help");
        CarletonEnergyDataSource source = new CarletonEnergyDataSource();
        System.out.println("Help2");
        try {
            source.sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(source.getCurrentTemperature());

    }


    public void finishLoading(View view){

        Intent intent = new Intent(this, WindActivity.class);
        startActivity(intent);
    }
}
