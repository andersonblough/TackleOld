package com.tackle.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tackle.app.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bill on 11/6/13.
 */
public class DayHeaderFragment extends Fragment {

    private long date;
    private TextView tv;

    public DayHeaderFragment(){
        super();
    }

    public DayHeaderFragment(long date){
        super();
        this.date = date;
    }

    public void setDate(long dateTime){
        this.date = dateTime;
        initTextView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_layout, container, false);

        tv = (TextView) view.findViewById(R.id.day_title);
        initTextView();

        return view;
    }

    private void initTextView(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        int date = cal.get(Calendar.DAY_OF_MONTH);
        tv.setText(dayOfWeek + ", " + date);
    }
}
