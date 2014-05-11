package com.zephyrus.testapp.datatestapp.app;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLConnection;

import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class CarletonEnergyDataSource {

    public double getLiveProduction(int windmill) {
        return 0.0;
    }

    public double getLiveConsumption() {
        return 0.0;
    }

    public double getCurrentTemperature() {
        return 0.0;
    }

    public double getCurrentWindSpeed() {
        return 0.0;
    }

    public void getGraphData(int thing_to_graph, Date start_time, Date end_time, int increment) {
        // figure this out later
    }

    public int checkForAlert() {
        return 0;
    }

    /*
    Get data from the internet (the weather website and lucid), update data files on phone
     */
    public void sync() {
        syncEnergyData();
        syncWeatherData();

    }



    private int syncEnergyData() {
        String json_string = "{\"startTimestamp\": \"2014/05/10 00:00:00\", \"results\": [{\"startTimestamp\": \"2014/05/10 00:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 972.3778}}, {\"startTimestamp\": \"2014/05/10 01:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1241.7904}}, {\"startTimestamp\": \"2014/05/10 02:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1234.8372}}, {\"startTimestamp\": \"2014/05/10 03:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1120.8208}}, {\"startTimestamp\": \"2014/05/10 04:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 936.2695}}, {\"startTimestamp\": \"2014/05/10 05:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1203.0469}}], \"endTimestamp\": \"2014/05/10 05:00:00\", \"page\": 1}";
        JSONObject all_electricity_page = null;

        try {
            all_electricity_page = (JSONObject) new JSONTokener(json_string).nextValue();
            JSONArray results = (JSONArray)all_electricity_page.get("results");
            //Log.i("results", results.toString());
            for (int i = 0; i < results.length(); i++) {
                Double value = ((JSONObject)((JSONObject)results.get(i)).get("carleton_campus_en_use")).getDouble("value");
                //Log.i("value", String.format("value = %f", value));

            }

            // successfully retrieved data
            return 0;


        } catch (JSONException e) {

            //didn't find something it was supposed to
            e.printStackTrace();
            Log.i("JSONException", e.toString());


        }

        return -1;

    }

    private int syncWeatherData() {

        String xml_string = "\n" +
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
                "<?xml-stylesheet href=\"latest_ob.xsl\" type=\"text/xsl\"?>\n" +
                "<current_observation version=\"1.0\"\n" +
                "\t xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "\t xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\t xsi:noNamespaceSchemaLocation=\"http://www.weather.gov/view/current_observation.xsd\">\n" +
                "\t<credit>NOAA's National Weather Service</credit>\n" +
                "\t<credit_URL>http://weather.gov/</credit_URL>\n" +
                "\t<image>\n" +
                "\t\t<url>http://weather.gov/images/xml_logo.gif</url>\n" +
                "\t\t<title>NOAA's National Weather Service</title>\n" +
                "\t\t<link>http://weather.gov</link>\n" +
                "\t</image>\n" +
                "\t<suggested_pickup>15 minutes after the hour</suggested_pickup>\n" +
                "\t<suggested_pickup_period>60</suggested_pickup_period>\n" +
                "\t<location>Stanton Airfield, MN</location>\n" +
                "\t<station_id>KSYN</station_id>\n" +
                "\t<latitude>44.467</latitude>\n" +
                "\t<longitude>-93.017</longitude>\n" +
                "\t<observation_time>Last Updated on May 10 2014, 7:52 pm CDT</observation_time>\n" +
                "        <observation_time_rfc822>Sat, 10 May 2014 19:52:00 -0500</observation_time_rfc822>\n" +
                "\t<weather>Light Rain</weather>\n" +
                "\t<temperature_string>60.0 F (15.3 C)</temperature_string>\n" +
                "\t<temp_f>60.0</temp_f>\n" +
                "\t<temp_c>15.3</temp_c>\n" +
                "\t<relative_humidity>74</relative_humidity>\n" +
                "\t<wind_string>East at 11.5 MPH (10 KT)</wind_string>\n" +
                "\t<wind_dir>East</wind_dir>\n" +
                "\t<wind_degrees>110</wind_degrees>\n" +
                "\t<wind_mph>11.5</wind_mph>\n" +
                "\t<wind_kt>10</wind_kt>\n" +
                "\t<pressure_in>29.78</pressure_in>\n" +
                "\t<dewpoint_string>51.1 F (10.6 C)</dewpoint_string>\n" +
                "\t<dewpoint_f>51.1</dewpoint_f>\n" +
                "\t<dewpoint_c>10.6</dewpoint_c>\n" +
                "\t<windchill_string>58 F (14 C)</windchill_string>\n" +
                "      \t<windchill_f>58</windchill_f>\n" +
                "      \t<windchill_c>14</windchill_c>\n" +
                "\t<visibility_mi>10.00</visibility_mi>\n" +
                " \t<icon_url_base>http://forecast.weather.gov/images/wtf/small/</icon_url_base>\n" +
                "\t<two_day_history_url>http://www.weather.gov/data/obhistory/KSYN.html</two_day_history_url>\n" +
                "\t<icon_url_name>ra.png</icon_url_name>\n" +
                "\t<ob_url>http://www.weather.gov/data/METAR/KSYN.1.txt</ob_url>\n" +
                "\t<disclaimer_url>http://weather.gov/disclaimer.html</disclaimer_url>\n" +
                "\t<copyright_url>http://weather.gov/disclaimer.html</copyright_url>\n" +
                "\t<privacy_policy_url>http://weather.gov/notice.html</privacy_policy_url>\n" +
                "</current_observation>\n";
        try {

            readFeed();

            // successfully retrieved data
            return 0;


        } catch (Exception e) {

            //didn't find something it was supposed to
            e.printStackTrace();
            Log.i("xml_error", e.toString());


        }

        return -1;

    }

    private void readFeed() throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        String xml_string = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
                "<?xml-stylesheet href=\"latest_ob.xsl\" type=\"text/xsl\"?>\n" +
                "<current_observation version=\"1.0\"\n" +
                "\t xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "\t xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\t xsi:noNamespaceSchemaLocation=\"http://www.weather.gov/view/current_observation.xsd\">\n" +
                "\t<credit>NOAA's National Weather Service</credit>\n" +
                "\t<credit_URL>http://weather.gov/</credit_URL>\n" +
                "\t<image>\n" +
                "\t\t<url>http://weather.gov/images/xml_logo.gif</url>\n" +
                "\t\t<title>NOAA's National Weather Service</title>\n" +
                "\t\t<link>http://weather.gov</link>\n" +
                "\t</image>\n" +
                "\t<suggested_pickup>15 minutes after the hour</suggested_pickup>\n" +
                "\t<suggested_pickup_period>60</suggested_pickup_period>\n" +
                "\t<location>Stanton Airfield, MN</location>\n" +
                "\t<station_id>KSYN</station_id>\n" +
                "\t<latitude>44.467</latitude>\n" +
                "\t<longitude>-93.017</longitude>\n" +
                "\t<observation_time>Last Updated on May 10 2014, 7:52 pm CDT</observation_time>\n" +
                "        <observation_time_rfc822>Sat, 10 May 2014 19:52:00 -0500</observation_time_rfc822>\n" +
                "\t<weather>Light Rain</weather>\n" +
                "\t<temperature_string>60.0 F (15.3 C)</temperature_string>\n" +
                "\t<temp_f>60.0</temp_f>\n" +
                "\t<temp_c>15.3</temp_c>\n" +
                "\t<relative_humidity>74</relative_humidity>\n" +
                "\t<wind_string>East at 11.5 MPH (10 KT)</wind_string>\n" +
                "\t<wind_dir>East</wind_dir>\n" +
                "\t<wind_degrees>110</wind_degrees>\n" +
                "\t<wind_mph>11.5</wind_mph>\n" +
                "\t<wind_kt>10</wind_kt>\n" +
                "\t<pressure_in>29.78</pressure_in>\n" +
                "\t<dewpoint_string>51.1 F (10.6 C)</dewpoint_string>\n" +
                "\t<dewpoint_f>51.1</dewpoint_f>\n" +
                "\t<dewpoint_c>10.6</dewpoint_c>\n" +
                "\t<windchill_string>58 F (14 C)</windchill_string>\n" +
                "      \t<windchill_f>58</windchill_f>\n" +
                "      \t<windchill_c>14</windchill_c>\n" +
                "\t<visibility_mi>10.00</visibility_mi>\n" +
                " \t<icon_url_base>http://forecast.weather.gov/images/wtf/small/</icon_url_base>\n" +
                "\t<two_day_history_url>http://www.weather.gov/data/obhistory/KSYN.html</two_day_history_url>\n" +
                "\t<icon_url_name>ra.png</icon_url_name>\n" +
                "\t<ob_url>http://www.weather.gov/data/METAR/KSYN.1.txt</ob_url>\n" +
                "\t<disclaimer_url>http://weather.gov/disclaimer.html</disclaimer_url>\n" +
                "\t<copyright_url>http://weather.gov/disclaimer.html</copyright_url>\n" +
                "\t<privacy_policy_url>http://weather.gov/notice.html</privacy_policy_url>\n" +
                "</current_observation>\n";

        xpp.setInput(new StringReader(xml_string));
        int eventType = xpp.getEventType();
        boolean read_next = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                //System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                String tag_name = xpp.getName();
                if (tag_name.equals("temp_f")) {
                    Log.i("xml", "temp_f exists");
                    read_next = true;
                }
                //System.out.println("Start tag "+xpp.getName());
            } else if(eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT && read_next) {
                //System.out.println(xpp.getText());
                read_next = false;

            }
            eventType = xpp.next();
        }
        //System.out.println("End document");

    }

}