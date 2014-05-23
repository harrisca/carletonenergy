package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class EnergyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);

        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();
        source.sync();

        //sets consumption and production
        TextView consumptionView = (TextView)findViewById(R.id.consumption_display);
        consumptionView.setText(Double.toString(source.getLiveConsumption()));
        TextView productionView= (TextView)findViewById(R.id.production_display);
        productionView.setText(Double.toString(source.getLiveProduction(1)));

        updateTextViewFonts();
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

    public void updateTextViewFonts(){
        TextView txt;
        Typeface boldFont = Typeface.createFromAsset(getAssets(), "Aller_Bd.ttf");
        Typeface regularFont = Typeface.createFromAsset(getAssets(), "Aller_Rg.ttf");
        Typeface lightFont = Typeface.createFromAsset(getAssets(), "Aller_Lt.ttf");

        txt = (TextView)findViewById(R.id.production_display);
        txt.setTypeface(boldFont);
        txt = (TextView)findViewById(R.id.production_label);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.kWh_production);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.consumption_display);
        txt.setTypeface(boldFont);
        txt = (TextView)findViewById(R.id.consumption_label);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.kWH_consumption);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.percent_wind_display);
        txt.setTypeface(boldFont);
        txt = (TextView)findViewById(R.id.percent_wind_label);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.last_updated_label);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.last_updated_display);
        txt.setTypeface(lightFont);
    }
}
