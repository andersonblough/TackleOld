package com.tackle.app.fragments.EditFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tackle.app.EditActivity;
import com.tackle.app.R;
import com.tackle.app.views.DateTimePicker;

/**
 * Created by Bill on 1/15/14.
 */
public class DateTimeFragment extends Fragment {

    private long mStartTime;
    private long mEndTime;
    private int allDay;

    DateTimePicker startDate;

    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_datetime, container, false);

        //

        DateTimePicker timePicker = (DateTimePicker) view.findViewById(R.id.enddate);

        timePicker.setFromTo("To");
        timePicker.setDate(System.currentTimeMillis());
        return view;
    }
}
