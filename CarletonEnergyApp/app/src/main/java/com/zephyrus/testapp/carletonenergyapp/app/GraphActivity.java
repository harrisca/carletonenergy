package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Displays a graph according to the buttons selected in DataFragment.
//This screen is accessed by rotating the device from Portrait to Landscape while
//on the DataFragment sceen.

public class GraphActivity extends Activity {

    private XYPlot plot;
    private CarletonEnergyDataSource dataSource;
    private static boolean productionChecked;
    private static boolean consumptionChecked;
    private String buttonClickedStr;
    private String increment = "quarterhour";     //must be quarter-hour for day; hour for week; day for month/year
    private Calendar startTime;
    private Calendar endTime;
    private String graphTitle = "Energy Data";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        buttonClickedStr = getIntent().getStringExtra("buttonClickedStr");
        productionChecked = getIntent().getBooleanExtra("productionChecked", true);
        consumptionChecked = getIntent().getBooleanExtra("consumptionChecked", true);

        dataSource = CarletonEnergyDataSource.getSingleton();

        //Switches to DataFragment when phone is rotated to Portrait
        if(!isLandscape()){
            Intent intent = new Intent(this, MainActivity.class);
            int jumpToData = 1;
            intent.putExtra("jumpToTab",jumpToData);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_graph);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.graphActivityPlot);

        //Set start times, end times, graph title, and increment based on which button was clicked
        if(buttonClickedStr.equals("week")){
            graphTitle = "This Week's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -7);
            increment = "hour";
        }
        else if (buttonClickedStr.equals("month")){
            graphTitle = "This Month's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.MONTH, -1);
            increment = "day";
        }
        else if (buttonClickedStr.equals("year")){
            graphTitle = "This Year's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.YEAR, -1);
            increment = "day";
        }
        else { //day by default
            graphTitle = "Today's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -1);
            increment = "quarterhour";
        }

        //Get our data from the CarletonEnergyDataSource
        ArrayList<Double> productionGraphData = dataSource.getGraphData("production1", startTime.getTime(), endTime.getTime(), increment);
        ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", startTime.getTime(), endTime.getTime(), increment);

        //Converting ArrayList<Double> to Number[]
        Number[] productionNums = new Number[productionGraphData.size()];
        for(int i = 0; i<productionNums.length; i++){
            productionNums[i] = productionGraphData.get(i);
        }

        Number[] consumptionNums = new Number[consumptionGraphData.size()];
        for(int i = 0; i<consumptionNums.length; i++){
            consumptionNums[i] = consumptionGraphData.get(i);
        }

        Number[] timeNums = new Number[productionGraphData.size()];

        for (int i = 0; i < timeNums.length; i++) {
            // calculates the number of milliseconds that passed between consecutive data points
            long increment_ms = 0l;
            if (increment.equals("quarterhour")) {
                increment_ms = 15 * 60 * 1000;
            } else if (increment.equals("hour")) {
                increment_ms = 60 * 60 * 1000;
            } else if (increment.equals("day")) {
                increment_ms = 24 * 60 * 60 * 1000;
            }

            // sets the time for each data point based on that increment
            timeNums[i] = i * increment_ms + startTime.getTime().getTime();
        }

        // create our production series from our array of nums:
        XYSeries production = new SimpleXYSeries(
                Arrays.asList(timeNums),
                Arrays.asList(productionNums),
                "Production");

        // create our consumption series from our array of nums:
        XYSeries consumption = new SimpleXYSeries(
               Arrays.asList(timeNums),
               Arrays.asList(consumptionNums),
               "Consumption");

        //Formatting our graph display
        XYGraphWidget gw = plot.getGraphWidget();

        gw.getDomainOriginLinePaint().setColor(Color.BLACK);
        gw.getRangeOriginLinePaint().setColor(Color.BLACK);
        gw.setSize(new SizeMetrics(50, SizeLayoutType.FILL, 50, SizeLayoutType.FILL));
        gw.setPaddingRight(2);

        LineAndPointFormatter proFormatter = new LineAndPointFormatter(Color.rgb(0,51,102), Color.rgb(0,51,102), null, null);
        LineAndPointFormatter conFormatter = new LineAndPointFormatter(Color.rgb(153,0,0), Color.rgb(153,0,0), null, null);

        //Graph production and consumption only if they were checked
        if(productionChecked == true) {
            plot.addSeries(production, proFormatter);
        }
        if(consumptionChecked == true) {
            plot.addSeries(consumption, conFormatter);
        }

        // customize our domain/range labels
        plot.setDomainLabel("Time");
        plot.setRangeLabel("Power (kW)");

        plot.setTitle(graphTitle);

        // get rid of decimal points in our range labels:
        plot.setRangeValueFormat(new DecimalFormat("0"));

        //Block deals with date formatting for our ticks and labels
        if (buttonClickedStr.equals("day")) {
            plot.setDomainValueFormat(new Format() {

                // create a simple date format that draws on the year portion of our timestamp.
                // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
                // for a full description of SimpleDateFormat.
                private SimpleDateFormat dateFormat = new SimpleDateFormat("K:mm");

                @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                    // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                    // we multiply our timestamp by 1000:
                    long timestamp = ((Number) obj).longValue();
                    Date date = new Date(timestamp);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
                    return dateFormat.format(date, toAppendTo, pos);
                }

                @Override
                public Object parseObject(String source, ParsePosition pos) {
                    return null;
                }
            });
        }
        else if (buttonClickedStr.equals("month") || buttonClickedStr.equals("week")) {

            if (buttonClickedStr.equals("week")) {
                // draw a domain tick for each day:
                plot.setDomainStep(XYStepMode.SUBDIVIDE, 7);
            }

            plot.setDomainValueFormat(new Format() {

                // create a simple date format that draws on the year portion of our timestamp.
                // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
                // for a full description of SimpleDateFormat.
                private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

                @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                    // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                    // we multiply our timestamp by 1000:
                    long timestamp = ((Number) obj).longValue();
                    Date date = new Date(timestamp);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
                    return dateFormat.format(date, toAppendTo, pos);
                }

                @Override
                public Object parseObject(String source, ParsePosition pos) {
                    return null;
                }
            });
        }
        else if (buttonClickedStr.equals("year")) {
            // show tick for each month
            plot.setDomainStep(XYStepMode.SUBDIVIDE, 12);
            plot.setDomainValueFormat(new Format() {

                // create a simple date format that draws on the year portion of our timestamp.
                // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
                // for a full description of SimpleDateFormat.
                private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");


                @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                    // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                    // we multiply our timestamp by 1000:
                    long timestamp = ((Number) obj).longValue();
                    Date date = new Date(timestamp);
                    dateFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
                    return dateFormat.format(date, toAppendTo, pos);
                }

                @Override
                public Object parseObject(String source, ParsePosition pos) {
                    return null;

                }
            });
        }
    }

    //Returns true if the device is in landscape
    public boolean isLandscape() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return true;
        else
            return false;
    }
}