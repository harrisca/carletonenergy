package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DataFragment extends Fragment {

    private CarletonEnergyDataSource dataSource;
    private XYPlot plot;
    private View fragView;
    private String dependentVariable = "production1";
    private String increment = "hour";
    private Calendar startTime;
    private Calendar endTime;
    private String timeUnit = "Day"; // you should be able to use increment instead of
    private RadioGroup rg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_data, container, false);

        if(!isPortrait()){
            Intent i = new Intent(this.getActivity(), GraphActivity.class);
            startActivity(i);
            getActivity().finish();
            RadioButton rb = (RadioButton)fragView.findViewById(R.id.radio_week);
            rb.setChecked(true);
        }

        final Calendar today = Calendar.getInstance();
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.HOUR, -24);
        final Calendar year_ago = Calendar.getInstance();
        year_ago.add(Calendar.YEAR, -1);
        final Calendar month_ago = Calendar.getInstance();
        month_ago.add(Calendar.MONTH, -1);
        final Calendar week_ago = Calendar.getInstance();
        week_ago.add(Calendar.DATE, -7);

        rg = (RadioGroup) fragView.findViewById(R.id.radioOption_time);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {

                Log.i("radioButtonTest", "starting onCheckedChanged");
                //onRadioButtonClicked(fragView.findViewById(checkedId));
                Log.i("radioButtonTest", "done with onCheckedChanged");

                switch (checkedId) {
                    case R.id.radio_day:
                            Log.i("radioButtonTest", "clicked day button");
                            timeUnit = "Day";
                            increment = "day";
                            startTime = yesterday;
                        break;
                    case R.id.radio_week:
                            Log.i("radioButtonTest", "clicked week button");
                            timeUnit = "Week";
                            increment = "week";
                            startTime = week_ago;
                        break;
                    case R.id.radio_month:
                        Log.i("radioButtonTest", "clicked month button");
                            timeUnit = "Month";
                            increment = "month";
                            startTime = month_ago;
                        break;
                    case R.id.radio_year:
                        Log.i("radioButtonTest", "clicked year button");
                            timeUnit = "Year";
                            increment = "year";
                            startTime = year_ago;
                        break;

                }
                dataSource = CarletonEnergyDataSource.getSingleton();

                Log.i("radioButtonTest", "starting to graph");
                // initialize our XYPlot reference:
                plot = (XYPlot) fragView.findViewById(R.id.mySimpleXYPlot);

                //This will hopefully allow us to fix the scale along the x-axis
                //right now, the axis is always in ms :/
                Number scaleAxis;

                if(increment.equals("week")){
                    scaleAxis = 1;
                    endTime = Calendar.getInstance();
                    startTime = Calendar.getInstance();
                    startTime.add(Calendar.DATE, -7);
                }
                else if (increment.equals("day")){
                    scaleAxis = 1;
                    endTime = Calendar.getInstance();
                    startTime = Calendar.getInstance();
                    startTime.add(Calendar.DATE, -1);
                }
                else { //hour by default
                    scaleAxis = 1;
                    endTime = Calendar.getInstance();
                    startTime = Calendar.getInstance();
                    startTime.add(Calendar.YEAR, -1);
                }

                ArrayList<Double> productionGraphData = dataSource.getGraphData(dependentVariable, startTime.getTime(), endTime.getTime(), increment);
                ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", startTime.getTime(), endTime.getTime(), increment);
                Log.i("graph_data", productionGraphData.size() + "");
                Log.i("graph_data", consumptionGraphData.size() + "");

                //Converting ArrayList<Double> to Number[]
                Number[] productionNums = new Number[productionGraphData.size()];
                for(int i = 0; i<productionNums.length; i++){
                    productionNums[i] = i;
//                    System.out.println(productionNums[i]);
                }

                Number[] consumptionNums = new Number[consumptionGraphData.size()];
                for(int i = 0; i<consumptionNums.length; i++){
                    consumptionNums[i] = i;
//                    System.out.println(consumptionNums[i]);
                }

                Number[] timeNums = new Number[productionGraphData.size()];
                timeNums = (Number[]) productionGraphData.toArray(timeNums);

                // create our series from our array of nums:
                XYSeries production = new SimpleXYSeries(
                        Arrays.asList(productionNums),
                        Arrays.asList(timeNums),
                        "Production");

                //XYSeries consumption = new SimpleXYSeries(
                //       Arrays.asList(consumptionNums),
                //     Arrays.asList(timeNums),
                //   "Consumption");

                plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
                plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
                plot.getGraphWidget().getDomainGridLinePaint().
                        setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
                plot.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
                plot.getGraphWidget().getRangeGridLinePaint().
                        setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
                plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
                plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
                plot.getGraphWidget().setSize(new SizeMetrics(
                        1, SizeLayoutType.FILL,
                        10, SizeLayoutType.FILL));

                // Create a formatter to use for drawing a series using LineAndPointRenderer:
                LineAndPointFormatter series1Format = new LineAndPointFormatter(
                        Color.rgb(0, 0, 100),                   // line color
                        Color.rgb(0, 0, 100),                   // point color
                        Color.rgb(0, 0, 200), null);                // fill color

                // setup our line fill paint to be a slightly transparent gradient:
                Paint lineFill = new Paint();
                lineFill.setAlpha(200);

                // ugly usage of LinearGradient. unfortunately there's no way to determine the actual size of
                // a View from within onCreate.  one alternative is to specify a dimension in resources
                // and use that accordingly.  at least then the values can be customized for the device type and orientation.
                lineFill.setShader(new LinearGradient(0, 0, 200, 200, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));

                LineAndPointFormatter formatter  =
                        new LineAndPointFormatter(Color.rgb(0, 0,0), Color.BLUE, Color.RED, null);
                formatter.setFillPaint(lineFill);
                plot.getGraphWidget().setPaddingRight(2);

                plot.addSeries(production, formatter);

                //plot.addSeries(consumption, new LineAndPointFormatter(Color.rgb(0, 0,0), Color.RED, Color.RED, null));

                // draw a domain tick for each year:
                plot.setDomainStep(XYStepMode.SUBDIVIDE, timeNums.length);

                // customize our domain/range labels
                plot.setDomainLabel("Time");

                plot.setRangeLabel("Power (kW)");

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

                Log.i("radioButtonTest", "done with onCreateView");
            }
        });



        return fragView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        fragView = null;
    }
    public boolean isPortrait() {
        Display getOrient = this.getActivity().getWindowManager().getDefaultDisplay();
        if (getOrient.getRotation()%2==0)
            return true;
        else
            return false;
    }


    /*public void onRadioButtonClicked(View view) {
        Log.i("radioButtonTest", "made it to onRadioButtonClicked");
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.HOUR, -24);

        View rg = fragView.findViewById(R.id.radioOption_time);

        Boolean checked = ((RadioButton) rg).isChecked();

        Calendar year_ago = Calendar.getInstance();
        year_ago.add(Calendar.YEAR, -1);
        Calendar month_ago = Calendar.getInstance();
        month_ago.add(Calendar.MONTH, -1);
        Calendar week_ago = Calendar.getInstance();
        week_ago.add(Calendar.DATE, -7);

        switch (fragView.getId()) {
            case R.id.radio_day:
                if (checked) {
                    Log.i("radioButtonTest", "clicked day button");
                    increment = "day";
                    startTime = yesterday;
                }
                break;
            case R.id.radio_week:
                if (checked) {
                    increment = "week";
                    startTime = week_ago;
                }
                break;
            case R.id.radio_month:
                if (checked) {
                    increment = "month";
                    startTime = month_ago;
                }
                break;
            case R.id.radio_year:
                if (checked) {
                    increment = "year";
                    startTime = year_ago;
                }
                break;
        }
        dataSource = CarletonEnergyDataSource.getSingleton();
        Log.i("radioButtonTest", "starting next graph");

        // initialize our XYPlot reference:
        plot = (XYPlot) fragView.findViewById(R.id.mySimpleXYPlot);


        //This will hopefully allow us to fix the scale along the x-axis
        //right now, the axis is always in ms :/
        Number scaleAxis;


        if(increment.equals("week")){
            scaleAxis = 1;
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -7);
        }
        else if (increment.equals("day")){
            scaleAxis = 1;
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.DATE, -1);
        }
        else { //hour by default
            scaleAxis = 1;
            endTime = Calendar.getInstance();
            startTime = Calendar.getInstance();
            startTime.add(Calendar.YEAR, -1);
        }

        ArrayList<Double> productionGraphData = dataSource.getGraphData(dependentVariable, startTime.getTime(), endTime.getTime(), increment);
        ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", startTime.getTime(), endTime.getTime(), increment);
        Log.i("graph_data", productionGraphData.size() + "");
        Log.i("graph_data", productionGraphData.size() + "");

        //Converting ArrayList<Double> to Number[]
        Number[] productionNums = new Number[productionGraphData.size()];
        for(int i = 0; i<productionNums.length; i++){
            productionNums[i] = i;
            System.out.println(productionNums[i]);
        }

        Number[] consumptionNums = new Number[consumptionGraphData.size()];
        for(int i = 0; i<consumptionNums.length; i++){
            consumptionNums[i] = i;
            System.out.println(consumptionNums[i]);
        }

        Number[] timeNums = new Number[productionGraphData.size()];
        timeNums = (Number[]) productionGraphData.toArray(timeNums);


        // create our series from our array of nums:
        XYSeries production = new SimpleXYSeries(
                Arrays.asList(productionNums),
                Arrays.asList(timeNums),
                "Production");

        //XYSeries consumption = new SimpleXYSeries(
        //       Arrays.asList(consumptionNums),
        //     Arrays.asList(timeNums),
        //   "Consumption");

        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainGridLinePaint().
                setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        plot.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeGridLinePaint().
                setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().setSize(new SizeMetrics(
                1, SizeLayoutType.FILL,
                10, SizeLayoutType.FILL));

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 0, 100),                   // line color
                Color.rgb(0, 0, 100),                   // point color
                Color.rgb(0, 0, 200), null);                // fill color

        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);

        // ugly usage of LinearGradient. unfortunately there's no way to determine the actual size of
        // a View from within onCreate.  one alternative is to specify a dimension in resources
        // and use that accordingly.  at least then the values can be customized for the device type and orientation.
        lineFill.setShader(new LinearGradient(0, 0, 200, 200, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));

        LineAndPointFormatter formatter  =
                new LineAndPointFormatter(Color.rgb(0, 0,0), Color.BLUE, Color.RED, null);
        formatter.setFillPaint(lineFill);
        plot.getGraphWidget().setPaddingRight(2);

        plot.addSeries(production, formatter);

        //plot.addSeries(consumption, new LineAndPointFormatter(Color.rgb(0, 0,0), Color.RED, Color.RED, null));

        // draw a domain tick for each year:
        plot.setDomainStep(XYStepMode.SUBDIVIDE, timeNums.length);

        // customize our domain/range labels
        plot.setDomainLabel("Time");

        plot.setRangeLabel("Power (kW)");

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

*/
}

