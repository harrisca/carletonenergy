package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;


public class DataFragment extends Fragment {

    private XYPlot plot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_data, container, false);

        /*
        if(!isPortrait()){
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            startActivity(intent);
        }
        */

        // initialize our XYPlot reference:
        plot = (XYPlot) getActivity().findViewById(R.id.mySimpleXYPlot);

        // Create a couple arrays of y-values to plot:
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        Number[] series2Numbers = {4, 6, 3, 8, 2, 10};

        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");                             // Set the display title of the series

        // same as above
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:

        //Method 1 of adding a series ------
        //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);
        //series1Format.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
        //series1Format.configure(getActivity().getApplicationContext(), R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        //plot.addSeries(series1, series1Format);
        //------


        //Method 2 -------
        plot.addSeries(
                series1,
                new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100),
                null,
                (PointLabelFormatter) null));

        // same as above:
        //LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.YELLOW, Color.BLUE, Color.GREEN, null);
        //series2Format.setPointLabelFormatter(new PointLabelFormatter(Color.BLACK));
        //series2Format.configure(getActivity().getApplicationContext(), R.xml.line_point_formatter_with_plf2);
        //plot.addSeries(series2, series2Format);

        plot.addSeries(
                series2,
                new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100),
                        null,
                        (PointLabelFormatter) null));

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);



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
