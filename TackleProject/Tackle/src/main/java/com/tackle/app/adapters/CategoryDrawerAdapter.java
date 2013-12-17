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

import com.tackle.app.R;
import com.tackle.app.data.TackleContract;

/**
 * Created by Bill on 12/16/13.
 */
public class CategoryDrawerAdapter extends CursorAdapter {
    LayoutInflater layoutInflater;

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

        String category = cursor.getString(cursor.getColumnIndex(TackleContract.Categories.NAME));
        int categoryID = cursor.getInt(cursor.getColumnIndex(TackleContract.Categories.ID));
        holder.category.setText(category);

        Cursor count = context.getContentResolver().query(TackleContract.TackleItems.CONTENT_URI, new String[]{TackleContract.TackleItems.ID, TackleContract.TackleItems.CATEGORY_ID}, categoryID + "=" + TackleContract.TackleItems.CATEGORY_ID, null, null);
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
}
