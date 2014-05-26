package com.zephyrus.testapp.carletonenergyapp.app;

import java.util.Date;

/**
 * Created by Licha on 5/25/2014.
 */
public class LoadManager {

    public interface DateListener{
        public void onStateChange(boolean state);
    }

    private DateListener mListener = null;
    private Date lastUpdatedDate;
    private boolean wasUpdated = false;

    public void registerListener(DateListener listener){
        mListener = listener;
    }

    public void checkDataSync(){

        CarletonEnergyDataSource source = CarletonEnergyDataSource.getSingleton();

        if(source.lastUpdated != null && source.lastUpdated != lastUpdatedDate)
            wasUpdated = true;

        if(mListener != null)
            mListener.onStateChange(wasUpdated);

        if(wasUpdated){
            wasUpdated = false;
            lastUpdatedDate = source.lastUpdated;
        }

    }

}
