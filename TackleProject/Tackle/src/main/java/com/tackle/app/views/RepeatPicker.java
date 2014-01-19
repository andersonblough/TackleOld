package com.tackle.app.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tackle.app.R;
import com.tackle.app.adapters.RepeatSpinnerAdapter;

import java.util.Date;

/**
 * Created by Bill on 1/17/14.
 */
public class RepeatPicker extends LinearLayout{

    private static final int SINGLE_CHOICE = 100;
    private static final int MULTIPLE_CHOICE = 200;

    private Context context;

    private RepeatSpinnerListener mSpinnerListener;
    private GridView repeatDays;
    private FrameLayout daysFrame;
    private UntilSpinner untilSpinner;

    private Boolean[] selectedDays;
    private Boolean[] selectedTypes;

    public RepeatSpinnerAdapter spinnerAdapter;
    Adapter typeAdapter;
    Adapter daysAdapter;


    public RepeatPicker(Context context) {
        this(context, null);
    }

    public RepeatPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        String[] titles = {"Daily", "Weekly", "Monthly", "Yearly"};
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.repeat_picker, this, true);

        untilSpinner = (UntilSpinner) findViewById(R.id.untilSpinner);
        spinnerAdapter = new RepeatSpinnerAdapter(getContext());
        untilSpinner.setAdapter(spinnerAdapter);
        untilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean userClicked = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userClicked) {
                    if (position == 1) {
                        mSpinnerListener.onSpinnerItemSelected(position);
                    }
                }
                userClicked = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        repeatDays = (GridView) findViewById(R.id.repeatDays);
        repeatDays.setNumColumns(days.length);
        daysFrame = (FrameLayout) findViewById(R.id.daysFrame);

        GridView repeatTypes = (GridView) findViewById(R.id.repeatTypes);
        repeatTypes.setNumColumns(titles.length);
        typeAdapter = new Adapter(titles, SINGLE_CHOICE);
        repeatTypes.setAdapter(typeAdapter);
        repeatTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeAdapter.setSelectedView(i);
                if (i == 1 && typeAdapter.selected[i]){
                    repeatDays.setVisibility(View.VISIBLE);
                    daysFrame.setVisibility(View.VISIBLE);
                }
                else {
                    repeatDays.setVisibility(View.GONE);
                    daysFrame.setVisibility(View.GONE);
                }
                if (typeAdapter.selected[i]){
                    untilSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    untilSpinner.setVisibility(View.GONE);
                }
                selectedTypes = typeAdapter.selected;
            }
        });
        daysAdapter = new Adapter(days, MULTIPLE_CHOICE);
        repeatDays.setAdapter(daysAdapter);
        repeatDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                daysAdapter.setSelectedView(i);
                selectedDays = typeAdapter.selected;
            }
        });

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        return super.onSaveInstanceState();
    }

    public Boolean[] getSelectedDays(){
        return selectedDays;
    }

    public void setSelectedDays(Boolean[] selected){
        selectedDays = selected;
        daysAdapter.selected = selectedDays;
        daysAdapter.notifyDataSetChanged();
    }

    public interface RepeatSpinnerListener{
        public void onSpinnerItemSelected(int position);
    }

    public void setSpinnerListener(RepeatSpinnerListener listener){
        mSpinnerListener = listener;
    }



    private class Adapter extends BaseAdapter{
        private int choiceMode;
        private String[] items;
        public Boolean[] selected;

        public Adapter(String[] items, int choiceMode){
            super();
            this.items = items;
            this.choiceMode = choiceMode;
            selected = new Boolean[items.length];
            for (int i = 0; i < items.length; i++){
                selected[i] = false;
            }

        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        public void toggleSelected(int position){
            if (selected[position]){
                selected[position] = false;
            }
            else {
                selected[position] = true;
            }
        }

        public void setSelectedView(int position){
            if (choiceMode == SINGLE_CHOICE){
                for (int i = 0; i < selected.length; i++){
                    if (i == position){
                        toggleSelected(i);
                    }
                    else {
                        selected[i] = false;
                    }
                }
            }
            else {
                toggleSelected(position);

            }
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            TextView view = (TextView) convertView;
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (TextView) inflater.inflate(R.layout.picker_text, viewGroup, false);
            }
            view.setText(items[position]);
            view.setBackgroundResource(R.color.lighter);

            if (selected[position]){
                view.setBackgroundResource(R.color.medium_light);
            }

            return view;
        }
    }
}
