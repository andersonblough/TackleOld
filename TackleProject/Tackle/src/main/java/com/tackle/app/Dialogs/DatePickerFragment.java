package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Bill on 1/18/14.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String STARTDATE = "start";
    public static final String ENDDATE = "end";
    public static final String UNTILDATE = "until";

    private Calendar c;

    DateChangeListener mListener;
    long dateTime;
    int count;

    public DatePickerFragment(){
        super();
    }

    public DatePickerFragment(long dateTime){
        super();
        this.dateTime = dateTime;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DateChangeListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement DateChangeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        count = 1;

        c = Calendar.getInstance();
        c.setTimeInMillis(dateTime);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (count == 1){
                    c.set(year, month, day);

                    mListener.onDateChanged(c.getTimeInMillis(), getTag());
                    count++;
                }

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, year, month, day);

        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public interface DateChangeListener {
        public void onDateChanged(long dateTime, String tag);
    }
}
