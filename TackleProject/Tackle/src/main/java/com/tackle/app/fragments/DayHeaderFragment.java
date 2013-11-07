package com.tackle.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tackle.app.R;

/**
 * Created by Bill on 11/6/13.
 */
public class DayHeaderFragment extends Fragment {

    private String day;

    public DayHeaderFragment(){
        super();
    }

    public DayHeaderFragment(String day){
        super();
        this.day = day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_layout, container, false);

        TextView tv = (TextView) view.findViewById(R.id.day_title);
        tv.setText(day);

        return view;
    }
}
