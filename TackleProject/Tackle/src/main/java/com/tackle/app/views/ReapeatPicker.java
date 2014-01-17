package com.tackle.app.views;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tackle.app.R;

import java.util.ArrayList;

/**
 * Created by Bill on 1/17/14.
 */
public class ReapeatPicker extends LinearLayout{

    private Context context;

    GridView repeatDays;


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

        repeatDays = (GridView) findViewById(R.id.repeatDays);
        repeatDays.setVisibility(INVISIBLE);

        GridView repeatTypes = (GridView) findViewById(R.id.repeatTypes);
        final Adapter adapter = new Adapter(titles);
        repeatTypes.setAdapter(adapter);
        repeatTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedView(i);
                if (i == 0 && adapter.selectedPosition == 0){
                    repeatDays.setVisibility(VISIBLE);
                }
                else {
                    repeatDays.setVisibility(INVISIBLE);
                }
            }
        });


        final Adapter daysAdapter = new Adapter(days);
        repeatDays.setAdapter(daysAdapter);
        repeatDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                daysAdapter.setSelections(i);
            }
        });

    }

    private class Adapter extends BaseAdapter{
        private String[] items;
        private Boolean[] selections;
        public int selectedPosition = -1;

        public Adapter(String[] items){
            super();
            this.items = items;
            selections = new Boolean[]{false, false, false, false, false, false, false};
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        public void setSelections(int position){
            if (!selections[position]){
                selections[position] = true;
            }
            else {
                selections[position] = false;
            }
            notifyDataSetChanged();
        }

        public void setSelectedView(int position){
            if (selectedPosition == position){
                selectedPosition = -1;
            }
            else {
                selectedPosition = position;
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

            if (position == selectedPosition || selections[position]){
                view.setBackgroundResource(R.color.medium_light);
            }

            return view;
        }
    }
}
