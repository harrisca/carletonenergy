package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//Screen provides buttons for the user to select the data they would like to view on a graph.
//Graph is viewed by rotating the screen to Landscape from Portrait which switches to GraphActivity.

public class HistoricFragment extends Fragment {
    private static boolean productionChecked = true;
    private static boolean consumptionChecked = true;
    private static String buttonClickedStr = "day";
    private static int buttonClicked = R.id.radio_day;
    private View fragView;
    private RadioGroup rg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_historic, container, false);

        //If the screen is rotated, go to GraphActivity and send necessary details for the graph
        if (!isPortrait()) {
            Intent i = new Intent(this.getActivity(), GraphActivity.class).putExtra("buttonClickedStr", buttonClickedStr);
            i.putExtra("productionChecked", productionChecked);
            i.putExtra("consumptionChecked", consumptionChecked);
            startActivity(i);
            getActivity().finish();
        }

        CheckBox proCheckbox = (CheckBox) fragView.findViewById(R.id.checkbox_windmill);
        CheckBox conCheckbox = (CheckBox) fragView.findViewById(R.id.checkbox_consumption);

        //Default: display both production and consumption data
        proCheckbox.setChecked(productionChecked);
        conCheckbox.setChecked(consumptionChecked);

        //Set the appropriate radioButton to be checked
        RadioButton bc = (RadioButton) fragView.findViewById(buttonClicked);
        bc.setChecked(true);

        proCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton proCheckBox, boolean checked){
                if(proCheckBox.isChecked()){
                    productionChecked = true;
                }
                else {
                    productionChecked = false;
                }
            }
        });

        conCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton conCheckBox, boolean checked){
                if(conCheckBox.isChecked()){
                    consumptionChecked = true;
                }
                else {
                    consumptionChecked = false;
                }
            }
        });

        //RadioButton management; marks which has been clicked and
        //changes buttonClickedStr to be the last button clicked
        rg = (RadioGroup) fragView.findViewById(R.id.radioOption_time);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {

                buttonClicked = checkedId;

                switch (checkedId) {
                    case R.id.radio_day:
                        buttonClickedStr = "day";
                        break;
                    case R.id.radio_week:
                        buttonClickedStr = "week";
                        break;
                    case R.id.radio_month:
                        buttonClickedStr = "month";
                        break;
                    case R.id.radio_year:
                        buttonClickedStr = "year";
                        break;
                }
            }
        });
        return fragView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        fragView = null;
    }
    public boolean isPortrait() {
        Display getOrient = this.getActivity().getWindowManager().getDefaultDisplay();
        if (getOrient.getRotation()%2==0)
            return true;
        else
            return false;
    }
}

