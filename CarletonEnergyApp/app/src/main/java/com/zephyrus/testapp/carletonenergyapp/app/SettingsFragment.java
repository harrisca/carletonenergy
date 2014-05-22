package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
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
import android.widget.ToggleButton;


public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "prefrences";
    SharedPreferences sharedPref;
    int notificationToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        //notificationToggle = sharedPref.getInt("notifications",0);
        return rootView;
    }

    public void notificationTest(View view) {

        long when = System.currentTimeMillis();
        NotificationManager nm=(NotificationManager)this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this.getActivity(), WindFragment.class);
        PendingIntent pending=PendingIntent.getActivity(this.getActivity(), 0, intent, 0);
        Notification notification = new Notification.Builder(this.getActivity()).setContentTitle("Pwr").setContentText("Omg this worked? that cray")
                .setSmallIcon(R.drawable.launcher)
                .setContentIntent(pending).setWhen(when).setAutoCancel(true).build();

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

}
