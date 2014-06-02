package com.zephyrus.testapp.carletonenergyapp.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//Screen provides buttons for the user to select the data they would like to view on a graph.
//Graph is viewed by rotating the screen to Landscape from Portrait which switches to GraphActivity.

public class HistoricFragment extends Fragment {
    //private XYPlot plot;
    //private CarletonEnergyDataSource dataSource;
    private static boolean productionChecked = true;
    private static boolean consumptionChecked = true;
    private static String buttonClickedStr = "day";
    private static int buttonClicked = R.id.radio_day;
    //private String increment = "quarterhour"; //must be quarter-hour for day; hour for week; day for month/year
    //private Calendar startTime;
    //private Calendar endTime;
    //private String graphTitle = "Energy Data";
    private View fragView;
    private RadioGroup rg;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragView = inflater.inflate(R.layout.fragment_historic, container, false);
        sharedPref = fragView.getContext().getSharedPreferences("preferences", 0);
        //Log.i("units", "units in WindFrag: " + units);


        switch (sharedPref.getInt("background", 0)) {
            case 0:
                fragView.setBackgroundResource(R.drawable.background_dawn);
                break;
            case 1:
                fragView.setBackgroundResource(R.drawable.background_day);
                break;
            case 2:
                fragView.setBackgroundResource(R.drawable.background_sunset);
                break;
        }


            if (!isPortrait()) {
            Intent i = new Intent(this.getActivity(), GraphActivity.class).putExtra("buttonClickedStr", buttonClickedStr);
            i.putExtra("productionChecked", productionChecked);
            i.putExtra("consumptionChecked", consumptionChecked);
            //RadioButton rb = (RadioButton) fragView.findViewById(R.id.radio_day);
            //rb.setChecked(true);
            startActivity(i);
            getActivity().finish();
        }

        CheckBox proCheckbox = (CheckBox) fragView.findViewById(R.id.checkbox_windmill);
        CheckBox conCheckbox = (CheckBox) fragView.findViewById(R.id.checkbox_consumption);

        proCheckbox.setChecked(productionChecked);
        conCheckbox.setChecked(consumptionChecked);

        RadioButton bc = (RadioButton) fragView.findViewById(buttonClicked);
        bc.setChecked(true);

        proCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton proCheckBox, boolean checked){
                if(proCheckBox.isChecked()){
                    productionChecked = true;
                }
                else {
                    productionChecked = false;
                }
            }
        });

        conCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton conCheckBox, boolean checked){
                if(conCheckBox.isChecked()){
                    consumptionChecked = true;
                }
                else {
                    consumptionChecked = false;
                }
            }
        });

        rg = (RadioGroup) fragView.findViewById(R.id.radioOption_time);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checkedId) {

                Log.i("radioButtonTest", "starting onCheckedChanged");
                //onRadioButtonClicked(fragView.findViewById(checkedId));
                Log.i("radioButtonTest", "done with onCheckedChanged");

                buttonClicked = checkedId;

                switch (checkedId) {
                    case R.id.radio_day:
                        //Log.i("radioButtonTest", "clicked day button");
                        buttonClickedStr = "day";
                        //graphTitle = "Today's Energy";
                        //endTime = Calendar.getInstance();
                        //startTime = Calendar.getInstance();
                        //startTime.add(Calendar.DATE, -1);
                        //increment = "quarterhour";
                        break;
                    case R.id.radio_week:
                        //Log.i("radioButtonTest", "clicked week button");
                        buttonClickedStr = "week";
                        //graphTitle = "This Week's Energy";
                        //endTime = Calendar.getInstance();
                        //startTime = Calendar.getInstance();
                        //startTime.add(Calendar.DATE, -7);
                        //increment = "hour";
                        break;
                    case R.id.radio_month:
                        //Log.i("radioButtonTest", "clicked month button");
                        buttonClickedStr = "month";
                        //graphTitle = "This Month's Energy";
                        //endTime = Calendar.getInstance();
                        //startTime = Calendar.getInstance();
                        //startTime.add(Calendar.MONTH, -1);
                        //increment = "day";
                        //graphTitle = "This Month's Energy";
                        break;
                    case R.id.radio_year:
                        //Log.i("radioButtonTest", "clicked year button");
                        buttonClickedStr = "year";
                        //graphTitle = "This Year's Energy";
                        //endTime = Calendar.getInstance();
                        //startTime = Calendar.getInstance();
                        //startTime.add(Calendar.YEAR, -1);
                        //increment = "day";
                        break;
                }

                /* Start Graphing code

                dataSource = CarletonEnergyDataSource.getSingleton();

                Log.i("radioButtonTest", "starting to graph");

                // initialize our XYPlot reference:
                //plot = (XYPlot) fragView.findViewById(R.id.dataFragGraph);

                ArrayList<Double> productionGraphData = dataSource.getGraphData("production1", startTime.getTime(), endTime.getTime(), increment);
                ArrayList<Double> consumptionGraphData = dataSource.getGraphData("consumption", startTime.getTime(), endTime.getTime(), increment);

                //Converting ArrayList<Double> to Number[]
                Number[] productionNums = new Number[productionGraphData.size()];
                for (int i = 0; i < productionNums.length; i++) {
                    productionNums[i] = productionGraphData.get(i);
                }

                Number[] consumptionNums = new Number[consumptionGraphData.size()];
                for (int i = 0; i < consumptionNums.length; i++) {
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

                // create our series from our array of nums:
                XYSeries production = new SimpleXYSeries(
                        Arrays.asList(timeNums),
                        Arrays.asList(productionNums),
                        "Production");

                XYSeries consumption = new SimpleXYSeries(
                        Arrays.asList(timeNums),
                        Arrays.asList(consumptionNums),
                        "Consumption");

                XYGraphWidget gw = plot.getGraphWidget();

                gw.getDomainOriginLinePaint().setColor(Color.BLACK);
                gw.getRangeOriginLinePaint().setColor(Color.BLACK);
                gw.setSize(new SizeMetrics(1, SizeLayoutType.FILL, 10, SizeLayoutType.FILL));
                gw.setPaddingRight(2);

                LineAndPointFormatter proFormatter = new LineAndPointFormatter(Color.rgb(0, 51, 102), Color.rgb(0, 51, 102), null, null);
                LineAndPointFormatter conFormatter = new LineAndPointFormatter(Color.rgb(153, 0, 0), Color.rgb(153, 0, 0), null, null);

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

                if (increment.equals("quarterhour")) {
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
                } else if (increment.equals("hour")) {
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
                } else if (increment.equals("day")) {
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
                } */ //end graphing code
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

    public void jumpToGraph(){
        Intent i = new Intent(this.getActivity(), GraphActivity.class).putExtra("buttonClickedStr", buttonClickedStr);
        i.putExtra("productionChecked", productionChecked);
        i.putExtra("consumptionChecked", consumptionChecked);
        startActivity(i);
        getActivity().finish();

    }
}

