package com.tackle.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.tackle.app.views.MonthAndYearPicker;

/**
 * Created by Bill on 12/6/13.
 */
public class PickerDialog extends DialogFragment {
    private long dateTime;
    private PickerDateChangeListener mListener;

    public PickerDialog(long dateTime){
        super();
        this.dateTime = dateTime;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PickerDateChangeListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement PickerDateChangeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        final MonthAndYearPicker picker = new MonthAndYearPicker(getActivity(), dateTime);
        builder.setView(picker)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPickerDateChanged(picker.getDateTime());


                    }
                });
        return builder.create();

    }

    public interface PickerDateChangeListener{
        public void onPickerDateChanged(long dateTime);
    }
}
