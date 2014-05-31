package com.zephyrus.testapp.carletonenergyapp.app.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StubProvider extends ContentProvider {

    private double currentTemperature = 0.0;
    private double currentWindspeed = 0.0;
    private double liveProduction1 = 0.0;
    private double liveProduction2 = 0.0;
    private double liveConsumption = 0.0;
    private String lastUpdated = null;


    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /*
     * Return an empty String for MIME type
     */

    public String getType() {
        return "";
    }

    /*
     * query() always returns no results
     *
     */
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    /*
     * insert() always returns null (no URI)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        currentTemperature = values.getAsDouble("currentTemperature");
        currentWindspeed   = values.getAsDouble("currentWindspeed");
        liveProduction1    = values.getAsDouble("liveProduction1");
        liveProduction2    = values.getAsDouble("liveProduction2");
        lastUpdated        = values.getAsString("lastUpdated");

        return null;
    }

    /*
     * delete() always returns "no rows affected" (0)
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /*
     * update() always returns "no rows affected" (0)
     */
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {
        return 0;
    }

    public List getStatistics(){
        //updates
        List returnList = Arrays.asList(currentTemperature, currentWindspeed, liveProduction2, liveProduction1, liveConsumption, lastUpdated);


        return returnList;
    }
}
