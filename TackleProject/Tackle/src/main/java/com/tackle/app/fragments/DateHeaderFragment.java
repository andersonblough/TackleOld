package com.tackle.app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.R;
import com.tackle.app.Weather.JSONWeatherParser;
import com.tackle.app.Weather.Weather;
import com.tackle.app.Weather.WeatherHTTPClient;

import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by Bill on 11/5/13.
 */
public class DateHeaderFragment extends Fragment {

    private static final int MILLI_PER_SECOND = 1000;
    private static final int SEC_PER_HOUR = 3600;
    private static final int THREE_HOURS = (3 * SEC_PER_HOUR * MILLI_PER_SECOND);

    public long[] daysOfWeek;
    public Weather[] mForecast;

    private TextView[] daysTV;
    private TextView[] datesTV;
    private ImageView[] weatherIcons;
    private Integer lastDay;
    private long date;

    public DateHeaderFragment(long dateTime){
        super();
        date = dateTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daysTV = new TextView[5];
        datesTV = new TextView[5];
        weatherIcons = new ImageView[5];

        /// Method used to get the current days of week to be used in the text views ///
        initDays();
        /// Method used to load Weather data. when finished loading the image views will update ///
        runnable.run();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "resumed", Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.framgent_dateheader, container, false);
        initViews(view);

        if (savedInstanceState != null){
            lastDay = savedInstanceState.getInt("lastDay");
            daysOfWeek = savedInstanceState.getLongArray("daysOfWeek");

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        if (lastDay != null){
            /// Resuming from previous state
            ///

            Calendar c = Calendar.getInstance();
            int thisDay = c.get(Calendar.DAY_OF_YEAR);
            if (lastDay != thisDay){
                /// New Day. Reload everything
                initDays();
                setDaysOfWeek();
                handler.removeCallbacks(runnable);
                runnable.run();
                //Toast.makeText(getActivity(), "New Day", Toast.LENGTH_SHORT).show();
            }
            else
                /// Same Day
                /// Set up text and image views
                setDaysOfWeek();
                setWeatherIcons();
        }

        setDaysOfWeek();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Calendar cal = Calendar.getInstance();
        lastDay = cal.get(Calendar.DAY_OF_YEAR);
        initDays();
        outState.putInt("lastDay", lastDay);
        outState.putLongArray("daysOfWeek", daysOfWeek);
    }

    private void setWeatherIcons(){
        if (mForecast != null){
            for (int i = 0; i < mForecast.length; i++){
                Weather weather = mForecast[i];

                if (weather != null){
                    int weatherID = weather.getWeatherId();

                    if (weatherID >=200 && weatherID <= 232){
                        weatherIcons[i].setImageResource(R.drawable.weather_thunderstorm);
                    }
                    else if (weatherID >= 300 && weatherID <= 522){
                        weatherIcons[i].setImageResource(R.drawable.weather_rain);
                    }
                    else if (weatherID >= 600 && weatherID <= 621){
                        weatherIcons[i].setImageResource(R.drawable.weather_snow);
                    }
                    else if (weatherID == 800){
                        weatherIcons[i].setImageResource(R.drawable.weather_clear);
                    }
                    else if (weatherID >= 801 && weatherID <= 803){
                        weatherIcons[i].setImageResource(R.drawable.weather_partlycloudy);
                    }
                    else if (weatherID == 804){
                        weatherIcons[i].setImageResource(R.drawable.weather_cloudy);
                    }
                }
            }
        }

    }

    private void setDaysOfWeek(){
        for (int i = 0; i < 5; i++){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(daysOfWeek[i]);

            int date_num = c.get(Calendar.DAY_OF_MONTH);
            int day = c.get(Calendar.DAY_OF_WEEK);

            String dow = getDay(day);
            String date = String.valueOf(date_num);

            daysTV[i].setText(dow);
            datesTV[i].setText(date);
        }
    }

    private String getDay(int day){
        String[] dayOfWeek = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };
        return dayOfWeek[day - 1];
    }

    private void initViews(View view){

        int[] dows = { R.id.dow1, R.id.dow2, R.id.dow3, R.id.dow4, R.id.dow5 };
        int[] dates = { R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5 };
        int[] icons = { R.id.weather_icon_1, R.id.weather_icon_2, R.id.weather_icon_3, R.id.weather_icon_4, R.id.weather_icon_5};

        if (view != null){
            for(int i = 0; i < 5; i++){
                daysTV[i] = (TextView) view.findViewById(dows[i]);
                datesTV[i] = (TextView) view.findViewById(dates[i]);
                weatherIcons[i] = (ImageView) view.findViewById(icons[i]);
            }
        }
    }

    private void initDays(){
        daysOfWeek = new long[5];
        for (int i = 0; i < 5; i++){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date);
            cal.add(Calendar.DAY_OF_MONTH, i);
            daysOfWeek[i] = cal.getTimeInMillis();
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double latitude = 0;
            double longitude = 0;

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            String locale = "lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude);

            Toast.makeText(getActivity(), "Run Started", Toast.LENGTH_SHORT).show();

            JSONWeatherTask weatherTask = new JSONWeatherTask();

            weatherTask.execute(new String[]{locale});
            handler.postDelayed(runnable, THREE_HOURS);

        }
    };

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather[]> {

        @Override
        protected Weather[] doInBackground(String... params) {
            Weather[] forecast = new Weather[5];
            String data = new WeatherHTTPClient().getWeatherData(params[0]);
            JSONWeatherParser parser = new JSONWeatherParser();

            if (data != null){
                //System.out.println(data);

                for (int i = 0; i < 5; i++){
                    try {
                        Weather weather = parser.getWeather(data, i);
                        forecast[i] = weather;

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return forecast;
        }

        @Override
        protected void onPostExecute(Weather[] forecast) {
            super.onPostExecute(forecast);
            mForecast = forecast;

            setWeatherIcons();
        }


    }
}
