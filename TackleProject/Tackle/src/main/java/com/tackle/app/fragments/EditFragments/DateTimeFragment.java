package com.tackle.app.fragments.EditFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.Dialogs.TimePickerFragment;
import com.tackle.app.EditActivity;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;
import com.tackle.app.data.TackleEvent;
import com.tackle.app.views.DateTimePicker;

/**
 * Created by Bill on 1/15/14.
 */
public class DateTimeFragment extends Fragment {

    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";
    public static final String TYPE = "type";
    public static final String ALL_DAY = "allday";

    private long mStartTime;
    private long mEndTime;
    private int allDay;

    private Cursor mCursor;

    DateTimePicker startDate;
    DateTimePicker endDate;
    CheckBox allDayBox;

    private int type;

    public DateTimeFragment(Cursor cursor){
        super();
        mCursor = cursor;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up values
        if (savedInstanceState != null){
            // set values from saved state
            mStartTime = savedInstanceState.getLong(START_DATE);
            mEndTime = savedInstanceState.getLong(END_DATE);
            allDay = savedInstanceState.getInt(ALL_DAY);
            type = savedInstanceState.getInt(TYPE);
        }
        else {
            // set up values from data
            mCursor.moveToFirst();
            type = mCursor.getInt(mCursor.getColumnIndex(TackleContract.TackleEvent.TYPE));
            mStartTime = mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent.START_DATE));
            mEndTime = mCursor.getLong(mCursor.getColumnIndex(TackleContract.TackleEvent.END_DATE));
            allDay = mCursor.getInt(mCursor.getColumnIndex(TackleContract.TackleEvent.ALL_DAY));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_datetime, container, false);

        //set up UI elements
        startDate = (DateTimePicker) view.findViewById(R.id.startdate);
        endDate = (DateTimePicker) view.findViewById(R.id.enddate);
        allDayBox = (CheckBox) view.findViewById(R.id.all_day);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //init values for UI elements
        startDate.setDate(mStartTime);
        startDate.setTime(mStartTime);
        if (type == TackleEvent.Type.EVENT){
            startDate.setFromTo("From");
            endDate.setFromTo("To");
            endDate.setDate(mEndTime);
            endDate.setTime(mEndTime);

            //set up visibility of enddate based on if its an event or not
            endDate.setVisibility(View.VISIBLE);
        }

        if (allDay == 1){
            allDayBox.setChecked(true);
        }
        else {
            allDayBox.setChecked(false);
        }

        //set click listeners
        startDate.setOnClickListener(new DateTimePicker.OnClickListener() {
            @Override
            public void onDateClicked() {
                DatePickerFragment dateDialog = new DatePickerFragment(mStartTime);
                dateDialog.show(getFragmentManager(), DatePickerFragment.STARTDATE);
            }

            @Override
            public void onTimeClicked() {
                TimePickerFragment timeDialog = new TimePickerFragment(mStartTime);
                timeDialog.show(getFragmentManager(), TimePickerFragment.STARTTIME);
            }
        });

        endDate.setOnClickListener(new DateTimePicker.OnClickListener() {
            @Override
            public void onDateClicked() {
                DatePickerFragment dateDialog = new DatePickerFragment(mEndTime);
                dateDialog.show(getFragmentManager(), DatePickerFragment.ENDDATE);
            }

            @Override
            public void onTimeClicked() {
                TimePickerFragment timeDialog = new TimePickerFragment(mEndTime);
                timeDialog.show(getFragmentManager(), TimePickerFragment.ENDTIME);
            }
        });

        allDayBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    allDay = 1;
                    startDate.setAllDay();
                    endDate.setAllDay();
                }
                else {
                    allDay = 0;
                    startDate.setTime(mStartTime);
                    endDate.setTime(mEndTime);
                }
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save values
        outState.putLong(START_DATE, mStartTime);
        outState.putLong(END_DATE, mEndTime);
        outState.putInt(ALL_DAY, allDay);
        outState.putInt(TYPE, type);
    }

    public void setStartDate(long dateTime){
        mStartTime = dateTime;
        startDate.setDate(mStartTime);
        // check that your startdate is before enddate, otherwise move enddate
        if (mStartTime > mEndTime){
            mEndTime = mStartTime + (60 * 60 * 1000);
            endDate.setDate(mEndTime);
            if (allDay != 1){
                endDate.setTime(mEndTime);
                startDate.setTime(mStartTime);
            }
        }
        startDate.setDate(mStartTime);


    }

    public void setEndDate(long dateTime){
        mEndTime = dateTime;
        endDate.setDate(mEndTime);
        // check that the endtime is after startdate, otherwise set it to an hour later
        if (mEndTime < mStartTime){
            mEndTime = mStartTime + (60 * 60 * 1000);
            if (allDay != 1){
                endDate.setTime(mEndTime);
            }
        }
        endDate.setDate(mEndTime);



    }

    public void setStartTime(long dateTime){
        mStartTime = dateTime;
        startDate.setTime(mStartTime);
        if (mStartTime > mEndTime){
            mEndTime = mStartTime + (60 * 60 * 1000);
            endDate.setTime(mEndTime);
            endDate.setDate(mEndTime);
        }
        startDate.setTime(mStartTime);
        allDayBox.setChecked(false);

    }

    public void setEndTime(long dateTime){
        mEndTime = dateTime;
        endDate.setTime(mEndTime);
        if (mEndTime < mStartTime){
            mEndTime = mStartTime + (60 * 60 * 1000);
        }
        endDate.setDate(mEndTime);
        endDate.setTime(mEndTime);
        allDayBox.setChecked(false);

    }
}
