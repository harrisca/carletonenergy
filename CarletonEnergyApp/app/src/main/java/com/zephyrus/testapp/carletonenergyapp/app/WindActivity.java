package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class WindActivity extends Activity {

    private boolean windmillOneOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind);

        //creates datasource
        CarletonEnergyDataSource source = new CarletonEnergyDataSource(this);
        source.sync();

        //sets temperature and windspeed
        TextView temperatureView = (TextView)findViewById(R.id.temperature_display);
        temperatureView.setText(Double.toString(source.getCurrentTemperature()));
        TextView windspeedView = (TextView)findViewById(R.id.windspeed_display);
        windspeedView.setText(Double.toString(source.getCurrentWindSpeed()));

        //updates textviews
        updateTextViewFonts();

        //sets default animation to single windmill
        windmillOneOnly = true;

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

    //switches windmill animation on tap
    public void switchAnimation(View view){
        if(windmillOneOnly) {
            ImageView anim = (ImageView) findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_twin_anim);
            windmillOneOnly = false;
        }
        else{
            ImageView anim = (ImageView) findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_anim);
            windmillOneOnly = true;
        }
    }

    //Updates fonts for all textviews
    public void updateTextViewFonts(){
        TextView txt;
        Typeface boldFont = Typeface.createFromAsset(getAssets(), "Aller_Bd.ttf");
        Typeface regularFont = Typeface.createFromAsset(getAssets(), "Aller_Rg.ttf");
        Typeface lightFont = Typeface.createFromAsset(getAssets(), "Aller_Lt.ttf");

        txt = (TextView)findViewById(R.id.temperature_display);
        txt.setTypeface(boldFont);
        txt = (TextView)findViewById(R.id.temperature_type);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.temperature_label);
        txt.setTypeface(regularFont);
        txt = (TextView)findViewById(R.id.windspeed_display);
        txt.setTypeface(boldFont);
        txt = (TextView)findViewById(R.id.windspeed_label);
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
