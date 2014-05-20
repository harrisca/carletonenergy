package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CarletonEnergyDataSource dataSource = new CarletonEnergyDataSource(this);
        //dataSource.sync();
        //System.out.println(dataSource.getCurrentTemperature());

    }


    public void finishLoading(View view){

        Intent intent = new Intent(this, WindActivity.class);
        startActivity(intent);
    }
}
