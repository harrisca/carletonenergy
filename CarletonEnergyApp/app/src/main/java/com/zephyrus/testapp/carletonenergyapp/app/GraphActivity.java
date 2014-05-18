package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;


public class GraphActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        if(!isLandscape()){
            Intent intent = new Intent(this, DataActivity.class);
            startActivity(intent);
        }
    }

    public boolean isLandscape() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return true;
        else
            return false;
    }
}
