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
public class WeekViewFragment extends Fragment{
    private long mDateTime;

    private long[] mWeek;
    private int[] mWeather;

    private TextView[] daysTextView;
    private TextView[] datesTextView;
    private ImageView[] weatherIcons;

    private int start;

    /**
     * empty constructor
     */
    public WeekViewFragment(){
        this(System.currentTimeMillis());
    }

    /**
     * constructor taking a dateTime as an argument
     */
    public WeekViewFragment(long dateTime){
        super();
        mDateTime = dateTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datesTextView = new TextView[5];
        daysTextView = new TextView[5];
        weatherIcons = new ImageView[5];

        initWeek();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_dateheader, container, false);

        initViews(view);
        fillViews();
        return view;
    }

    /**
     * private method to initialize the Views for the Fragment class
     */
    private void initViews(View view){
        int[] days = { R.id.dow1, R.id.dow2, R.id.dow3, R.id.dow4, R.id.dow5 };
        int[] dates = { R.id.date1, R.id.date2, R.id.date3, R.id.date4, R.id.date5 };
        int[] icons = { R.id.weather_icon_1, R.id.weather_icon_2, R.id.weather_icon_3, R.id.weather_icon_4, R.id.weather_icon_5};

        if (view != null){
            for (int i = 0; i < 5; i++){
                daysTextView[i] = (TextView) view.findViewById(days[i]);
                datesTextView[i] = (TextView) view.findViewById(dates[i]);
                weatherIcons[i] = (ImageView) view.findViewById(icons[i]);
            }
        }
    }

    /**
     * private method to fill views with data
     */
    private void fillViews(){
        String[] shortNames = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
        for (int i = 0; i < 5; i++){
            if (dayHasWeather(mWeek[i])){
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(mWeek[i]);

                String day = shortNames[cal.get(Calendar.DAY_OF_WEEK) -1];
                int date = cal.get(Calendar.DAY_OF_MONTH);

                daysTextView[i].setText(day);
                datesTextView[i].setText(String.valueOf(date));
                if (mWeather != null){
                    weatherIcons[i].setImageResource(mWeather[start]);
                }

            }
            else {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(mWeek[i]);

                String day = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
                int date = cal.get(Calendar.DAY_OF_MONTH);

                daysTextView[i].setText(day);
                datesTextView[i].setText(String.valueOf(date));
                weatherIcons[i].setImageResource(0);

            }


        }

    }

    private boolean dayHasWeather(long l) {
        boolean result = false;
        start = 0;
        for (int i = 0; i < 5; i++){
            Calendar curWeek = Calendar.getInstance();
            curWeek.add(Calendar.DAY_OF_MONTH, i);
            Calendar week = Calendar.getInstance();
            week.setTimeInMillis(l);

            if (curWeek.get(Calendar.YEAR) == week.get(Calendar.YEAR) && curWeek.get(Calendar.DAY_OF_YEAR) == week.get(Calendar.DAY_OF_YEAR)){
                start = i;
                result = true;
            }
        }
        return result;
    }

    /**
     *public method to set the dateTime of the WeekViewFragment
     */
    public void setDateTime(long dateTime){
        mDateTime = dateTime;
        initWeek();
        fillViews();
    }

    /**
     * public method to set weather and update the UI
     */
    public void setWeather(int[] weatherData){
        mWeather = weatherData;
        fillViews();

    }

    /**
     * initialize the Week array with long values for each date
     */
    private void initWeek(){
        if (mWeek == null){
            mWeek =  new long[5];
        }
        for (int i = 0; i < 5; i++){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(mDateTime);
            cal.add(Calendar.DAY_OF_MONTH, i);
            mWeek[i] = cal.getTimeInMillis();
        }

    }

    public long getWeekDay(int position){
        return mWeek[position];
    }


}