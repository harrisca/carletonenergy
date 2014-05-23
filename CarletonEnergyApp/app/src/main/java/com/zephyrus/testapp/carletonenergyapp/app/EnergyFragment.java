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

    View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_energy, container, false);

        fragView = rootView;
        updateTextFields();

        return rootView;
    }

    public void updateTextFields(){
        CarletonEnergyDataSource source = ((MainActivity) this.getActivity()).getDataSrc();

        fragView.invalidate();

        TextView consumptionView = (TextView)fragView.findViewById(R.id.consumption_display);
        consumptionView.setText(Double.toString(source.getLiveConsumption()));
        TextView productionView= (TextView)fragView.findViewById(R.id.production_display);
        productionView.setText(Double.toString(source.getLiveProduction(1)));
        TextView lastUpdatedView = (TextView)fragView.findViewById(R.id.last_updated_display);
        //lastUpdatedView.setText(source.lastUpdated.toString());

    }
}
