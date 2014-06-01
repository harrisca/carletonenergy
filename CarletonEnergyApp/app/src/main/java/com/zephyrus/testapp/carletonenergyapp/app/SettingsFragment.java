package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "preferences";
    SharedPreferences sharedPref;
    int units;
    int notificationToggle;
    Spinner spinner1;
    View fragView;
    View fragWind;
    View fragInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_settings, container, false);
        fragWind = inflater.inflate(R.layout.fragment_wind, container, false);
        fragInfo = inflater.inflate(R.layout.fragment_info, container, false);


        sharedPref = fragView.getContext().getSharedPreferences(PREFS_NAME, 0);
        units = sharedPref.getInt("units", 0);

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
                Log.e("no", "plzno");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("background", 0 );
                editor.commit();
                break;
        }

        ToggleButton unitsToggle = (ToggleButton) fragView.findViewById(R.id.UnitsToggle);
        if(units==1){
            unitsToggle.setChecked(true);
        }
        unitsToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                int temp;
                boolean on = ((ToggleButton)view).isChecked();
                if(on){temp =1;}
                else{temp = 0;}
                editor.putInt("units", temp);
                editor.commit();
                units = sharedPref.getInt("units", 0);
                Log.i("units", "units in settings: " + units);
            }
        });
        ToggleButton notificationButton = (ToggleButton) fragView.findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        //Making a spinner
        Spinner spinner = (Spinner) fragView.findViewById(R.id.font_choice);
        spinner1 = (Spinner) fragView.findViewById(R.id.font_choice);
        List<String> list = new ArrayList<String>();
        list.add("Color Scheme: Dawn");
        list.add("Color Scheme: Day");
        list.add("Color Scheme: Sunset");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (fragView.getContext(), android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                Object item = parent.getItemAtPosition(pos);

                switch (pos){
                    case 0:
                        fragView.setBackgroundResource(R.drawable.background_dawn);
                        fragWind.setBackgroundResource(R.drawable.background_dawn);
                        fragInfo.setBackgroundResource(R.drawable.background_dawn);


                        break;
                    case 1:
                        fragView.setBackgroundResource(R.drawable.background_day);
                        fragWind.setBackgroundResource(R.drawable.background_day);
                        fragInfo.setBackgroundResource(R.drawable.background_day);

                        break;
                    case 2:
                        fragView.setBackgroundResource(R.drawable.background_sunset);
                        fragWind.setBackgroundResource(R.drawable.background_sunset);
                        fragInfo.setBackgroundResource(R.drawable.background_sunset);

                        break;


                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("background", pos );
                editor.commit();
                units = sharedPref.getInt("units", 0);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        }));
        spinner1.setSelection(sharedPref.getInt("background", 0));



        //notificationToggle = sharedPref.getInt("notifications",0);
        return fragView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        fragView = null;
    }
    public void notificationTest(View view) {

        long when = System.currentTimeMillis();
        NotificationManager nm=(NotificationManager)this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this.getActivity(), WindFragment.class);
        PendingIntent pending=PendingIntent.getActivity(this.getActivity(), 0, intent, 0);
        Notification notification = new Notification.Builder(this.getActivity()).setContentTitle("Pwr").setContentText("Omg this worked? that cray")
                .setSmallIcon(R.drawable.launcher)
                .setContentIntent(pending).setWhen(when).setAutoCancel(true).getNotification();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        nm.notify(0, notification);



    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            //enable
        } else {
            // Disable
        }
    }

    public void unitChange(View view){

        SharedPreferences.Editor editor = sharedPref.edit();
        int temp= 0;
        boolean on = ((ToggleButton)view).isChecked();
        if(on){temp =1;}
        else{temp = 0;}
        editor.putInt("units", temp);
        editor.commit();
    }

}
