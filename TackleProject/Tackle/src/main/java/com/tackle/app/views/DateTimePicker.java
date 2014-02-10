package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    private LinearLayout dateLayout, timeLayout;
    private Calendar date;
    private OnClickListener mListener;

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.datetime_picker_layout, this, true);
        date = Calendar.getInstance();
        initTextViews();
        dateLayout = (LinearLayout) findViewById(R.id.datelayout);
        timeLayout = (LinearLayout) findViewById(R.id.timelayout);

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onDateClicked();
                }
            }
        });
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onTimeClicked();
                }
            }
        });


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

    public void setAllDay(){
        time.setText("All Day");
    }

    public void setDate(long dateTime){
        date.setTimeInMillis(dateTime);
        // todo: fix the locale
        month.setText(date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US));
        day.setText(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
        year.setText(String.valueOf(date.get(Calendar.YEAR)));

    }

    public void setTime(long dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma");
        date.setTimeInMillis(dateTime);
        time.setText(dateFormat.format(date.getTime()));
    }

    public void hideLabel(){
        fromTo.setVisibility(View.GONE);
    }

    public void setOnClickListener(OnClickListener listener){
        mListener = listener;
    }

    public interface OnClickListener{
        public void onDateClicked();

        public void onTimeClicked();
    }

}
