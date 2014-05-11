package com.zephyrus.testapp.datatestapp.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.util.JsonReader;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

import java.io.InputStream;
import java.net.*;
import java.util.Date;

public class MainActivity extends Activity {


    /*public String getInternetData() throws Exception{

        BufferedReader in = null;
        String data = null;


        try
        {
            HttpClient client = new DefaultHttpClient();
            URI website = new URI("http://www.android.com");
            HttpGet request = new HttpGet();
            request.setURI(website);
            HttpResponse response = client.execute(request);
            response.getStatusLine().getStatusCode();

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String l = "";
            String nl = System.getProperty("line.separator");
            while ((l = in.readLine()) !=null){
                sb.append(l + nl);
            }
            in.close();
            data = sb.toString();
            return data;
        } finally{
            if (in != null){
                try{
                    in.close();
                    return data;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarletonEnergyDataSource dataSouce = new CarletonEnergyDataSource();
        dataSouce.sync();




        /*// Testing URL to string - should move this code back to CarletonEnergyDataSource
        //URL url = null;
        try {
            //url = new URL("https://rest.buildingos.com/reports/timeseries/?start=2014/05/07+20:00:00&resolution=hour&end=2014/5/07+20:00:00&name=carleton_campus_en_use");
            //url = new URL("http://cs.carleton.edu/faculty/jondich/courses/cs342_s14/assignments/assignment1.html");
            //HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            //String response = "http is working, sort of :(";



            URL url = new URL("http://www.android.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //readStream(in);
            }finally {
                urlConnection.disconnect();
            }
            URL url = new URL("http://www.android.com/");
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result, line = reader.readLine();
            result = line;
            while((line=reader.readLine())!=null){
                result+=line;
            }
            //System.out.println(result);



            //String response = String.format("%d", urlConnection.getResponseCode());
            //urlConnection.getResponseMessage();


            // is URLConnection even working?






            //InputStream in = urlConnection.getInputStream();

            //InputStreamReader reader = new InputStreamReader(in);
            //int max_size = 200000;
            //char[] buffer = new char[max_size];
            //reader.read(buffer, max_size, 0);
            //String json_string = new String(buffer);

            TextView textView=(TextView)findViewById(R.id.text);
            //text.setText(json_string);
            String result = getInternetData();
            textView.setText(result);
            textView.setTextColor(Color.RED);





        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
