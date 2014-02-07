package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Bill on 1/29/14.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String STARTTIME = "start";
    public static final String ENDTIME = "end";

    private long dateTime;
    private TimeChangeListener mListener;
    int count;
    Calendar c;

    public TimePickerFragment(){
        super();
    }

    public TimePickerFragment(long dateTime){
        super();
        this.dateTime = dateTime;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TimeChangeListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement TimeChangeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        count = 1;
        c = Calendar.getInstance();
        c.setTimeInMillis(dateTime);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (count == 1){
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);

                    mListener.onTimeChanged(c.getTimeInMillis(), getTag());
                    count++;
                }

            }
        };

        TimePickerDialog dialog =  new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, hourOfDay, minute, false);

        return dialog;
    }

    public interface TimeChangeListener{
        public void onTimeChanged(long dateTime, String tag);
    }
}
