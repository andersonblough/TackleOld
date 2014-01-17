package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tackle.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bill on 1/16/14.
 */
public class DateTimePicker extends LinearLayout {
    private TextView fromTo, month, day, year, time;
    private Calendar date;
    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.datetime_picker_layout, this, true);
        date = Calendar.getInstance();
        initTextViews();


    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFromTo(String text){
        fromTo.setText(text);
    }

    private void initTextViews(){
        fromTo = (TextView) findViewById(R.id.from_to);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        year = (TextView) findViewById(R.id.year);
        time = (TextView) findViewById(R.id.time);
    }

    public void setDate(long dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma");

        date.setTimeInMillis(dateTime);
        // todo: fix the locale
        month.setText(date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US));
        day.setText(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
        year.setText(String.valueOf(date.get(Calendar.YEAR)));
        time.setText(dateFormat.format(date.getTime()));
    }


}
