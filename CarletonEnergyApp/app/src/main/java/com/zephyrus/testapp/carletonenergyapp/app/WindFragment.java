package com.zephyrus.testapp.carletonenergyapp.app;

//import android.app.Fragment;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
        import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


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
        DateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("US/Central"));
        if (source.getTimeUpdated() != null) {
            lastUpdatedView.setText(df.format(source.getTimeUpdated()));
        }
        else {
            lastUpdatedView.setText("Loading now ...");
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



    //switches windmill animation on tap
    public void switchAnimation(View view){

        if(windmillOneOnly == true) {
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_second_anim);
            ImageView post = (ImageView) fragView.findViewById(R.id.windmill_post);
            post.setImageResource(R.drawable.windmill2_post);
            windmillOneOnly = false;
        }
        else{
            ImageView anim = (ImageView) fragView.findViewById(R.id.windmillAnim);
            anim.setImageResource(R.drawable.windmill_anim);
            ImageView post = (ImageView) fragView.findViewById(R.id.windmill_post);
            post.setImageResource(R.drawable.windmill1_post);
            windmillOneOnly = true;
        }
    }

}
