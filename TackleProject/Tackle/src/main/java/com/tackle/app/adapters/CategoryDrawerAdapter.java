package com.tackle.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tackle.app.MainActivity;
import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

import java.util.Calendar;

/**
 * Created by Bill on 12/16/13.
 */
public class CategoryDrawerAdapter extends CursorAdapter {
    private static final int MILLI_PER_SECOND = 1000;
    private static final int SEC_PER_HOUR = 3600;

    LayoutInflater layoutInflater;
    int mViewState;
    long mDateTime;

    public CategoryDrawerAdapter(Context context, Cursor cursor, boolean autoRequery){
        super(context, cursor, autoRequery);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.category_drawer_item, parent, false);

        Holder holder = new Holder();
        holder.category = (TextView) view.findViewById(R.id.category);
        holder.color = (ImageView) view.findViewById(R.id.cat_color);
        holder.count = (TextView) view.findViewById(R.id.count);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = (Holder) view.getTag();

        String category = cursor.getString(cursor.getColumnIndex(TackleContract.Categories.CATEGORY_NAME));
        int categoryID = cursor.getInt(cursor.getColumnIndex(TackleContract.Categories._ID));
        holder.category.setText(category);

        String selection = TackleContract.TackleEvent.START_DATE + " >= ? AND " + TackleContract.TackleEvent.START_DATE + " <= ? AND " + TackleContract.TackleEvent.CATEGORY_ID + " = ?";
        String[] projection = {TackleContract.TackleEvent._ID, TackleContract.TackleEvent.CATEGORY_ID, TackleContract.TackleEvent.START_DATE};
        String[] selectionArgs = new String[3];
        long start;
        long end;

        switch (mViewState){
            case MainActivity.VIEW_STATE_DAY:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(mDateTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                start = cal.getTimeInMillis();
                end = start + (24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                selectionArgs = new String[]{String.valueOf(start), String.valueOf(end), String.valueOf(categoryID)};
                break;
            case MainActivity.VIEW_STATE_WEEK:
                Calendar week = Calendar.getInstance();
                week.setTimeInMillis(mDateTime);
                week.set(Calendar.HOUR_OF_DAY, 0);
                week.set(Calendar.MINUTE, 0);
                week.set(Calendar.SECOND, 0);
                week.set(Calendar.MILLISECOND, 0);
                start = week.getTimeInMillis();
                end = start + (5 * 24 * SEC_PER_HOUR * MILLI_PER_SECOND);
                selectionArgs = new String[]{String.valueOf(start), String.valueOf(end), String.valueOf(categoryID)};
                break;
        }

        Cursor count = context.getContentResolver().query(TackleContract.TackleEvent.CONTENT_URI, projection, selection, selectionArgs, null);
        count.moveToFirst();
        holder.count.setText(String.valueOf(count.getCount()));
        count.close();

        int fill = Color.parseColor(cursor.getString(cursor.getColumnIndex(TackleContract.Categories.COLOR)));
        ColorFilter filter = new PorterDuffColorFilter(fill, PorterDuff.Mode.SRC_ATOP);

        holder.color.setColorFilter(filter);
    }

    private static class Holder{
        TextView category;
        ImageView color;
        TextView count;

    }

    public void setDateTime(long date){
        mDateTime = date;
    }

    public void setViewState(int state){
        mViewState = state;
    }
}
