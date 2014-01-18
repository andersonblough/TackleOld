package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tackle.app.R;

/**
 * Created by Bill on 1/17/14.
 */
public class ReapeatPicker extends LinearLayout{

    private static final int SINGLE_CHOICE = 100;
    private static final int MULTIPLE_CHOICE = 200;

    private Context context;

    GridView repeatDays;
    FrameLayout daysFrame;
    Spinner untilSpinner;


    public ReapeatPicker(Context context) {
        this(context, null);
    }

    public ReapeatPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        String[] titles = {"Weekly", "Monthly", "Yearly"};
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.repeat_picker, this, true);

        untilSpinner = (Spinner) findViewById(R.id.untilSpinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.until, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        untilSpinner.setAdapter(spinnerAdapter);
        untilSpinner.setSelection(0);

        repeatDays = (GridView) findViewById(R.id.repeatDays);
        daysFrame = (FrameLayout) findViewById(R.id.daysFrame);

        GridView repeatTypes = (GridView) findViewById(R.id.repeatTypes);
        final Adapter adapter = new Adapter(titles, SINGLE_CHOICE);
        repeatTypes.setAdapter(adapter);
        repeatTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedView(i);
                if (i == 0 && adapter.selected[i]){
                    repeatDays.setVisibility(View.VISIBLE);
                    daysFrame.setVisibility(View.VISIBLE);
                }
                else {
                    repeatDays.setVisibility(View.GONE);
                    daysFrame.setVisibility(View.GONE);
                }
                if (adapter.selected[i]){
                    untilSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    untilSpinner.setVisibility(View.GONE);
                }
            }
        });
        final Adapter daysAdapter = new Adapter(days, MULTIPLE_CHOICE);
        repeatDays.setAdapter(daysAdapter);
        repeatDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                daysAdapter.setSelectedView(i);
            }
        });

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
