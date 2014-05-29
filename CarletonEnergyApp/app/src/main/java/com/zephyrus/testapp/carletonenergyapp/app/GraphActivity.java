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

public class GraphActivity extends Activity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        if(!isLandscape()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public boolean isLandscape() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return true;
        else
            return false;
    }*/

    private XYPlot plot;
    private CarletonEnergyDataSource dataSource;
    private String dependentVariable = "production1";
    private String increment = "year";
    private Calendar startTime;
    private Calendar endTime;
    private String graphTitle;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataSource = CarletonEnergyDataSource.getSingleton();

        if(!isLandscape()){
            Intent intent = new Intent(this, MainActivity.class);
            int jumpToData = 1;
            intent.putExtra("jumpToTab",jumpToData);
            startActivity(intent);
            finish();
        }

        // fun little snippet that prevents users from taking screenshots
        // on ICS+ devices :-)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_graph);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.graphActivityPlot);

        //This will hopefully allow us to fix the scale along the x-axis
        //right now, the axis is always in ms :/
        Number scaleAxis;

        if(increment.equals("week")){
            scaleAxis = 1;
            graphTitle = "This Week's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -7);
        }
        else if (increment.equals("month")){
            scaleAxis = 1;
            graphTitle = "This Month's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.HOUR, -1);
        }
        else if (increment.equals("year")){
            scaleAxis = 1;
            graphTitle = "This Year's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.YEAR, -1);
        }
        else { //day by default
            scaleAxis = 1;
            graphTitle = "Today's Energy";
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -1);
        }

        ArrayList<Double> productionGraphData = dataSource.getGraphData("production1", startTime.getTime(), endTime.getTime(), increment);
        ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", startTime.getTime(), endTime.getTime(), increment);
        Log.i("graph_data", productionGraphData.size() + "");
        Log.i("graph_data", productionGraphData.size() + "");

        //Converting ArrayList<Double> to Number[]
        Number[] productionNums = new Number[productionGraphData.size()];
        for(int i = 0; i<productionNums.length; i++){
            productionNums[i] = productionGraphData.get(i);
            System.out.println(productionNums[i]);
        }

        Number[] consumptionNums = new Number[consumptionGraphData.size()];
        for(int i = 0; i<consumptionNums.length; i++){
            consumptionNums[i] = consumptionGraphData.get(i);
        }

        Number[] timeNums = new Number[productionGraphData.size()];
        //timeNums = (Number[]) productionGraphData.toArray(timeNums);
        for(int i = 0; i<timeNums.length; i++){
            timeNums[i] = i;
        }

        // create our series from our array of nums:
        XYSeries production = new SimpleXYSeries(
                Arrays.asList(timeNums),
                Arrays.asList(productionNums),
                "Production");

        XYSeries consumption = new SimpleXYSeries(
               Arrays.asList(timeNums),
               Arrays.asList(consumptionNums),
               "Consumption");

        ///plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        ///plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        //plot.getGraphWidget().getDomainGridLinePaint().
          //      setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        ///plot.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
        //plot.getGraphWidget().getRangeGridLinePaint().
          //      setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().setSize(new SizeMetrics(1, SizeLayoutType.FILL, 10, SizeLayoutType.FILL));

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        /*LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 0, 100),                   // line color
                Color.rgb(0, 0, 0),                   // point color
                Color.rgb(0, 0, 0), null);                // fill color
        */
        // setup our line fill paint to be a slightly transparent gradient:
        //Paint lineFill = new Paint();
        //lineFill.setAlpha(200);

        // ugly usage of LinearGradient. unfortunately there's no way to determine the actual size of
        // a View from within onCreate.  one alternative is to specify a dimension in resources
        // and use that accordingly.  at least then the values can be customized for the device type and orientation.
        //lineFill.setShader(new LinearGradient(0, 0, 200, 200, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));

        LineAndPointFormatter proFormatter = new LineAndPointFormatter(Color.rgb(0,51,102), Color.rgb(0,51,102), null, null);
        //formatter.setFillPaint(lineFill);
        //proFormatter.setFillPaint(null);
        plot.getGraphWidget().setPaddingRight(2);
        //plot.addSeries(production, proFormatter);


        LineAndPointFormatter conFormatter = new LineAndPointFormatter(Color.rgb(153,0,0), Color.rgb(153,0,0), null, null);
        //conFormatter.setFillPaint(null);
        plot.addSeries(consumption, conFormatter);
        plot.addSeries(production, proFormatter);
        // draw a domain tick for each year:
        plot.setDomainStep(XYStepMode.SUBDIVIDE, timeNums.length);

        // customize our domain/range labels
        plot.setDomainLabel("Time");
        plot.setRangeLabel("Power (kW)");

        plot.setTitle(graphTitle);

        // get rid of decimal points in our range labels:
        plot.setRangeValueFormat(new DecimalFormat("0"));

        plot.setDomainValueFormat(new Format() {

            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.
            private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });
    }

    public boolean isLandscape() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return true;
        else
            return false;
    }
}
