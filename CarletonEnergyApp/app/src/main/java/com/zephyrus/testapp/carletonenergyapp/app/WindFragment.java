package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
//import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class WindFragment extends Fragment {

    private boolean windmillOneOnly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wind, container, false);

        //gets datasource
        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();
        source.sync();

        /*//sets temperature and windspeed
        TextView temperatureView = (TextView)getView().findViewById(R.id.temperature_display);
        temperatureView.setText(Double.toString(source.getCurrentTemperature()));
        TextView windspeedView = (TextView)getView().findViewById(R.id.windspeed_display);
        windspeedView.setText(Double.toString(source.getCurrentWindSpeed()));*/

        //sets default animation to single windmill
        windmillOneOnly = true;

        return rootView;
    }


    //switches windmill animation on tap
    public void switchAnimation(View view){
        if(windmillOneOnly) {
            ImageView anim = (ImageView) getView().findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_twin_anim);
            windmillOneOnly = false;
        }
        else{
            ImageView anim = (ImageView) getView().findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_anim);
            windmillOneOnly = true;
        }
    }

}
