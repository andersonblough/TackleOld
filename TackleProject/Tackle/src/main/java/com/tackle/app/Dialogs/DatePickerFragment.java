package com.tackle.app.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Bill on 1/18/14.
 */
public class DatePickerFragment extends DialogFragment {

    UntilDateListener mListener;
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
            mListener = (UntilDateListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement UntilDateListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        count = 1;

        c.setTimeInMillis(dateTime);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (count == 1){
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day);

                    //Toast.makeText(getActivity(), String.valueOf(c.getTimeInMillis()), Toast.LENGTH_SHORT).show();
                    mListener.onUntilDateChanged(c.getTimeInMillis());
                    count++;
                }

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, year, month, day);

        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public interface UntilDateListener{
        public void onUntilDateChanged(long dateTime);
    }
}
