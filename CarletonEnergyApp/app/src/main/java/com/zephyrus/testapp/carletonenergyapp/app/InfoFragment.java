package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class InfoFragment extends Fragment {

    View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_info, container, false);

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


}
