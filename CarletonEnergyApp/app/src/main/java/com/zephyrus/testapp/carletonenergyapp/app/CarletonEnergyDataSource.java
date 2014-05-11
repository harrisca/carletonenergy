package com.zephyrus.testapp.carletonenergyapp.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 */
public class CarletonEnergyDataSource {

    // https://rest.buildingos.com/reports/timeseries/?start=2014/05/07+20:00:00&resolution=hour&end=2014/5/07+20:00:00&name=carleton_campus_en_use
    // https://rest.buildingos.com/reports/timeseries/?start=2014/03/01+00:00:00&resolution=hour&end=2014/4/30+00:00:00&name=carleton_campus_en_use
    //

    double getLiveProduction(int windmill) {
        return 0.0;
    }

    double getLiveConsumption() {
        return 0.0;
    }

    double getCurrentTemperature() {
        return getStantonTemperatureF();
    }

    public int getStantonTemperatureF() {

        // URL oracle = new URL("w1.weather.gov/xml/current_obs/KSYN.xml");
        URL noaa = null;
        try {
            noaa = new URL("http://w1.weather.gov/obhistory/KSYN.html");
        } catch (MalformedURLException e) {
            return -999;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(noaa.openStream()));
        } catch (IOException e) {
            return -999;
        }

        String inputLine;
        int lineNum = 0;
        String temp = new String();
        try {
            while ((inputLine = in.readLine()) != null){
                if (lineNum == 23){
                    temp = inputLine;
                }
                lineNum++;
            }
        } catch (IOException e) {
            return -999;
        }
        try {
            in.close();
        } catch (IOException e) {
            return -999;
        }
        char charInt;
        String degreesF = "";
        int end = 0;
        String numbers = temp.substring(temp.length() - 25);
        for (int i=0; i<numbers.length(); i++){
            charInt=numbers.charAt(i);
            if(charInt>=48 && charInt<=57){
                degreesF = degreesF + charInt;
                end = 1;
            }
            else{
                if (end == 1){
                    break;
                }
            }
        }
        return Integer.parseInt(degreesF);
    }

    double getCurrentWindSpeed() {
        return 0.0;
    }

    void getGraphData(int thing_to_graph, Date start_time, Date end_time, int increment) {
        // figure this out later
    }

    int checkForAlert() {
        return 0;
    }

    void sync() {

        /*URL url = new URL("https://graph.facebook.com/search?q=java&type=post");
        try (InputStream is = url.openStream();
            JsonParser parser = Json.createParser(is)) {
            while (parser.hasNext()) {
                EventLog.Event e = parser.next();
                if (e == EventLog.Event.KEY_NAME) {

                }

            }

        }*/

        URL url = null;
        try {
            url = new URL("https://rest.buildingos.com/reports/timeseries/?start=2014/05/07+20:00:00&resolution=hour&end=2014/5/07+20:00:00&name=carleton_campus_en_use");
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader reader = new InputStreamReader(in);
            int max_size = 200000;
            char[] buffer = new char[max_size];
            reader.read(buffer, max_size, 0);
            String json_string = new String(buffer);




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}