package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class LiveFragment extends Fragment {

    private boolean windmillOneOnly;
    View fragView;
    public static final String PREFS_NAME = "preferences";
    SharedPreferences sharedPref;
    int units;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //stores inflated view
        fragView = inflater.inflate(R.layout.fragment_wind, container, false);

        //retrieves unit preferences
        sharedPref = fragView.getContext().getSharedPreferences(PREFS_NAME, 0);
        units = sharedPref.getInt("units", 0);
        //Log.i("units", "units in WindFrag: " + units);


        switch (sharedPref.getInt("background", 0)){
            case 0:
                fragView.setBackgroundResource(R.drawable.background_dawn);
                break;
            case 1:
                fragView.setBackgroundResource(R.drawable.background_day);
                break;
            case 2:
                fragView.setBackgroundResource(R.drawable.background_sunset);
                break;
            case 3:
                fragView.setBackgroundResource(R.drawable.background_evening);
                break;
            case 4:
                fragView.setBackgroundResource(R.drawable.background_dusk);
                break;
        }




        //initializes fields
        windmillOneOnly = true;
        updateTextFields();
        animateWindmill();

        return fragView;
    }

    //clean up
    public void onDestroyView(){
        super.onDestroyView();

        fragView = null;
        }

    public void updateTextFields(){
        //Log.i("units", "updatingTextFields");
        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();

        fragView.invalidate();

        TextView windSpeedView = (TextView)fragView.findViewById(R.id.windspeed_display);
        windSpeedView.setText(Double.toString(source.getCurrentWindSpeed()));

        TextView temperatureView = (TextView)fragView.findViewById(R.id.temperature_display);
        TextView temperatureUnit = (TextView)fragView.findViewById(R.id.temperature_type);

        Double temperature = source.getCurrentTemperature();
        if(units == 0){
            temperature = (temperature - 32.0)*5.0/9.0;
            temperatureUnit.setText("C");
        }
        else {
            temperatureUnit.setText("F");
        }
        DecimalFormat temp_format = new DecimalFormat("#.#");
        temperatureView.setText(temp_format.format(temperature) + "°");

        DecimalFormat data_format = new DecimalFormat("#.##");

        TextView consumptionView = (TextView)fragView.findViewById(R.id.consumption_display);
        consumptionView.setText(data_format.format(source.getLiveConsumption()));

        TextView productionView= (TextView)fragView.findViewById(R.id.production_display);
        productionView.setText(data_format.format(source.getLiveProduction(1)));

        TextView lastUpdatedView = (TextView)fragView.findViewById(R.id.last_updated_display);
        //DateFormat df = new SimpleDateFormat("K:mm'pm' MM/dd/yy");
        DateFormat df = new SimpleDateFormat("K:mm a 'on' MMM d");

        df.setTimeZone(TimeZone.getTimeZone("US/Central"));

        if (source.getTimeUpdated() != null) {
            lastUpdatedView.setText(df.format(source.getTimeUpdated()));
        }
        else {
            lastUpdatedView.setText("Syncing...");
        }

        TextView percentWind = (TextView)fragView.findViewById(R.id.percent_wind_display);
        DecimalFormat percent_format = new DecimalFormat("#.#");
        percentWind.setText(percent_format.format(100*source.getLiveProduction(1)/source.getLiveConsumption()) + "%");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void animateWindmill(){
        /*
        ImageView windmill = (ImageView)fragView.findViewById(R.id.windmill_anim);
        Animation anim = new RotateAnimation(30,360, windmill.getPivotX(), windmill.getPivotY());
        anim.setDuration(2000);               // duration in ms
        anim.setRepeatCount(-1);                // -1 = infinite repeated
        anim.setRepeatMode(Animation.INFINITE);
        windmill.startAnimation(anim);
        */
    }

    /*
    //switches windmill animation on tap
    public void switchAnimation(View view){

        if(windmillOneOnly == true) {
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_second_anim);
            ImageView post = (ImageView) fragView.findViewById(R.id.windmill_post);
            post.setImageResource(R.drawable.windmill_stand_inverted);
            windmillOneOnly = false;
        }
        else{
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_anim);
            ImageView post = (ImageView) fragView.findViewById(R.id.windmill_post);
            post.setImageResource(R.drawable.windmill_stand);
            windmillOneOnly = true;
        }
    }
*/
}
