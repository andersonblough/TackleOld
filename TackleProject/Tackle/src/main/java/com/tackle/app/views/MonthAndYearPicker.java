package com.tackle.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.tackle.app.R;

import java.util.Calendar;

/**
 * Created by Bill on 12/6/13.
 */
public class MonthAndYearPicker extends LinearLayout implements ViewSwitcher.ViewFactory, View.OnClickListener {
    private Context mContext;

    private TextSwitcher mSwitcher;
    private GridView mMonthGrid;
    private ImageView previous, next;
    private long dateTime;
    private Calendar calendar;

    public MonthAndYearPicker(Context context, long dateTime) {
        this(context, null, dateTime);
    }

    public MonthAndYearPicker(Context context, AttributeSet attrs, long dateTime) {
        super(context, attrs);
        mContext = context;
        this.dateTime = dateTime;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_picker_layout, this, true);

        previous = (ImageView) findViewById(R.id.previous_month);
        next = (ImageView) findViewById(R.id.next_month);

        previous.setOnClickListener(this);
        next.setOnClickListener(this);

        mSwitcher = (TextSwitcher) findViewById(R.id.text_switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        mMonthGrid = (GridView) findViewById(R.id.month_grid);
        final MonthGridAdapter mAdapter = new MonthGridAdapter();
        mMonthGrid.setAdapter(mAdapter);
        mMonthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calendar.set(Calendar.MONTH, position);
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    public MonthAndYearPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View makeView() {
        TextView year = new TextView(mContext);
        year.setGravity(Gravity.CENTER);
        year.setTextColor(getResources().getColor(R.color.Tackle_Green));
        year.setTextSize(28);

        return year;
    }

    public long getDateTime(){
        return calendar.getTimeInMillis();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_month:
                calendar.add(Calendar.YEAR, 1);
                mSwitcher.setInAnimation(mContext, R.anim.slide_in_right_fade);
                mSwitcher.setOutAnimation(mContext, R.anim.slide_out_right_fade);
                mSwitcher.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                break;
            case R.id.previous_month:
                calendar.add(Calendar.YEAR, -1);
                mSwitcher.setInAnimation(mContext, R.anim.slide_in_left_fade);
                mSwitcher.setOutAnimation(mContext, R.anim.slide_out_left_fade);
                mSwitcher.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                break;
        }
    }

    private class MonthGridAdapter extends BaseAdapter{

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        @Override
        public int getCount() {
            return months.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView month = new TextView(getContext());
            month.setTextSize(28);
            month.setGravity(Gravity.CENTER);
            month.setPadding(8,12,8,12);
            month.setText(months[position]);

            if (position == calendar.get(Calendar.MONTH)){
                month.setTextColor(getResources().getColor(R.color.Tackle_Green));
            }

            return month;
        }
    }


}
