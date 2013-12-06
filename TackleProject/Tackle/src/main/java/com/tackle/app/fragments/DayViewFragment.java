package com.tackle.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tackle.app.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bill on 12/5/13.
 */
public class DayViewFragment extends Fragment {

    private long mDateTime;
    private TextView mDayText;
    private ImageView mWeatherIcon;

    private int[] mWeather;

    /**
     * public empty constructor
     */
    public DayViewFragment(){
        super();
        // default dateTime is current system time
        mDateTime = System.currentTimeMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_layout, container, false);

        //set up TextView and ImageView
        mDayText = (TextView) view.findViewById(R.id.day_title);
        mWeatherIcon = (ImageView) view.findViewById(R.id.weather_icon);

        //fill TextView and ImageView with content
        fillTextView();
        fillImageView();

        return view;
    }

    /**
     * private method to fill text view with current day text
     */
    private void fillTextView(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDateTime);
        // TODO: edit the local with system locale
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        mDayText.setText(dayOfWeek + ", " + day);
    }

    /**
     * private method to fill image view with updated weather icon
     */
    private void fillImageView(){
        mWeatherIcon.setImageResource(0);

        for (int i = 0; i < 5; i++){
            Calendar curWeek = Calendar.getInstance();
            curWeek.add(Calendar.DAY_OF_MONTH, i);

            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(mDateTime);

            if (curWeek.get(Calendar.YEAR) == today.get(Calendar.YEAR) && curWeek.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)){
                if (mWeather != null){
                    mWeatherIcon.setImageResource(mWeather[i]);
                }
            }
        }
    }

    /**
     * Method to set the date for the Day and update the UI
     */
    public void setDate(long dateTime){
        mDateTime = dateTime;
        fillTextView();
        fillImageView();
    }

    /**
     * Method to set the weather data
     */
    public void setWeather(int[] weatherData){
        mWeather = weatherData;
        fillImageView();
    }
}
