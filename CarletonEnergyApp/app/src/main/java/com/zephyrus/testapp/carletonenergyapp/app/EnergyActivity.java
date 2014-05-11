package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class EnergyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);
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
}
