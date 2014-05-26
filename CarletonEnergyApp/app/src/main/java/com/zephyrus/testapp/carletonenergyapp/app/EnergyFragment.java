package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class EnergyFragment extends Fragment {

    View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_energy, container, false);

        fragView = rootView;
        updateTextFields();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    public void updateTextFields(){
        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();

        fragView.invalidate();

        DecimalFormat data_format = new DecimalFormat("#.##");
        TextView consumptionView = (TextView)fragView.findViewById(R.id.consumption_display);
        consumptionView.setText(data_format.format(source.getLiveConsumption()));
        TextView productionView= (TextView)fragView.findViewById(R.id.production_display);
        productionView.setText(data_format.format(source.getLiveProduction(1)));
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
}
