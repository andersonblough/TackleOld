package com.tackle.app.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tackle.app.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Bill on 1/18/14.
 */
public class RepeatSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private Date mDate;

    public void setDate(Date date){
        mDate = date;
        notifyDataSetChanged();
    }

    public RepeatSpinnerAdapter(Context context){
        super();
        mContext = context;
        mDate = new Date(System.currentTimeMillis());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView view = (TextView) convertView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (TextView) inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        switch (position){
            case 0:
                view.setText("Forever");
                break;
            case 1:
                SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy");
                view.setText("Until " + dateFormat.format(mDate));
                break;
            case 2:
                view.setText("12 Occurrences");
                break;
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String[] options = mContext.getResources().getStringArray(R.array.until);
        CheckedTextView view = (CheckedTextView) convertView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (CheckedTextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        view.setText(options[position]);
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
