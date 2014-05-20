package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.SharedPreferences;
import android.widget.ToggleButton;


public class SettingsActivity extends Activity {

    public static final String PREFS_NAME = "prefrences";
    SharedPreferences sharedPref;
    int notificationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationToggle = sharedPref.getInt("notifications",0);
    }


    public void switchWind(View view){
        Intent intent = new Intent(this, WindActivity.class);
        startActivity(intent);
    }

    public void switchData(View view){
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void switchSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void switchEnergy(View view){
        Intent intent = new Intent(this, EnergyActivity.class);
        startActivity(intent);
    }

    public void notificationTest(View view) {

        long when = System.currentTimeMillis();
        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(this, WindActivity.class);
        PendingIntent pending=PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this).setContentTitle("Pwr").setContentText("Omg this worked? that cray")
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
