package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {
    String info = "";
    JSONObject jsonWeather;
    TextView city1,temp1, time1, date1, weather1;
    TextView city2,temp2, time2, date2, weather2;
    TextView city3,temp3, time3, date3, weather3;
    Button getWeather;
    EditText longitude, latitude;
    ImageView image1, image2, image3;
    int code1, code2, code3;
    DownloadFilesTask task = new DownloadFilesTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city1 = findViewById(R.id.id_city1);
        temp1 = findViewById(R.id.id_temp1);
        image1 = findViewById(R.id.id_image1);
        time1 = findViewById(R.id.id_time1);
        date1 = findViewById(R.id.id_date1);
        weather1 = findViewById(R.id.id_weather1);

        city2 = findViewById(R.id.id_city2);
        temp2 = findViewById(R.id.id_temp2);
        image2 = findViewById(R.id.id_image2);
        time2 = findViewById(R.id.id_time2);
        date2 = findViewById(R.id.id_date2);
        weather2 = findViewById(R.id.id_weather2);

        city3 = findViewById(R.id.id_city3);
        temp3 = findViewById(R.id.id_temp3);
        image3 = findViewById(R.id.id_image3);
        time3 = findViewById(R.id.id_time3);
        date3 = findViewById(R.id.id_date3);
        weather3 = findViewById(R.id.id_weather3);

        getWeather = findViewById(R.id.id_getWeather);
        longitude = findViewById(R.id.id_long);
        latitude = findViewById(R.id.id_lat);


        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task == null){
                    task = new DownloadFilesTask();
                    task.execute();
                }
                task.cancel(true);
                DownloadFilesTask task2 = new DownloadFilesTask();
                task2.execute();
            }
        });
        task.onPostExecute(info);
    }

    public String Date (Long epochtime)
    {
        Date date = new Date(epochtime*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String finalDate = dateFormat.format(date);
        return finalDate;
    }

    public String time (Long epochtime)
    {
        Date time = new Date(epochtime*1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String finalTime = timeFormat.format(time);
        return finalTime;
    }

    public Double tempConvert(double temp)
    {
        temp = (1.8*(temp - 273.15)) + 32;
        return temp;
    }

    private class DownloadFilesTask extends AsyncTask <String, Void, String> {
        protected String doInBackground(String... Voids) {

                    URL myUrl = null;
                    try {
                         myUrl = new URL("http://api.openweathermap.org/data/2.5/find?lat="+latitude.getText()+"&&lon="+longitude.getText()+"&cnt=3&appid=5282972fee30a1a1b7be0b18b9bc341b");
                        Log.d("TAG_", "this is myurl: "+myUrl);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (myUrl != null) {
                        URLConnection myURLConnection = null;
                        try {
                            myURLConnection = myUrl.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        InputStream myInputStream = new InputStream() {
                            @Override
                            public int read() throws IOException {
                                return 0;
                            }
                        };

                        try {
                            myInputStream = myURLConnection.getInputStream();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream));

                        try {
                            info = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            jsonWeather = new JSONObject(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            return info;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

                    try {
                        jsonWeather = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jsonWeather != null) {
                        try {

                            JSONArray list = jsonWeather.getJSONArray("list");

                            //city 1
                            JSONObject firstCity = list.getJSONObject(0);
                            JSONObject main1 = firstCity.getJSONObject("main");
                            JSONArray firstWeather = firstCity.getJSONArray("weather");
                            JSONObject zero = firstWeather.getJSONObject(0);

                            String name1 = firstCity.getString("name");
                            Double temperature1 = Math.round(tempConvert(main1.getDouble("temp"))*100.0)/100.0;
                            String desc1 = zero.getString("description");
                            int imageId = zero.getInt("id");
                            Long epochtime = firstCity.getLong("dt");

                            if (imageId >= 200 && imageId <= 232)
                                code1 = R.drawable.tstorms;
                            if (imageId >= 300 && imageId <= 531)
                                code1 = R.drawable.rain;
                            if (imageId >= 600 && imageId <= 622)
                                code1 = R.drawable.snow;
                            if(imageId == 800)
                                code1 = R.drawable.clear;
                            if (imageId >= 801 && imageId <= 804)
                                code1 = R.drawable.cloud;

                            city1.setText(""+name1);
                            temp1.setText(""+temperature1+"°F");
                            image1.setImageResource(code1);
                            date1.setText(Date(epochtime));
                            time1.setText(time(epochtime)+" EST");
                            weather1.setText(""+desc1);

                            //city2
                            JSONObject secondCity = list.getJSONObject(1);
                            JSONObject main2 = secondCity.getJSONObject("main");
                            JSONArray secondWeather = secondCity.getJSONArray("weather");
                            JSONObject zero2 = secondWeather.getJSONObject(0);

                            String name2 = secondCity.getString("name");
                            Double temperature2 = Math.round(tempConvert(main2.getDouble("temp"))*100.0)/100.0;
                            String desc2 = zero2.getString("description");
                            int imageId2 = zero2.getInt("id");
                            Long epochtime2 = secondCity.getLong("dt");

                            if (imageId2 >= 200 && imageId2 <= 232)
                                code2 = R.drawable.tstorms;
                            if (imageId2 >= 300 && imageId2 <= 531)
                                code2 = R.drawable.rain;
                            if (imageId2 >= 600 && imageId2 <= 622)
                                code2 = R.drawable.snow;
                            if(imageId2 == 800)
                                code2 = R.drawable.clear;
                            if (imageId2 >= 801 && imageId2 <= 804)
                                code2 = R.drawable.cloud;

                            city2.setText(""+name2);
                            temp2.setText(""+temperature2+"°F");
                            image2.setImageResource(code2);
                            date2.setText(Date(epochtime2));
                            time2.setText(time(epochtime)+" EST");
                            weather2.setText(""+desc2);


                            //city3
                            JSONObject thirdCity = list.getJSONObject(2);
                            JSONObject main3 = thirdCity.getJSONObject("main");
                            JSONArray thirdWeather = thirdCity.getJSONArray("weather");
                            JSONObject zero3 = thirdWeather.getJSONObject(0);

                            String name3 = thirdCity.getString("name");
                            Double temperature3 = Math.round(tempConvert(main3.getDouble("temp"))*100.0)/100.0;
                            String desc3 = zero3.getString("description");
                            int imageId3 = zero3.getInt("id");
                            Long epochtime3 = thirdCity.getLong("dt");

                            if (imageId3 >= 200 && imageId3 <= 232)
                                code3 = R.drawable.tstorms;
                            if (imageId3 >= 300 && imageId3 <= 531)
                                code3 = R.drawable.rain;
                            if (imageId3 >= 600 && imageId3 <= 622)
                                code3 = R.drawable.snow;
                            if(imageId3 == 800)
                                code3 = R.drawable.clear;
                            if (imageId3 >= 801 && imageId3 <= 804)
                                code3 = R.drawable.cloud;

                            city3.setText(""+name3);
                            temp3.setText(""+temperature3+"°F");
                            weather3.setText(""+desc3);
                            date3.setText(Date(epochtime3));
                            time3.setText(time(epochtime)+" EST");
                            image3.setImageResource(code3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }
            }

    }
}




