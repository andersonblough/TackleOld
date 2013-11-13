package com.tackle.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tackle.app.R;

import java.util.Calendar;

/**
 * Created by Bill on 11/12/13.
 */
public class CalendarAdapter extends BaseAdapter {
    private Calendar mCalendar;
    private Context context;
    public int dayOfWeek;
    int daysInMonth;
    int endOfCells;
    int currentDay;

    public CalendarAdapter(Context context, Calendar calendar){
        super();
        mCalendar = calendar;
        this.context = context;
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        daysInMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        endOfCells = daysInMonth + dayOfWeek;

        currentDay = getCurrentDay();
    }
    @Override
    public int getCount() {
        return endOfCells - 1;
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
    public View getView(int i, View view, ViewGroup viewGroup) {



        ViewHolder holder;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cell_day, viewGroup, false);
            holder = new ViewHolder();
            holder.day = (TextView) view.findViewById(R.id.day);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        if (i < (dayOfWeek - 1)){
            view.setClickable(false);
            view.setVisibility(View.INVISIBLE);
        } else if (i < endOfCells){
            holder.day.setText(String.valueOf((i + 2) - dayOfWeek));
        }
        if (currentDay != 0 && i == (currentDay + dayOfWeek - 2)){
            holder.day.setTextColor(context.getResources().getColor(R.color.Tackle_Green));
            holder.day.setTextSize(20);
            holder.day.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        }

        return view;
    }

    private int getCurrentDay(){
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == mCalendar.get(Calendar.YEAR) && cal.get(Calendar.MONTH) == mCalendar.get(Calendar.MONTH)){
            return cal.get(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    static class ViewHolder{
        TextView day;
    }
}
