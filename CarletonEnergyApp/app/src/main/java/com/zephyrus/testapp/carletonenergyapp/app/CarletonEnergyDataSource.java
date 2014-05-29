package com.zephyrus.testapp.carletonenergyapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
import java.util.SimpleTimeZone;

//This is a class for dealing with energy
public class CarletonEnergyDataSource {
    public static CarletonEnergyDataSource singleton;
    private String speedUnits = "US";
    private String degreeUnits;
    private double currentTemperature = 0.0;
    private double currentWindspeed = 0.0;
    private double liveProduction1 = 0.0;
    private double liveProduction2 = 0.0;
    private double liveConsumption = 0.0;
    private Date lastUpdated = null;
    private double[] oldData = new double[5];
    private Context context;
    public static final String PREFS_NAME = "preferences";
    SharedPreferences sharedPref;
    int notificationToggle;

    public CarletonEnergyDataSource(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.liveConsumption = (double)sharedPref.getFloat("liveConsumption", (float)-1.0);
        this.liveProduction1 = (double)sharedPref.getFloat("liveProduction1", (float)-1.0);
        this.liveProduction2 = (double)sharedPref.getFloat("liveProduction2", (float)-1.0);
        this.currentTemperature = (double)sharedPref.getFloat("currentTemperature", (float)-1.0);
        this.currentWindspeed = (double)sharedPref.getFloat("currentWindspeed", (float)-1.0);

        //degreeUnits = sharedPref.getString("degreeUnits", "C");
        //notificationToggle = sharedPref.getInt("notifications", 0);
    }

    public static CarletonEnergyDataSource getSingleton() {
        return singleton;
    }
    public static void setSingleton(CarletonEnergyDataSource newSource) {
        singleton = newSource;
    }
    public Date getTimeUpdated() {
        return lastUpdated;
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

        return liveConsumption;
    }


    /*
     * Returns a double representing the current temp according to weather.carleton.edu in degrees C
     */
    public double getCurrentTemperature() {
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
     * Returns a double ArrayList with a value for each time increment from start_time to end_time
     * (inclusive to the second if applicable),
     * where the value represents the energy production (or consumption) at that time in kWh.
     *
     * The dependent_variable could be production for either or both windmills or consumption
     * for the school. ex. "consumption", "production1"
     *
     * The increment should be "day", "hour", or "quarterhour".
     */
    public ArrayList<Double> getGraphData(String dependent_variable, Date start_time, Date end_time, String increment) {
        ArrayList<Double> valueList = new ArrayList<Double>();

        try {
            FileInputStream in = context.openFileInput(increment + "_" + dependent_variable + "_data");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String time_string = line.substring(0, line.indexOf(';'));
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    df.setTimeZone(SimpleTimeZone.getTimeZone("US/Central"));
                    Date time = df.parse(time_string);
                    Double value = Double.parseDouble(line.substring(line.indexOf(';') + 1, line.length()));
                    if (!time.after(end_time) && !time.before(start_time)) {
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

                try {
                    syncWeatherData();
                } catch (IOException e) {
                    Log.i("sync", "error syncing weather data");
                    e.printStackTrace();
                }
                try {
                    syncEnergyData();
                } catch (IOException e) {
                    Log.i("sync", "error syncing energy data");
                    e.printStackTrace();
                }

                lastUpdated = new Date();
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                df.setTimeZone(SimpleTimeZone.getTimeZone("US/Central"));

                Log.i("sync", "synced at: " + lastUpdated);
                Log.i("sync/time", "local time: " + df.format(lastUpdated));
                Log.i("sync", "consumption " + getLiveConsumption());
                Log.i("sync", "windmill1 " + getLiveProduction(1));
                Log.i("sync", "temp " + getCurrentTemperature());
                Log.i("sync", "wind " + getCurrentWindSpeed());

                //Log.i("sync", "" + getGraphData("consumption", ));
            }
        }).start();

    }

    /*
     * Retrieve energy data from lucid and update data stored in files
     */
    private int syncEnergyData() throws IOException {

        // initialize some useful dates
        Calendar today = Calendar.getInstance();
        //today.add(Calendar.MINUTE, -15);
        Calendar year_ago = Calendar.getInstance();
        year_ago.add(Calendar.YEAR, -1);
        Calendar month_ago = Calendar.getInstance();
        month_ago.add(Calendar.MONTH, -1);
        Calendar day_ago = Calendar.getInstance();
        day_ago.add(Calendar.DATE, -1);

        // get data and store in separate files for each time-range and data point


        // quarter-hourly consumption for past 24 hours
        String quarter_hourly_consumption = readEnergyJSON(day_ago.getTime(), today.getTime(), "quarterhour", "carleton_campus_en_use");
        Log.i("quarter_hourly_consumption", quarter_hourly_consumption);
        // update liveConsumption based on data from most recent complete 1/4 hour
        String[] consumption_list = quarter_hourly_consumption.split("[\n|\r]");
        String recent_consumption_line = consumption_list[consumption_list.length - 2];
        Log.i("syncEnergyData", recent_consumption_line);
        // lucid data is returned in average kW over time period - this is ok for live
        liveConsumption = (Double.parseDouble(recent_consumption_line.substring(recent_consumption_line.indexOf(';') + 1, recent_consumption_line.length())));
        Log.i("syncEnergyData", "liveConsumption: " + liveConsumption);

        // quarter-hourly windmill1 production for past 24 hours
        String quarter_hourly_production1 = readEnergyJSON(day_ago.getTime(), today.getTime(), "quarterhour", "carleton_turbine1_produced_power");
        Log.i("quarter_hourly_production1", quarter_hourly_production1);
        // update liveProduction based on data from most recent complete 1/4 hour
        String[] production1_list = quarter_hourly_production1.split("[\n|\r]");
        String recent_production1_line = production1_list[production1_list.length - 2];
        Log.i("syncEnergyData", recent_production1_line);
        liveProduction1 = (Double.parseDouble(recent_production1_line.substring(recent_production1_line.indexOf(';') + 1, recent_production1_line.length())));
        Log.i("energyData", "" + liveProduction1);

        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putFloat("liveConsumption", (float)liveConsumption);
        ed.putFloat("liveProduction1", (float)liveProduction1);


        // update graph data file
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("quarterhour_consumption_data", Context.MODE_PRIVATE));
            out.writeUTF(quarter_hourly_consumption);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("quarterhour_production1_data", Context.MODE_PRIVATE));
            out.writeUTF(quarter_hourly_production1);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }


        // daily consumption for past year
        String daily_consumption = readEnergyJSON(year_ago.getTime(), today.getTime(), "day", "carleton_campus_en_use");
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("day_consumption_data", Context.MODE_PRIVATE));
            out.writeUTF(daily_consumption);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }

        // daily windmill 1 production for past year
        String daily_production1 = readEnergyJSON(year_ago.getTime(), today.getTime(), "day", "carleton_turbine1_produced_power");
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("day_production1_data", Context.MODE_PRIVATE));
            out.writeUTF(daily_production1);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }

        // hourly consumption for past month
        String hourly_consumption = readEnergyJSON(month_ago.getTime(), today.getTime(), "hour", "carleton_campus_en_use");
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("hour_consumption_data", Context.MODE_PRIVATE));
            out.writeUTF(hourly_consumption);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }

        // hourly windmill1 production for past month
        String hourly_production1 = readEnergyJSON(month_ago.getTime(), today.getTime(), "hour", "carleton_turbine1_produced_power");
        try {
            DataOutputStream out =
                    new DataOutputStream(context.openFileOutput("hour_production1_data", Context.MODE_PRIVATE));
            out.writeUTF(hourly_production1);
            out.close();
        } catch (IOException e) {
            Log.i("syncEnergyData", "I/O Error");
        }


        return 0;



    }

    /*
     * Constructs a url for buildingos rest query based on arguments, then makes query and returns
     * results as a string in the format:
     *
     * timeStamp;value\n
     * timeStamp;value\n
     * etc.
     */
    private static String readEnergyJSON(Date start, Date end, String resolution, String point) throws IOException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd+HH:mm:ss");
        df.setTimeZone(SimpleTimeZone.getTimeZone("US/Central"));
        String url_string = "https://rest.buildingos.com/reports/timeseries/?start=" + df.format(start) + "&resolution=" + resolution + "&end=" + df.format(end) + "&name=" + point;
        URL consumption_url = new URL(url_string);
        Log.i("url", consumption_url.toString());
        InputStream in = consumption_url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result, line = reader.readLine();
        result = line;
        while((line=reader.readLine())!=null) {
            result += line;
        }
        String json_string = result;
        JSONObject all_electricity_page = null;
        String return_string = "";

        try {
            all_electricity_page = (JSONObject) new JSONTokener(json_string).nextValue();
            JSONArray results = (JSONArray)all_electricity_page.get("results");
            for (int i = 0; i < results.length(); i++) {
                Double value = -2.0;
                try {
                    if (results.get(i) != JSONObject.NULL
                            && ((JSONObject)results.get(i)).get(point) != JSONObject.NULL) {
                        value = ((JSONObject) ((JSONObject) results.get(i)).get(point)).getDouble("value");
                        //Log.i("not null", "getting here? value = " + value);

                    }
                    else {
                        value = 0.0;
                        String timestamp_string = ((JSONObject) results.get(i)).getString("startTimestamp");
                        Log.i("null", "adding -1.0? point=" + point + " time=" + timestamp_string);
                    }

                    String timestamp_string = ((JSONObject) results.get(i)).getString("startTimestamp");
                    return_string += timestamp_string + ";" + value + "\n";
                }
                catch (Exception e) {
                    //System.out.println(results.get(i));
                    e.printStackTrace();
                    //Log.i("null error?", "error: value = " + value);
                    //Log.i("null error?", e.toString());
                }
            }

        } catch (JSONException e) {
            //didn't find any results - url must have been wrong, or bad connection
            e.printStackTrace();
            Log.i("readEnergyJSON", "bad url? " + e.toString());
        }
        return return_string;
    }

    /*
     * Retrieves weather data from weather.carleton.edu and sets current temperature,
     * wind fields
     */
    private int syncWeatherData() throws IOException {
        URL carleton = null;
        try {
            carleton = new URL("http://weather.carleton.edu");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("syncWeatherData", "malformed URL for carleton weather");
            return -1;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(carleton.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("syncWeatherData", "openSteam IOException for carleton weather");
            return -1;
        }
        String inputLine;
        int lineNum = 0;
        String speedString = new String();
        String tempString = new String();
        try {
            while ((inputLine = in.readLine()) != null){
                if (lineNum == 126) {
                    tempString = inputLine;
                }
                else if (lineNum == 152) {
                    speedString = inputLine;
                }
                lineNum++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("syncWeatherData", "parsing IOException for carleton weather");
            return -1;
        }
        double temp = parseHTMLForTemp(tempString);
        int speed = parseHTMLForSpeed(speedString);

        currentTemperature = temp;
        currentWindspeed = speed;
        SharedPreferences.Editor e = sharedPref.edit();
        e.putFloat("currentTemperature", (float)currentTemperature);
        e.putFloat("currentWindspeed", (float)currentWindspeed);
        return 0;

    }

    /*
    * Returns a double from a line of html code received from weather.carleton.edu
    */
    private double parseHTMLForTemp(String line){
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
    private int parseHTMLForSpeed(String line){
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
     * This is testing XML Parsing from a string
     */
//    private void readFeed() throws XmlPullParserException, IOException {
//
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        factory.setNamespaceAware(true);
//        XmlPullParser xpp = factory.newPullParser();
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