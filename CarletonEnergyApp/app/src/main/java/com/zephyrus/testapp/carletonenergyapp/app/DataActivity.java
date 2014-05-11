package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;


public class DataActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        if(!orientationCheck()){
            Intent intent = new Intent(this, GraphActivity.class);
            startActivity(intent);
        }

    }

    public void switchWind(View view){
        Intent intent = new Intent(this, WindActivity.class);
        startActivity(intent);
    }

    public void switchData(View view){
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void switchSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void switchEnergy(View view){
        Intent intent = new Intent(this, EnergyActivity.class);
        startActivity(intent);
    }

    public boolean orientationCheck() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return false;
        else
            return true;
    }

}
