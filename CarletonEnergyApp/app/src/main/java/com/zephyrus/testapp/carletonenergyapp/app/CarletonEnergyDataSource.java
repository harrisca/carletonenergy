package com.zephyrus.testapp.carletonenergyapp.app;
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
        return 0.0;
    }

    double getCurrentWindSpeed() {
        return 0.0;
    }

    void getGraphData(int thing_to_graph, Date start_time, Date end_time, int increment) {
        // figure this out later
    }

    int checkForAlert() {
        return 0;
    }

    void sync() {

    }



}
