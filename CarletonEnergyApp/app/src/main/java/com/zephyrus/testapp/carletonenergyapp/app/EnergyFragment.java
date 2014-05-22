package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EnergyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_energy, container, false);

        //creates datasource
        CarletonEnergyDataSource source = new CarletonEnergyDataSource();
        source.sync();

        /*
        //sets consumption and production
        TextView consumptionView = (TextView)getView().findViewById(R.id.consumption_display);
        consumptionView.setText(Double.toString(source.getLiveConsumption()));
        TextView productionView= (TextView)getView().findViewById(R.id.production_display);
        productionView.setText(Double.toString(source.getLiveProduction(1)));
        */

        return rootView;
    }
}
