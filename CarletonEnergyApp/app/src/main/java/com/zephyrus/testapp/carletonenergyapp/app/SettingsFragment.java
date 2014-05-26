package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "preferences";
    SharedPreferences sharedPref;
    int notificationToggle;
    Spinner spinner1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);





        Spinner spinner = (Spinner) rootView.findViewById(R.id.font_choice);
        spinner1 = (Spinner) rootView.findViewById(R.id.font_choice);
        List<String> list = new ArrayList<String>();
        list.add("Dark");
        list.add("Light");
        list.add("Sky");
        list.add("Earth");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> (rootView.getContext(), android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Object item = parent.getItemAtPosition(pos);

                System.out.println("it works...   ");

            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        }));




        //notificationToggle = sharedPref.getInt("notifications",0);
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

    public void unitChange(View sview){
        SharedPreferences.Editor editor = sharedPref.edit();
        int temp = 0;
        editor.putInt("Units", temp);

        editor.commit();
    }

}
