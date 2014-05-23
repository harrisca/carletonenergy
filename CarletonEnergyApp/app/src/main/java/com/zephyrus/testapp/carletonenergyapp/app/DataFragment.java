package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_data, container, false);

        /*
        if(!isPortrait()){
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            startActivity(intent);
        }
        */
        return rootView;
    }

    public boolean isPortrait() {
        Display getOrient = this.getActivity().getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return false;
        else
            return true;
    }

}
