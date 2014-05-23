package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
//import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class WindFragment extends Fragment {

    private boolean windmillOneOnly;
    View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wind, container, false);

        fragView = rootView;
        //sets default animation to single windmill
        windmillOneOnly = true;
        updateTextFields();

        return rootView;
    }

    public void updateTextFields(){
        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();

        TextView windSpeedView = (TextView)fragView.findViewById(R.id.windspeed_display);
        windSpeedView.setText(Double.toString(source.getCurrentWindSpeed()));
        TextView temperatureView = (TextView)fragView.findViewById(R.id.temperature_display);
        temperatureView.setText(Double.toString(source.getCurrentTemperature()));
        TextView lastUpdatedView = (TextView)fragView.findViewById(R.id.last_updated_display);
//        lastUpdatedView.setText(source.lastUpdated.toString());

    }

    //switches windmill animation on tap
    public void switchAnimation(View view){
        if(windmillOneOnly) {
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_twin_anim);
            windmillOneOnly = false;
        }
        else{
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_anim);
            windmillOneOnly = true;
        }
    }

}
