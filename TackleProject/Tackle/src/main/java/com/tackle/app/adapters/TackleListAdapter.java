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
import android.widget.Toast;

import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bill on 12/17/13.
 */
public class TackleListAdapter extends CursorAdapter {
    LayoutInflater inflater;

    public TackleListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View customListView = null;
        int type = cursor.getInt(cursor.getColumnIndex(TackleContract.TackleItems.TYPE));
        switch (type){
            case TackleContract.TODO:
                customListView = inflater.inflate(R.layout.list_layout_todo, parent, false);
                TodoHolder todoHolder = new TodoHolder();
                todoHolder.typeIcon = (ImageView) customListView.findViewById(R.id.type_icon);
                todoHolder.category = (TextView) customListView.findViewById(R.id.category);
                todoHolder.title = (TextView) customListView.findViewById(R.id.title);
                todoHolder.date = (TextView) customListView.findViewById(R.id.date);
                customListView.setTag(todoHolder);
                break;
            case TackleContract.LIST:
                customListView = inflater.inflate(R.layout.list_layout_list, parent, false);
                ListHolder listHolder = new ListHolder();
                listHolder.typeIcon = (ImageView) customListView.findViewById(R.id.type_icon);
                listHolder.category = (TextView) customListView.findViewById(R.id.category);
                listHolder.title = (TextView) customListView.findViewById(R.id.title);
                listHolder.items = (TextView) customListView.findViewById(R.id.items);
                customListView.setTag(listHolder);
                break;
            case TackleContract.EVENT:
                customListView = inflater.inflate(R.layout.list_layout_event, parent, false);
                EventHolder eventHolder = new EventHolder();
                eventHolder.typeIcon = (ImageView) customListView.findViewById(R.id.type_icon);
                eventHolder.category = (TextView) customListView.findViewById(R.id.category);
                eventHolder.title = (TextView) customListView.findViewById(R.id.title);
                eventHolder.date = (TextView) customListView.findViewById(R.id.date);
                eventHolder.time = (TextView) customListView.findViewById(R.id.time);
                customListView.setTag(eventHolder);
                break;
            case TackleContract.NOTE:
                customListView = inflater.inflate(R.layout.list_layout_note, parent, false);
                NoteHolder noteHolder = new NoteHolder();
                noteHolder.typeIcon = (ImageView) customListView.findViewById(R.id.type_icon);
                noteHolder.category = (TextView) customListView.findViewById(R.id.category);
                noteHolder.title = (TextView) customListView.findViewById(R.id.title);
                customListView.setTag(noteHolder);
                break;
        }


        return customListView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int type = cursor.getInt(cursor.getColumnIndex(TackleContract.TackleItems.TYPE));

        Cursor categoryCursor = getCategoryCursor(context, cursor);
        categoryCursor.moveToFirst();
        int fill = Color.parseColor(categoryCursor.getString(categoryCursor.getColumnIndex(TackleContract.Categories.COLOR)));
        ColorFilter colorFilter = new PorterDuffColorFilter(fill, PorterDuff.Mode.SRC_ATOP);
        String categoryText = categoryCursor.getString(categoryCursor.getColumnIndex(TackleContract.Categories.NAME));
        categoryCursor.close();


        switch (type){
            case TackleContract.TODO:
                TodoHolder holder = (TodoHolder) view.getTag();
                holder.category.setText(categoryText);
                holder.category.setTextColor(fill);
                holder.typeIcon.setColorFilter(colorFilter);
                holder.title.setText(cursor.getString(cursor.getColumnIndex(TackleContract.TackleItems.NAME)));
                holder.date.setText(getDate(cursor));
                break;
            case TackleContract.LIST:
                ListHolder holder1 = (ListHolder) view.getTag();
                holder1.category.setText(categoryText);
                holder1.category.setTextColor(fill);
                holder1.typeIcon.setColorFilter(colorFilter);
                holder1.title.setText(cursor.getString(cursor.getColumnIndex(TackleContract.TackleItems.NAME)));
                int qty = getListItemCount(context, cursor);
                String count;
                if (qty == 1){
                    count = "1 item";
                }
                else {
                    count = String.valueOf(qty) + " items";
                }
                holder1.items.setText(count);
                break;
            case TackleContract.EVENT:
                EventHolder holder2 = (EventHolder) view.getTag();
                holder2.category.setText(categoryText);
                holder2.category.setTextColor(fill);
                holder2.typeIcon.setColorFilter(colorFilter);
                holder2.title.setText(cursor.getString(cursor.getColumnIndex(TackleContract.TackleItems.NAME)));
                holder2.date.setText(getDate(cursor));
                holder2.time.setText(getTime(cursor));
                break;
            case TackleContract.NOTE:
                NoteHolder holder3 = (NoteHolder) view.getTag();
                holder3.category.setText(categoryText);
                holder3.category.setTextColor(fill);
                holder3.typeIcon.setColorFilter(colorFilter);
                holder3.title.setText(cursor.getString(cursor.getColumnIndex(TackleContract.TackleItems.NAME)));
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return cursor.getInt(cursor.getColumnIndex(TackleContract.TackleItems.TYPE));
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    private int getListItemCount(Context context, Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(TackleContract.TackleItems.ID));
        String[] projection = {TackleContract.ListItems.ID, TackleContract.ListItems.LIST_ID};
        Cursor listCursor = context.getContentResolver().query(TackleContract.ListItems.CONTENT_URI, projection, TackleContract.ListItems.LIST_ID + "=" + id, null, null);
        listCursor.moveToFirst();
        int count = listCursor.getCount();
        listCursor.close();
        return count;
    }

    private Cursor getCategoryCursor(Context context, Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(TackleContract.TackleItems.CATEGORY_ID));
        String[] projection = {TackleContract.Categories.ID, TackleContract.Categories.NAME, TackleContract.Categories.COLOR};
        return context.getContentResolver().query(TackleContract.Categories.CONTENT_URI, projection, TackleContract.Categories.ID + "=" + id, null, null);
    }

    private String getDate(Cursor cursor){
        SimpleDateFormat formater = new SimpleDateFormat("MMMM dd");
        long dateLong = cursor.getLong(cursor.getColumnIndex(TackleContract.TackleItems.START_DATE));

        Date cal = Calendar.getInstance().getTime();
        cal.setTime(dateLong);

        String date = formater.format(cal);
        return date;

    }

    private String getTime(Cursor cursor){
        SimpleDateFormat formater = new SimpleDateFormat("h:mm a");
        long dateLong = cursor.getLong(cursor.getColumnIndex(TackleContract.TackleItems.START_DATE));

        Date cal = Calendar.getInstance().getTime();
        cal.setTime(dateLong);

        String time = formater.format(cal);
        return time;
    }

    public static class TodoHolder{
        public ImageView typeIcon;
        public TextView title;
        public TextView category;
        public TextView date;
    }

    public static class ListHolder{
        public ImageView typeIcon;
        public TextView title;
        public TextView category;
        public TextView items;
    }
    public static class EventHolder{
        public ImageView typeIcon;
        public TextView title;
        public TextView category;
        public TextView date;
        public TextView time;
    }
    public static class NoteHolder{
        public ImageView typeIcon;
        public TextView title;
        public TextView category;
    }

}
