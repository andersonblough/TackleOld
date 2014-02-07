package com.tackle.app.fragments.EditFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tackle.app.Dialogs.DatePickerFragment;
import com.tackle.app.Dialogs.NumberPickerFragment;
import com.tackle.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bill on 1/16/14.
 */
public class RemindersFragment extends Fragment {
    long untilDate;
    int choice;
    int weekVisibility;
    int untilVisibility;
    boolean[] daysChosen;
    int untilPos;
    int untilCount;

    private GridView freqGrid;
    private GridView weekGrid;
    private GridView untilGrid;

    private UntilAdapter untilAdapter;

    RelativeLayout weekLayout;
    RelativeLayout untilLayout;

    public RemindersFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setRetainInstance(true);

        //set up values
        if (savedState != null){
            // restore from state
            choice = savedState.getInt("choice");
            weekVisibility = savedState.getInt("weekVisible");
            daysChosen = savedState.getBooleanArray("daysChosen");
            untilPos = savedState.getInt("untilPos");
            untilVisibility = savedState.getInt("untilVisible");
            untilDate = savedState.getLong("untilDate");
            untilCount = savedState.getInt("untilCount");

        }
        else {
            // first time, set up everything
            untilDate = System.currentTimeMillis();
            choice = -1;
            weekVisibility = View.GONE;
            untilVisibility = View.GONE;
            daysChosen = new boolean[]{false, false, false, false, false, false, false};
            untilCount = 1;

        }

        //create until adapter
        untilAdapter = new UntilAdapter();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_fragment_reminders, container, false);

        //Only things to do with the view

        //set up the 3 Grid Views
        freqGrid = (GridView) view.findViewById(R.id.freq);
        weekGrid = (GridView) view.findViewById(R.id.weekGrid);
        untilGrid = (GridView) view.findViewById(R.id.untilGrid);

        //set up the 2 layouts
        weekLayout = (RelativeLayout) view.findViewById(R.id.weekDays);
        untilLayout = (RelativeLayout) view.findViewById(R.id.until);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //set Visibility of week and until layouts
        weekLayout.setVisibility(weekVisibility);
        untilLayout.setVisibility(untilVisibility);

        //set adapters for grid views
        ArrayAdapter freqAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.frequencies, R.layout.picker_text);
        freqGrid.setAdapter(freqAdapter);

        String[] week = {"S", "M", "T", "W", "T", "F", "S"};
        ArrayAdapter weekAdapter = new ArrayAdapter(getActivity(), R.layout.picker_text, week);
        weekGrid.setAdapter(weekAdapter);

        untilGrid.setAdapter(untilAdapter);

        //set up grid views with values
        if (choice != -1){
            freqGrid.setItemChecked(choice, true);
        }

        for (int i = 0; i < daysChosen.length; i++){
            weekGrid.setItemChecked(i, daysChosen[i]);
        }

        switch (untilPos){
            case 0:
                untilGrid.setItemChecked(untilPos, true);
                break;
            case 1:
                untilGrid.setItemChecked(untilPos, true);
                Date date = new Date(untilDate);
                DateFormat formater = new SimpleDateFormat("M/dd/yyyy");
                untilAdapter.setUntil(formater.format(date));
                break;
            case 2:
                untilGrid.setItemChecked(untilPos, true);
                if (untilCount == 1){
                    untilAdapter.setCount("Once");
                }
                else {
                    untilAdapter.setCount(String.valueOf(untilCount) + " times");
                }

        }

        //set up ItemClickListeners for the grid views
        freqGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == choice) {
                    freqGrid.setItemChecked(i, false);
                    choice = -1;
                    untilVisibility = View.GONE;
                } else {
                    freqGrid.setItemChecked(i, true);
                    choice = i;
                    untilVisibility = View.VISIBLE;

                }
                if (choice == 1) {
                    weekVisibility = View.VISIBLE;
                }
                else {
                    weekVisibility = View.GONE;
                }
                weekLayout.setVisibility(weekVisibility);
                untilLayout.setVisibility(untilVisibility);
            }
        });

        weekGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (daysChosen[position]){
                    daysChosen[position] = false;
                }
                else {
                    daysChosen[position] = true;
                }
            }
        });

        untilGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                untilPos = position;
                switch (position){
                    case 0:
                        untilAdapter.setUntil("Until");
                        untilDate = System.currentTimeMillis();
                        break;
                    case 1:
                        DatePickerFragment dialog = new DatePickerFragment(untilDate);
                        dialog.show(getFragmentManager(), DatePickerFragment.UNTILDATE);
                        break;
                    case 2:
                        NumberPickerFragment numPicker = new NumberPickerFragment();
                        numPicker.show(getFragmentManager(), "numPicker");
                        break;
                }

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choice", choice);
        outState.putInt("weekVisible", weekVisibility);
        outState.putBooleanArray("daysChosen", daysChosen);
        outState.putInt("untilPos", untilPos);
        outState.putInt("untilVisible", untilVisibility);
        outState.putLong("untilDate", untilDate);
        outState.putInt("untilCount", untilCount);
    }

    public void setUntilDate(long dateTime){
        untilDate = dateTime;
        Date date = new Date(untilDate);
        DateFormat formater = new SimpleDateFormat("M/dd/yyyy");
        untilAdapter.setUntil(formater.format(date));
    }

    public void setCountValue(int count){
        untilCount = count;
        if (untilCount == 1){
            untilAdapter.setCount("Once");
        }
        else {
            untilAdapter.setCount(String.valueOf(untilCount) + " times");
        }

    }

    private class UntilAdapter extends BaseAdapter{
        String until = "Until";
        String count = "Count";

        public void setUntil(String until){
            this.until = until;
            count = "Count";
            notifyDataSetChanged();
        }

        public void setCount(String count){
            this.count = count;
            until = "Until";
            untilDate = System.currentTimeMillis();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            TextView view = (TextView) convertView;
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (TextView) inflater.inflate(R.layout.picker_text, viewGroup, false);
            }

            switch (position){
                case 0:
                    view.setText("Forever");
                    break;
                case 1:
                    view.setText(until);
                    break;
                case 2:
                    view.setText(count);
                    break;
            }
            return view;
        }
    }


}
