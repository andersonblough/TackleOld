package com.tackle.app.fragments.EditFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.R;
import com.tackle.app.views.RepeatPicker;

import java.util.Date;

/**
 * Created by Bill on 1/16/14.
 */
public class RemindersFragment extends Fragment implements RepeatPicker.RepeatSpinnerListener {
    RepeatPicker repeatPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_reminders, container, false);
        repeatPicker = (RepeatPicker) view.findViewById(R.id.repeatPicker);
        repeatPicker.setSpinnerListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSpinnerItemSelected(int position) {
        //Toast.makeText(getActivity(), "Spinner Position " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        DialogFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.show(getFragmentManager(), "DatePicker");
    }
}
