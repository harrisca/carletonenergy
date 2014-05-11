package com.zephyrus.testapp.carletonenergyapp.app;

import android.util.JsonReader;
import org.json.JSONObject;

//import java.io.BufferedInputStream;
//import java.io.InputStreamReader;
//import java.net.URLConnection;

import java.io.*;
import java.net.*;
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
        try {
            return getHulingsTemperature("F");
        } catch (IOException e) {
            return -999.9;
        }
    }

    double getCurrentWindSpeed() {
        return 0.0;
    }

    void getGraphData(int thing_to_graph, Date start_time, Date end_time, int increment) {
        // figure this out later
    }

    double getHulingsTemperature(String unit) throws IOException {
        URL carleton = new URL("http://weather.carleton.edu");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(carleton.openStream()));
        String inputLine;
        int lineNum = 0;
        String tempString = new String();
        while ((inputLine = in.readLine()) != null){
            if (unit == "F") {
                if (lineNum == 126) {
                    tempString = inputLine;
                }
            }
            if (unit == "C"){
                if (lineNum == 127) {
                    tempString = inputLine;
                }
            }
            lineNum++;
        }
        double temp = parseHTMLForTemp(tempString);
        in.close();
        return temp;
    }

    double parseHTMLForTemp(String line){
        String output = "";
        int end = 0;
        char charInt;
        for (int i=0; i<line.length(); i++){
            charInt=line.charAt(i);
            if(charInt>=45 && charInt<=57){
                output = output + charInt;
                end+=1;
            }
            else{
                if (end > 1){
                    break;
                }
            }
        }

        return Double.parseDouble(output.substring(1));
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