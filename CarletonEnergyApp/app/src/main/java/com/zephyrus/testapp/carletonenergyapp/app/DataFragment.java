package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_data, container, false);

        dataSource = new CarletonEnergyDataSource(this.getActivity());

        /*
        if(!isPortrait()){
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            startActivity(intent);
        }
        */
        fragView = rootView;




        // initialize our XYPlot reference:
        plot = (XYPlot) fragView.findViewById(R.id.mySimpleXYPlot);

        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        String dependentVariable = "production1";
        String increment = "hour";

        //This will hopefully allow us to fix the scale along the x-axis
        //right now, the axis is always in ms :/
        Number scaleAxis;

        if(increment.equals("quarterhour")){
            scaleAxis = 1;
        }
        else if (increment.equals("hour")){
            scaleAxis = 1;
        }
        else scaleAxis = 1;

        ArrayList<Double> productionGraphData = dataSource.getGraphData(dependentVariable, yesterday.getTime(), today.getTime(), increment);
        ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", yesterday.getTime(), today.getTime(), "hour");
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
        });


        return rootView;
    }

    public boolean isPortrait() {
        Display getOrient = this.getActivity().getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() >= getOrient.getHeight())
            return false;
        else
            return true;
    }


}
