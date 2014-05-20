package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class CarletonEnergyDataSource {
    String speedUnits = "US";
    String degreeUnits;
    double currentTemperature = 0.0;
    double currentWindspeed = 0.0;
    double liveProduction1 = 0.0;
    double liveProduction2 = 0.0;
    double liveConsumption = 0.0;
    Date lastUpdated = null;
    ArrayList oldData = null;
    Context context;
    public static final String PREFS_NAME = "prefrences";
    SharedPreferences sharedPref;
    int notificationToggle;

    public CarletonEnergyDataSource(Context context) {
        this.context = context;
        degreeUnits = sharedPref.getString("degreeUnits", "C");
        notificationToggle = sharedPref.getInt("notifications", 0);
    }

    /*
     * Returns a double representing the most up-to-date live energy production for the
     * given windmill(s) in kW
     */
    public double getLiveProduction(int windmill) {
        if (windmill == 1){
            return liveProduction1;
        }
        if (windmill == 2) {
            return liveProduction2;
        }
        if (windmill == 0)  {
            return liveProduction1 + liveProduction2;
        }
        return -1.0;
    }

    /*
     * Returns a double representing the most up-to-date live energy consumption for the
     * whole campus in kW
     */
    public double getLiveConsumption() {

        return 0.0;
    }
    /*
    * Returns a double representing the current temperature according to the weather.carleton.edu units depend on string input - F or C
    */
    double getHulingsTemperature() {
        URL carleton = null;
        try {
            carleton = new URL("http://weather.carleton.edu");
        } catch (MalformedURLException e) {
            System.out.println("malformed URL Exception getHulingsTemperature");
            return -999.9;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(carleton.openStream()));
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsTemperature");
            return -999.9;
        }
        String inputLine;
        int lineNum = 0;
        String tempString = new String();
        try {
            while ((inputLine = in.readLine()) != null){
                if (lineNum == 126) {
                    tempString = inputLine;
                }


                lineNum++;
            }
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsTemperature");
            return -999.9;
        }
        double temp = parseHTMLForTemp(tempString);
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsTemperature");
            return -999.9;
        }
        return temp;
    }
    /*
    * Returns a int representing the current wind speed according to the weather.carleton.edu in MPH
    */
    double getHulingsWindSpeed() {
        URL carleton = null;
        try {
            carleton = new URL("http://weather.carleton.edu");
        } catch (MalformedURLException e) {
            System.out.println("malformed URL Exception getHulingsWindSpeed");
            return -1.0;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(carleton.openStream()));
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsWindSpeed");
            return -1.0;
        }
        String inputLine;
        int lineNum = 0;
        String speedString = new String();
        try {
            while ((inputLine = in.readLine()) != null){
                if (lineNum == 152) {
                    speedString = inputLine;
                }
                lineNum++;
            }
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsWindSpeed");
            return -1.0;
        }
        int speed = parseHTMLForSpeed(speedString);
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("IO Exception getHulingsWindSpeed");
            return -1.0;
        }
        return speed*1.0;
    }

    /*
    * Returns a double from a line of html code received from weather.carleton.edu
    */
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
    /*
    * Returns a int from a line of html code received from weather.carleton.edu
    */
    int parseHTMLForSpeed(String line){
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

        return Integer.parseInt(output.substring(1));
    }

    /*
     * Returns a double representing the current temp according to weather.carleton.edu in degrees C
     * Error when returns -999.9
     */
    double getCurrentTemperature() {
        return this.currentTemperature;
    }

    /*
     * Returns a double representing the current wind speed according to the weather.carleton.edu in MPH
     * Error when returns -1.0
     */
    public double getCurrentWindSpeed() {
        return this.currentWindspeed;
    }

    /*
     * Returns a double array with a value for each time increment from start_time to end_time,
     * where the value represents the energy production (or consumption) at that time in kWh.
     *
     * The dependent_variable could be production for either or both windmills or consumption
     * for the school.
     */
    public ArrayList<Double> getGraphData(int dependent_variable, Date start_time, Date end_time, String increment) {
        ArrayList<Double> valueList = new ArrayList<Double>();

        if (dependent_variable == 1 && increment.equals("day")) {
            //Log.i("getGraphData", "We're here!");
            try {
                FileInputStream in = context.openFileInput("daily_consumption_data");
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;

                //Log.i("getGraphData", "start_time, end_time" + start_time + " " + end_time);
                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        //Log.i("getGraphData", "line = " + line);
                        String time_string = line.substring(0, line.indexOf(';'));
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date time = df.parse(time_string);
                        Double value = Double.parseDouble(line.substring(line.indexOf(';') + 1, line.length()));
                        //Log.i("getGraphData", time.getMonth() + " < " + end_time.getYear() + ": " + time.before(end_time));
                        //Log.i("getGraphData", time.getYear() + " < " + start_time.getYear() + ": " + (time.before(start_time)));
                        if (!time.after(end_time) && !time.before(start_time)) {
                            //Log.i("getGraphData", "getting here!");
                            valueList.add(value);
                        }
                    }
                    catch (Exception e){
                        Log.i("getGraphData", "exception: line = " + line);
                        e.printStackTrace();
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }



        return valueList;
    }

    /*
     * Returns an int - 0 for no current alert, other values representing alerts
     */
    public int checkForAlert() {
        return 0;
    }

    /*
     * Get data from the internet (the weather website and lucid), update data files on phone
     */
    public void sync()  {

        new Thread(new Runnable() {
            public void run() {
                //System.out.println("In run function");
                currentTemperature = getHulingsTemperature();
                //System.out.println("In run function");
                currentWindspeed = getHulingsWindSpeed();
                //System.out.println("Current Temperature: " + currentTemperature);

                try {
                    syncEnergyData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*try {
                    syncWeatherData();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                lastUpdated = new Date();
            }
        }).start();
        //System.out.println(this.currentTemperature);
        //System.out.println(this.currentWindspeed);
    }

    /*
     * Retrieve energy data from lucid and update data stored in a file
     *
     */
    private int syncEnergyData() throws IOException {

        Calendar today = Calendar.getInstance();
        Calendar year_ago = Calendar.getInstance();
        year_ago.add(Calendar.YEAR, -1);
        Calendar month_ago = Calendar.getInstance();
        month_ago.add(Calendar.MONTH, -1);
        Calendar week_ago = Calendar.getInstance();
        week_ago.add(Calendar.DATE, -7);
        String daily_consumption = readEnergyJSON(year_ago.getTime(), today.getTime(), "day", "carleton_campus_en_use");
        String daily_production = readEnergyJSON(year_ago.getTime(), today.getTime(), "day", "carleton_turbine1_produced_power");
        String hourly_consumption = readEnergyJSON(month_ago.getTime(), today.getTime(), "hour", "carleton_campus_en_use");
        String hourly_production = readEnergyJSON(month_ago.getTime(), today.getTime(), "hour", "carleton_turbine1_produced_power");
        String quarter_hourly_consumption = readEnergyJSON(week_ago.getTime(), today.getTime(), "quarterhour", "carleton_campus_en_use");
        String quarter_hourly_production = readEnergyJSON(week_ago.getTime(), today.getTime(), "quarterhour", "carleton_turbine1_produced_power");

        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("daily_consumption_data", Context.MODE_PRIVATE));
            out.writeUTF(daily_consumption);
            out.close();
        } catch (IOException e) {
            Log.i("Data Input Sample", "I/O Error");
        }







        // NEXT STEP: CHECK THAT SAVING TO A FILE IS WORKING, THEN READ FROM FILE IN GET_GRAPH_DATA()


        // ALMOST THERE!!!!!





        return 0;

    }

    private String readEnergyJSON(Date start, Date end, String resolution, String point) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd+HH:mm:ss");
        //System.out.println(df.format(start) + start + end);
        String url_string = "https://rest.buildingos.com/reports/timeseries/?start=" + df.format(start) + "&resolution=" + resolution + "&end=" + df.format(end) + "&name=" + point;
        //System.out.println(url_string);
        URL consumption_url = new URL(url_string);
        InputStream in = consumption_url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result, line = reader.readLine();
        result = line;
        while((line=reader.readLine())!=null){
            result+=line;
        }
        //
        // System.out.println(result);
        String json_string = result;

        //String response = String.format("%d", urlConnection.getResponseCode());
        //urlConnection.getResponseMessage();


        //String json_string = "{\"startTimestamp\": \"2014/05/10 00:00:00\", \"results\": [{\"startTimestamp\": \"2014/05/10 00:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 972.3778}}, {\"startTimestamp\": \"2014/05/10 01:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1241.7904}}, {\"startTimestamp\": \"2014/05/10 02:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1234.8372}}, {\"startTimestamp\": \"2014/05/10 03:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1120.8208}}, {\"startTimestamp\": \"2014/05/10 04:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 936.2695}}, {\"startTimestamp\": \"2014/05/10 05:00:00\", \"carleton_campus_en_use\": {\"hoursElapsed\": 1.0, \"weight\": 1.0, \"value\": 1203.0469}}], \"endTimestamp\": \"2014/05/10 05:00:00\", \"page\": 1}";
        JSONObject all_electricity_page = null;

        String return_string = "";

        try {
            all_electricity_page = (JSONObject) new JSONTokener(json_string).nextValue();
            JSONArray results = (JSONArray)all_electricity_page.get("results");
            //Log.i("results", results.toString());
            for (int i = 0; i < results.length(); i++) {
                try {
                    Double value = ((JSONObject) ((JSONObject) results.get(i)).get(point)).getDouble("value");
                    String timestamp_string = ((JSONObject) results.get(i)).getString("startTimestamp");
                    return_string += timestamp_string + ";" + value + "\n";
                }
                catch (Exception e) {
                    //System.out.println(results.get(i));
                    //e.printStackTrace();
                }

                //System.out.println("time: " + timestamp + "; value: " + value);
                // time string in this format:
                // 2014/04/08 23:00:00
                //Date timestamp_date = new Date(timestamp_string);
                //System.out.println(timestamp_date);

            }


        } catch (JSONException e) {

            //didn't find something it was supposed to
            e.printStackTrace();
            Log.i("JSONException", e.toString());


        }

        return return_string;
    }

    /*
     * Will retrieve weather data from http://w1.weather.gov/ and update data stored in a file
     * not implemented yet!
     */
    private void syncWeatherData() throws IOException {
//        try {
//
//            // testing XML parsing right now
//            readFeed();
//
//
//        } catch (Exception e) {
//
//            //didn't find something it was supposed to
//            e.printStackTrace();
//            Log.i("xml_error", e.toString());
//
//
//        }

    }

    /*
     * This is testing XML Parsing from a string
     */
//    private void readFeed() throws XmlPullParserException, IOException {
//
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        factory.setNamespaceAware(true);
//        XmlPullParser xpp = factory.newPullParser();
//
//        String xml_string = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
//                "<?xml-stylesheet href=\"latest_ob.xsl\" type=\"text/xsl\"?>\n" +
//                "<current_observation version=\"1.0\"\n" +
//                "\t xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
//                "\t xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
//                "\t xsi:noNamespaceSchemaLocation=\"http://www.weather.gov/view/current_observation.xsd\">\n" +
//                "\t<credit>NOAA's National Weather Service</credit>\n" +
//                "\t<credit_URL>http://weather.gov/</credit_URL>\n" +
//                "\t<image>\n" +
//                "\t\t<url>http://weather.gov/images/xml_logo.gif</url>\n" +
//                "\t\t<title>NOAA's National Weather Service</title>\n" +
//                "\t\t<link>http://weather.gov</link>\n" +
//                "\t</image>\n" +
//                "\t<suggested_pickup>15 minutes after the hour</suggested_pickup>\n" +
//                "\t<suggested_pickup_period>60</suggested_pickup_period>\n" +
//                "\t<location>Stanton Airfield, MN</location>\n" +
//                "\t<station_id>KSYN</station_id>\n" +
//                "\t<latitude>44.467</latitude>\n" +
//                "\t<longitude>-93.017</longitude>\n" +
//                "\t<observation_time>Last Updated on May 10 2014, 7:52 pm CDT</observation_time>\n" +
//                "        <observation_time_rfc822>Sat, 10 May 2014 19:52:00 -0500</observation_time_rfc822>\n" +
//                "\t<weather>Light Rain</weather>\n" +
//                "\t<temperature_string>60.0 F (15.3 C)</temperature_string>\n" +
//                "\t<temp_f>60.0</temp_f>\n" +
//                "\t<temp_c>15.3</temp_c>\n" +
//                "\t<relative_humidity>74</relative_humidity>\n" +
//                "\t<wind_string>East at 11.5 MPH (10 KT)</wind_string>\n" +
//                "\t<wind_dir>East</wind_dir>\n" +
//                "\t<wind_degrees>110</wind_degrees>\n" +
//                "\t<wind_mph>11.5</wind_mph>\n" +
//                "\t<wind_kt>10</wind_kt>\n" +
//                "\t<pressure_in>29.78</pressure_in>\n" +
//                "\t<dewpoint_string>51.1 F (10.6 C)</dewpoint_string>\n" +
//                "\t<dewpoint_f>51.1</dewpoint_f>\n" +
//                "\t<dewpoint_c>10.6</dewpoint_c>\n" +
//                "\t<windchill_string>58 F (14 C)</windchill_string>\n" +
//                "      \t<windchill_f>58</windchill_f>\n" +
//                "      \t<windchill_c>14</windchill_c>\n" +
//                "\t<visibility_mi>10.00</visibility_mi>\n" +
//                " \t<icon_url_base>http://forecast.weather.gov/images/wtf/small/</icon_url_base>\n" +
//                "\t<two_day_history_url>http://www.weather.gov/data/obhistory/KSYN.html</two_day_history_url>\n" +
//                "\t<icon_url_name>ra.png</icon_url_name>\n" +
//                "\t<ob_url>http://www.weather.gov/data/METAR/KSYN.1.txt</ob_url>\n" +
//                "\t<disclaimer_url>http://weather.gov/disclaimer.html</disclaimer_url>\n" +
//                "\t<copyright_url>http://weather.gov/disclaimer.html</copyright_url>\n" +
//                "\t<privacy_policy_url>http://weather.gov/notice.html</privacy_policy_url>\n" +
//                "</current_observation>\n";
//
//        xpp.setInput(new StringReader(xml_string));
//        int eventType = xpp.getEventType();
//        boolean read_next = false;
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if(eventType == XmlPullParser.START_DOCUMENT) {
//                //System.out.println("Start document");
//            } else if(eventType == XmlPullParser.START_TAG) {
//                String tag_name = xpp.getName();
//                if (tag_name.equals("temp_f")) {
//                    Log.i("xml", "temp_f exists");
//                    read_next = true;
//                }
//                //System.out.println("Start tag "+xpp.getName());
//            } else if(eventType == XmlPullParser.END_TAG) {
//                //System.out.println("End tag "+xpp.getName());
//            } else if(eventType == XmlPullParser.TEXT && read_next) {
//                //System.out.println(xpp.getText());
//                read_next = false;
//
//            }
//            eventType = xpp.next();
//        }
//        //System.out.println("End document");
//
//    }

}