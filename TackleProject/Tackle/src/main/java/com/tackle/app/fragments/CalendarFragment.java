package com.tackle.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.R;
import com.tackle.app.adapters.CalendarAdapter;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bill on 11/12/13.
 */
public class CalendarFragment extends Fragment {

    protected Calendar calendar;
    private ImageView monthImage;
    private TextView monthTextView;
    private GridView gridView;


    public CalendarFragment(Calendar cal){
        calendar = cal;
    }

    public static CalendarFragment newInstance(Calendar cal){
        return new CalendarFragment(cal);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        monthImage = (ImageView) view.findViewById(R.id.month_image);
        monthTextView = (TextView) view.findViewById(R.id.month_text);
        gridView = (GridView) view.findViewById(R.id.calendar_grid);


        updateUI();

        return view;
    }

    public void updateUI(){
        TypedArray monthImages = getResources().obtainTypedArray(R.array.months);
        int month = calendar.get(Calendar.MONTH);

        monthImage.setImageDrawable(monthImages.getDrawable(month));

        String monthText = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = calendar.get(Calendar.YEAR);
        String yearText = String.valueOf(year);

        monthTextView.setText(monthText + " " + yearText);

        final CalendarAdapter adapter = new CalendarAdapter(getActivity(), calendar);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(), String.valueOf((position + 2) - adapter.dayOfWeek), Toast.LENGTH_SHORT).show();
                int day  = (position + 2) - adapter.dayOfWeek;
                Calendar cal = (Calendar) calendar.clone();
                cal.set(Calendar.DAY_OF_MONTH, day);
                long time = cal.getTimeInMillis();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", time);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
                getActivity().overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
            }
        });

    }

    public void onPreviousMonth(){
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY){
            calendar.set(calendar.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 1);
        }
        else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
    }

    public void onNextMonth(){
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            calendar.set(calendar.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
        }
        else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }

    }

    public void setMonth(Calendar cal){

        calendar = cal;
        updateUI();

    }


}
